from django.contrib.auth import authenticate
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
from django.contrib.auth import login as auth_login
from dispatch.models import ETurtleGroup as Group
from server.utils import api_login_required, HttpResponseUnauthorized

@csrf_exempt
def loginview(request):
    username = request.POST.get('username')
    password = request.POST.get('password')

    user = authenticate(username=username, password=password)
    if user and user.is_active and user.has_perm("dispatch.api_access"):
        auth_login(request, user)
        return HttpResponse("Logged in")

    return HttpResponseUnauthorized('Unathorized')

@api_login_required
def checkin(request):
    return HttpResponse('success')