"""
This file demonstrates writing tests using the unittest module. These will pass
when you run "manage.py test".

Replace this with more appropriate tests for your application.
"""

from django.test import TestCase
from django.core.urlresolvers import reverse
from django.core.management import call_command
class SimpleTest(TestCase):
    
    def setUp(self):
        import os
        os.system('pwd')
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
