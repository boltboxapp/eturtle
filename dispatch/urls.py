from django.conf.urls.defaults import patterns, include, url
from django.core.urlresolvers import reverse
from django.views.generic.simple import redirect_to

urlpatterns = patterns('',
   
    url(r'^login/', 'dispatch.views.loginview'),
    url(r'^packages/', 'dispatch.views.loginview', name="package_list"),
    url(r'^clients/', 'dispatch.views.loginview', name="client_list"),
    url(r'^couriers/', 'dispatch.views.loginview', name="courier_list"),
)
