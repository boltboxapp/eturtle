from django.conf.urls.defaults import patterns, include, url
from dispatch.urls import *
# Uncomment the next two lines to enable the admin:
from django.contrib import admin
admin.autodiscover()

from dispatch.api import PackageResource

package_resource = PackageResource()

   

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'eturtle.views.home', name='home'),
    #url(r'^eturtle/', include('eturtle.foo.urls')),

    # Uncomment the admin/doc line below to enable admin documentation:
    url(r'^admin/doc/', include('django.contrib.admindocs.urls')),
 	url(r'^api/', include(package_resource.urls)),
 	url(r'^dispatch/', include('dispatch.urls')),

    # Uncomment the next line to enable the admin:
    url(r'^admin/', include(admin.site.urls)),
)
