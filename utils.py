from django.http import Http404
from django.utils.decorators import method_decorator
from django.contrib.auth.decorators import login_required

class LoginRequiredMixin(object):
    u"""Ensures that user must be authenticated in order to access view."""

    @method_decorator(login_required)
    def dispatch(self, *args, **kwargs):
        return super(LoginRequiredMixin, self).dispatch(*args, **kwargs)

class AdminOr404Mixin(object):
    u"""Ensures that user must has the eturtle_admin parrmission in order to access view."""

    def dispatch(self, *args, **kwargs):
        request = args[0]
        if not request.user.has_perm('dispatch.eturtle_admin'):
            raise Http404()
        return super(AdminOr404Mixin, self).dispatch(*args, **kwargs)