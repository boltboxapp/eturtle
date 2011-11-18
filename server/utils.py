from django.http import Http404, HttpResponse
from django.utils.decorators import method_decorator
from django.contrib.auth.decorators import login_required, permission_required

class WebLoginRequiredMixin(object):
    u"""Ensures that user must be authenticated in order to access view."""

    @method_decorator(permission_required('dispatch.web_access'))
    def dispatch(self, *args, **kwargs):
        return super(WebLoginRequiredMixin, self).dispatch(*args, **kwargs)

class AdminOr404Mixin(object):
    u"""Ensures that user must has the eturtle_admin parrmission in order to access view."""

    def dispatch(self, *args, **kwargs):
        request = args[0]
        if not request.user.has_perm('dispatch.eturtle_admin'):
            raise Http404()
        return super(AdminOr404Mixin, self).dispatch(*args, **kwargs)

class HttpResponseUnauthorized(HttpResponse):
    def __init__(self, content='', mimetype=None, status=None, content_type=None):
        super(HttpResponseUnauthorized, self).__init__(content=content,
                                                       mimetype=mimetype,
                                                       status=status,
                                                       content_type=content_type)
        self.status_code = 401

def api_permission_required(the_func):
    def _decorated(request, *args, **kwargs):
        print request.user
        if request.user.is_authenticated() and request.user.has_perm("dispatch.api_access"):
            return the_func(request,*args, **kwargs)
        else:
            return HttpResponseUnauthorized('Unathorized')
    return _decorated
