from django.conf.urls.defaults import patterns, url
from dispatch.views import PackageListView

urlpatterns = patterns('',
   
    url(r'^login/', 'dispatch.views.loginview'),
    url(r'^packages/', PackageListView.as_view(), name="package_list"),
    url(r'^clients/', 'dispatch.views.loginview', name="client_list"),
    url(r'^couriers/', 'dispatch.views.loginview', name="courier_list"),
)
