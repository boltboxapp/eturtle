from django.test import TestCase
from django.core.urlresolvers import reverse
from django.core.management import call_command
from dispatch.models import *


"""Packages
#1     
"name": "Arvizturotukorfurogep", 
"destination": "Fovam Ter",
"source": "BME E epulet",
#2
"name": "Sinkurkaszo piszkakompozitor", 
"source": "Deak Ter",
"destination": "Oktogon",
#3
"name": "K\u00e1poszta", 
"destination": "Budapest, Budafoki ut 120,", 
"source": "Kiraly utca 10",
#4
"name": "S\u00f3",
"destination": "Budapest Rozsak tere",
"source": "Budapest rakoczi ut 56",     

"""

TEST_LOCATIONS = {
    'roka':     {'lat':'47.480451', 'lng':'19.084947'},
    'martos':   {'lat': '47.480233','lng':'19.055851'},
    "bme_e":    {'lat':"47.477834", "lng":"19.057693"},
    "fovam":    {"lat":"47.487669", "lng":"19.058141"},
    "oktogon":  {"lat":"47.505468","lng":"19.063414"},
    "deak":     {"lat":"47.49752", "lng":"19.055104"},
    "buda120":  {"lat":"47.450314", "lng":"19.05039"},
    "kiraly10": {"lat":"47.498534", "lng":"19.057449"},
    "rakoczi56":{"lat":"47.501686", "lng":"19.076048"},
    "rozsak":   {"lat":"47.501216", "lng":"19.076804"},
}



