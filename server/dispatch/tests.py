"""
This file demonstrates writing tests using the unittest module. These will pass
when you run "manage.py test".

Replace this with more appropriate tests for your application.
"""

from django.test import TestCase
from django.core.urlresolvers import reverse
from django.core.management import call_command
from dispatch.models import *
TEST_LOCATIONS = {
    'roka': {'lat':'47.480451', 'lng':'19.084947'}

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