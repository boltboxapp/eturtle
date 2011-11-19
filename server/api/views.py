from django.contrib.auth import authenticate
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
from django.contrib.auth import login as auth_login
from dispatch.models import ETurtleGroup as Group
from server.dispatch.models import Courier
from server.utils import api_permission_required, HttpResponseUnauthorized

@csrf_exempt
def loginview(request):
    username = request.POST.get('username')
    password = request.POST.get('password')

    user = authenticate(username=username, password=password)
    if user and user.is_active and user.has_perm("dispatch.api_access"):
        auth_login(request, user)
        return HttpResponse("Logged in")

    return HttpResponseUnauthorized('Unathorized')

@api_permission_required
def check_in(request):
    courier = Courier.objects.get(id=request.user.id)
    courier.state = Courier.STATE_STANDING_BY
    return HttpResponse('checked in')

@api_permission_required
def leave(request):
    courier = Courier.objects.get(id=request.user.id)
    courier.state = Courier.STATE_IDLE
    #TODO: decline package if dispatched
    return HttpResponse('left')

@api_permission_required
def decline(request):
    #TODO:implement
    return HttpResponse('declined')

@api_permission_required
def accept(request):
    #TODO:implement
    return HttpResponse('accepted')

@api_permission_required
def complete(request):
    #TODO:implement
    return HttpResponse('completed')

@api_permission_required
def fail(request):
    #TODO:implement
    return HttpResponse('failed')
