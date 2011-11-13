from tastypie.resources import ModelResource
from dispatch.models import Package
from tastypie.authentication import Authentication


class CustomAuthentication(Authentication):
    def is_authenticated(self, request, **kwargs):
        return request.user.is_authenticated()


class PackageResource(ModelResource):
    class Meta:
        queryset = Package.objects.all()
        resource_name = 'package'
        authentication = CustomAuthentication()