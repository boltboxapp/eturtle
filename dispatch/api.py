from tastypie.resources import ModelResource
from dispatch.models import Package
from tastypie.authentication import BasicAuthentication


class PackageResource(ModelResource):
    class Meta:
        queryset = Package.objects.all()
        resource_name = 'package'
        authentication = BasicAuthentication()