from django.conf.urls.defaults import patterns, url
from api.views import checkin, loginview

urlpatterns = patterns('',
    url(r'^login/', loginview, name="api_login"),
    url(r'^checkin/', checkin, name="api_checkin"),
)
