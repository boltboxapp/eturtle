from django.conf.urls.defaults import patterns, include, url
from dispatch.urls import *
# Uncomment the next two lines to enable the admin:
from django.contrib import admin
import registration

admin.autodiscover()

from dispatch.api import PackageResource

package_resource = PackageResource()

   

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'eturtle.views.home', name='home'),
    #url(r'^eturtle/', include('eturtle.foo.urls')),

    url(r'^admin/doc/', include('django.contrib.admindocs.urls')),
 	url(r'^api/', include(package_resource.urls)),
 	url(r'^dispatch/', include('dispatch.urls')),

    url(r'^admin/', include(admin.site.urls)),

    url(r'^accounts/', include('registration.backends.default.urls')),
)
