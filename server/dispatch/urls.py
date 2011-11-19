from django.conf.urls.defaults import patterns, url
from dispatch.views import PackageListView, ClientListView, CourierListView, PackageCreateView, CourierCreateView
from server.dispatch.views import ClientToggleView, CourierToggleView, CourierUpdateView, PackageDetailView

urlpatterns = patterns('',
   
    url(r'^clients/(?P<pk>\d)/toggle/', ClientToggleView.as_view(), name="client_toggle"),
    url(r'^clients/', ClientListView.as_view(), name="client_list"),

    url(r'^packages/(?P<pk>\d)/', PackageDetailView.as_view(), name="package_detail"),
    url(r'^packages/new/', PackageCreateView.as_view(), name="package_add"),
    url(r'^packages/', PackageListView.as_view(), name="package_list"),

    url(r'^couriers/new/', CourierCreateView.as_view(), name="courier_add"),
    url(r'^couriers/(?P<pk>\d)/toggle/', CourierToggleView.as_view(), name="courier_toggle"),
    url(r'^couriers/(?P<pk>\d)/edit/', CourierUpdateView.as_view(), name="courier_edit"),
    url(r'^couriers/', CourierListView.as_view(), name="courier_list"),
)
