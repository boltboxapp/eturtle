from django.conf.urls.defaults import patterns, url
from dispatch.views import PackageListView, ClientListView, CourierListView, PackageCreateView, CourierCreateView

urlpatterns = patterns('',
   
    url(r'^clients/', ClientListView.as_view(), name="client_list"),

    url(r'^packages/new/', PackageCreateView.as_view(), name="package_add"),
    url(r'^packages/', PackageListView.as_view(), name="package_list"),

    url(r'^couriers/new/', CourierCreateView.as_view(), name="courier_add"),
    url(r'^couriers/', CourierListView.as_view(), name="courier_list"),
)
