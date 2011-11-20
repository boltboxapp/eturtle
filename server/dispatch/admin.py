from django.contrib import admin
from dispatch.models import Courier, Dispatch, Package, Client

class CourierAdmin(admin.ModelAdmin):
    list_display = ('username', 'state', 'lat', 'lng', 'c2dmkey')
    list_filter = ('state',)

class PackageAdmin(admin.ModelAdmin):
    list_display = ('name', 'state', 'client', 'source', 'destination')
    list_filter = ('state', 'client')

class DispatchAdmin(admin.ModelAdmin):
    list_display = ('courier', 'package', 'state', 'date_modified')
    list_filter = ('courier', 'state')

admin.site.register(Courier, CourierAdmin)
admin.site.register(Package, PackageAdmin)
admin.site.register(Dispatch, DispatchAdmin)
admin.site.register(Client)