class SimpleTest(TestCase):
    
    def setUp(self):
        call_command('loaddata','fixtures/initial_auth.json', interactive=True)
        call_command('loaddata','fixtures/initial_dispatch.json', interactive=True)
        call_command('loaddata','fixtures/initial_sites.json', interactive=True)
        call_command('initialpermissions', interactive=True)

    def test_login_status_codes(self):
        response = self.client.post(reverse('api_login'), 
                                { 'username':'foo', } )
        self.assertEquals(response.status_code,400)
        response = self.client.post(reverse('api_login'), 
                                { 'username':'bad', 'password':'usernameandpassword', } )
        self.assertEquals(response.status_code,401)
        response = self.client.post(reverse('api_login'), 
                                { 'username':'roka', 'password':'roka', } )
        self.assertEquals(response.status_code,200)
    
    def test_location_update(self):
        response = self.client.post(reverse('api_login'), { 'username':'roka', 'password':'roka', } )
        self.assertEquals(response.status_code,200)
        response = self.client.post(reverse('api_loc_update'),TEST_LOCATIONS['roka'])
        self.assertEquals(response.status_code,200)
        roka = Courier.objects.get(username="roka")
        self.assertEquals(roka.lat,TEST_LOCATIONS['roka']['lat'] )
        self.assertEquals(roka.lng,TEST_LOCATIONS['roka']['lng'] )

    def test_dispatcher_one(self):
        """
        Test dispatcher with 4 packages and one courier.
        Expected result is that one package gets dispatched to the courier.

        """
        #log roka in
        response = self.client.post(reverse('api_login'), { 'username':'roka', 'password':'roka', } )
        self.assertEquals(response.status_code,200)
        #update location
        response = self.client.post(reverse('api_loc_update'),TEST_LOCATIONS['rozsak'])
        self.assertEquals(response.status_code,200)
        
        #no packages should be assigned yet
        self.assertEquals(Package.objects.filter(state = Package.STATE_PENDING).count(),0)
        self.assertEquals(Package.objects.filter(state = Package.STATE_NEW).count(),4)
        #check in, this should make the dispatcher run
        response = self.client.get(reverse('api_checkin'),{})
        self.assertEquals(response.status_code,200)
        #a package should have been set to pending now
        self.assertEquals(Package.objects.filter(state = Package.STATE_PENDING).count(),1)
        #is the courier pending
        self.assertEquals(Courier.objects.get(username='roka').state, Courier.STATE_PENDING)
        #is the dispatch pending?
        self.assertEquals(Dispatch.objects.get(pk=1).state, Dispatch.STATE_PENDING)

    def test_dispatcher_backoff(self):
        """
        Test dispatcher with 2 couriers and one package.
        Expected result is that the package gets assigned to the closer courier


        package 2: Deak Ter

        courier 1: rozsak tere
        courier 2: kiraly10

        Expected result: p2c2, 

        c2 rejects, the next time dispatcher runs it should be dispatched to c1
        c1 rejects, then it is redispatched to c1, and only c1 no matter how many times he rejects
        c1 leaves, c2 gets the package

        """  
        #first clear all packages
        for p in Package.objects.all():
            p.state = Package.STATE_SHIPPED
            p.save()
        self.assertEquals(Package.objects.filter(state = Package.STATE_SHIPPED).count(),4)
        self.assertEquals(Package.objects.count(),4)

        #log roka in
        response = self.client.post(reverse('api_login'), { 'username':'roka', 'password':'roka', } )
        self.assertEquals(response.status_code,200)
        #update location
        response = self.client.post(reverse('api_loc_update'),TEST_LOCATIONS['rozsak'])
        self.assertEquals(response.status_code,200)
        #check in
        response = self.client.get(reverse('api_checkin'),{})
        self.assertEquals(response.status_code,200)

        #log teki in
        response = self.client.post(reverse('api_login'), { 'username':'teki', 'password':'teki', } )
        self.assertEquals(response.status_code,200)
        #update location
        response = self.client.post(reverse('api_loc_update'),TEST_LOCATIONS['kiraly10'])
        self.assertEquals(response.status_code,200)
        #check in
        response = self.client.get(reverse('api_checkin'),{})
        self.assertEquals(response.status_code,200)

        roka = Courier.objects.get(username='roka')
        teki = Courier.objects.get(username='teki')

        self.assertEquals(roka.state,Courier.STATE_STANDING_BY)
        self.assertEquals(teki.state,Courier.STATE_STANDING_BY)
       
        #log roka in
        response = self.client.login(username="elek", password="elek")
        self.assertTrue(response)
        response = self.client.post(reverse('package_add'),
            {"name":"Test Package",
            "source":"Budapest Deak Ferenc ter",
            "destination": "Kaszasdulo 1037 Budapest",
            "src_lat":"47.497498",
            "src_lng":"19.0547950",
            "dst_lat":"47.556774",
            "dst_lng":"19.0452659"})
        

        self.assertEquals(Dispatch.objects.count(),1)


        self.assertEquals(Courier.objects.get(username='roka').state,Courier.STATE_STANDING_BY)
        self.assertEquals(Courier.objects.get(username='teki').state,Courier.STATE_PENDING)
        self.assertEquals(Package.objects.count(),5)
        self.assertEquals(Package.objects.get(pk=1).state,Package.STATE_SHIPPED)
        self.assertEquals(Package.objects.get(pk=2).state,Package.STATE_SHIPPED)
        self.assertEquals(Package.objects.get(pk=3).state,Package.STATE_SHIPPED)
        self.assertEquals(Package.objects.get(pk=4).state,Package.STATE_SHIPPED)
        self.assertEquals(Package.objects.get(pk=5).state,Package.STATE_PENDING)

        self.client.logout()
        #teki rejects
        #log teki in
        response = self.client.post(reverse('api_login'), { 'username':'teki', 'password':'teki', } )
        self.assertEquals(response.status_code,200)
        #reject
        response = self.client.get(reverse('api_decline'),{})
        self.assertEquals(response.status_code,200)
        #this will run the dispatcher again

        self.assertEquals(Courier.objects.get(username='roka').state,Courier.STATE_PENDING)
        self.assertEquals(Courier.objects.get(username='teki').state,Courier.STATE_STANDING_BY)
        

        self.client.logout()
        #roka rejects
        #log roka in
        response = self.client.post(reverse('api_login'), { 'username':'roka', 'password':'roka', } )
        self.assertEquals(response.status_code,200)
        #reject
        response = self.client.get(reverse('api_decline'),{})
        self.assertEquals(response.status_code,200)
        #this will run the dispatcher again

        self.assertEquals(Courier.objects.get(username='roka').state,Courier.STATE_STANDING_BY)
        self.assertEquals(Courier.objects.get(username='teki').state,Courier.STATE_PENDING)

        #retry a few more times, teki still gets the package
        for i in range(1,5):
            self.client.logout()
            #teki rejects
            #log teki in
            response = self.client.post(reverse('api_login'), { 'username':'teki', 'password':'teki', } )
            self.assertEquals(response.status_code,200)
            #reject
            response = self.client.get(reverse('api_decline'),{})
            self.assertEquals(response.status_code,200)
            #this will run the dispatcher again

            self.assertEquals(Courier.objects.get(username='roka').state,Courier.STATE_STANDING_BY)
            self.assertEquals(Courier.objects.get(username='teki').state,Courier.STATE_PENDING)
        
        response = self.client.get(reverse('api_leave'),{})
        self.assertEquals(response.status_code,200)
        #this will run the dispatcher again

        self.assertEquals(Courier.objects.get(username='roka').state,Courier.STATE_PENDING)
        self.assertEquals(Courier.objects.get(username='teki').state,Courier.STATE_IDLE)


    def test_dispatch_timeout(self):
        """
        Test dispatcher timeout. After x seconds 
            the package state should be new
            the courier state should be idle
            the dispatch state should be rejected

        """
        #log roka in
        response = self.client.post(reverse('api_login'), { 'username':'roka', 'password':'roka', } )
        self.assertEquals(response.status_code,200)
        #update location
        response = self.client.post(reverse('api_loc_update'),TEST_LOCATIONS['rozsak'])
        self.assertEquals(response.status_code,200)
        
        #no packages should be assigned yet
        self.assertEquals(Package.objects.filter(state = Package.STATE_PENDING).count(),0)
        self.assertEquals(Package.objects.filter(state = Package.STATE_NEW).count(),4)
        #check in, this should make the dispatcher run
        response = self.client.get(reverse('api_checkin'),{})
        self.assertEquals(response.status_code,200)
        #a package should have been set to pending now
        self.assertEquals(Package.objects.filter(state = Package.STATE_PENDING).count(),1)
        #is the courier pending
        self.assertEquals(Courier.objects.get(username='roka').state, Courier.STATE_PENDING)
        #is the dispatch pending?
        self.assertEquals(Dispatch.objects.get(pk=1).state, Dispatch.STATE_PENDING)
        import time
        time.sleep(1)
        call_command('run_dispatcher','1')
        self.assertEquals(Dispatch.objects.get(pk=1).state, Dispatch.STATE_TIMED_OUT)
        self.assertEquals(Dispatch.objects.get(pk=1).courier.state, Courier.STATE_IDLE)
        self.assertEquals(Dispatch.objects.get(pk=1).package.state, Package.STATE_NEW)
    


    def test_dispatch_full(self):
        """
        tests the whole package delivery flow
        """
        pass