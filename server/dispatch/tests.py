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
    "kiraly10": {"lat":"47.498394", "lng":"19.057782"},
    "rakoczi56":{"lat":"47.501686", "lng":"19.076048"},
    "rozsak":   {"lat":"47.498394", "lng":"19.057782"},
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

    def test_dispatcher_two(self):
        """
        Test dispatcher with 2 couriers and one package.
        Expected result is that the package gets assigned to the closer courier


        package 2: Deak Ter

        courier 1: Rozsak tere
        courier 2: Budafoki 120

        Expected result: p2c1, 
        """  
        #first clear all packages
        for p in Package.objects.all():
            p.state = Package.STATE_PENDING
            p.save()
        self.assertEquals(Package.objects.filter(state = Package.STATE_PENDING).count(),4)

        #log roka in
        response = self.client.post(reverse('api_login'), { 'username':'teki', 'password':'teki', } )
        self.assertEquals(response.status_code,200)
        #update location
        response = self.client.post(reverse('api_loc_update'),TEST_LOCATIONS['buda120'])
        self.assertEquals(response.status_code,200)
        #check in
        response = self.client.get(reverse('api_checkin'),{})
        self.assertEquals(response.status_code,200)

        #log roka in
        response = self.client.post(reverse('api_login'), { 'username':'roka', 'password':'roka', } )
        self.assertEquals(response.status_code,200)
        #update location
        response = self.client.post(reverse('api_loc_update'),TEST_LOCATIONS['rozsak'])
        self.assertEquals(response.status_code,200)
        #check in
        response = self.client.get(reverse('api_checkin'),{})
        self.assertEquals(response.status_code,200)

        

        
     
        

    def test_dispatch_timeout(self):
        """
        dispatch timeout
        """
        pass
    
    def test_dispatch_full(self):
        """
        tests the whole package delivery flow
        """
        pass