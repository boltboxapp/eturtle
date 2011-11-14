from django.conf.urls.defaults import patterns, url
from dispatch.views import PackageListView, ClientListView, CourierListView

urlpatterns = patterns('',
   
    url(r'^login/', 'dispatch.views.loginview'),
    url(r'^packages/', PackageListView.as_view(), name="package_list"),
    url(r'^clients/', ClientListView.as_view(), name="client_list"),
    url(r'^couriers/', CourierListView.as_view(), name="courier_list"),
)
