from django.conf.urls.defaults import patterns, include, url
from dispatch.urls import *
from django.contrib import admin
from django.core.urlresolvers import reverse
from django.views.generic.simple import redirect_to
from django.utils.functional import lazy
from server.dispatch.views import ProfileView

reverse_lazy = lazy(reverse, unicode)
admin.autodiscover()

urlpatterns = patterns('',

    #dispacth app urls
    url(r'^dispatch/', include('dispatch.urls')),

    #admin urls
    url(r'^admin/doc/', include('django.contrib.admindocs.urls')),
    url(r'^admin/', include(admin.site.urls)),

    #api urls
    url(r'^api/', include('api.urls')),

    #registration+auth app urls
    url(r'^accounts/', include('registration.backends.default.urls')),
    url(r'^accounts/profile/', ProfileView.as_view(), name="profile"),

    #redirect to package_list(in dispatch app)
    url(r'^$', redirect_to, {'url': reverse_lazy('package_list')}),

)
