from datetime import datetime
import logging
from django.contrib.auth import authenticate
from django.core import serializers
from django.http import HttpResponse, HttpResponseBadRequest
from django.shortcuts import get_object_or_404
from django.views.decorators.csrf import csrf_exempt
from django.contrib.auth import login as auth_login
from dispatch.models import ETurtleGroup as Group
from server.dispatch.dispatcher import run_dispatcher
from server.dispatch.models import Courier, Dispatch, Package
from server.utils import api_permission_required, HttpResponseUnauthorized
import json

@csrf_exempt
def loginview(request):
    if not request.method=='POST':
        return HttpResponseBadRequest("post required")
    username = request.POST.get('username' or None)
    password = request.POST.get('password' or None)
    if not (username and password):
        return HttpResponseBadRequest("invalid or missing parameters")

    user = authenticate(username=username, password=password)
    if user and user.is_active and user.has_perm("dispatch.api_access"):
        auth_login(request, user)
        return HttpResponse("Logged in")

    return HttpResponseUnauthorized('Unathorized')

@api_permission_required
def check_in(request):
    courier = Courier.objects.get(id=request.user.id)
    courier.state = Courier.STATE_STANDING_BY
    courier.save()
    run_dispatcher()
    return HttpResponse('checked in')

@api_permission_required
def leave(request):
    courier = Courier.objects.get(id=request.user.id)
    courier.state = Courier.STATE_IDLE
    courier.save()

    try:
        dispatch = Dispatch.objects.get(courier=courier, state=Dispatch.STATE_PENDING)
    except Dispatch.DoesNotExist:
        pass
    else:
        #updates the state of the Dispatch
        dispatch.state = Dispatch.STATE_REJECTED
        dispatch.save()

        #updates the state of the Package
        dispatch.package.state=Package.STATE_NEW
        dispatch.package.save()

    return HttpResponse('left')

@api_permission_required
def decline(request):
    courier = Courier.objects.get(id=request.user.id)
    dispatch = get_object_or_404(Dispatch, courier=courier, state=Dispatch.STATE_PENDING)

    #updates the state of the Dispatch
    dispatch.state = Dispatch.STATE_REJECTED
    dispatch.save()

    #updates the state of the Courier
    courier.state = Courier.STATE_STANDING_BY
    courier.save()

    #updates the state of the Package
    dispatch.package.state=Package.STATE_NEW
    dispatch.package.save()

    return HttpResponse('declined')

@api_permission_required
def get(request):
    courier = Courier.objects.get(id=request.user.id)

    dispatch = get_object_or_404(Dispatch, courier=courier, state=Dispatch.STATE_PENDING)

    package = dispatch.package

    dump = package.serialize()
    response = HttpResponse(dump)
    response['Content-Type'] = 'application/json; charset=utf-8'
    return response

@api_permission_required
def accept(request):
    courier = Courier.objects.get(id=request.user.id)

    #get the corresponding Dispatch object
    dispatch = get_object_or_404(Dispatch, courier=courier, state=Dispatch.STATE_PENDING)

    #updates the state of the pending dispatch
    dispatch.state=Dispatch.STATE_SHIPPING
    dispatch.save()

    #updates the state of the Courier
    courier.state = Courier.STATE_SHIPPING
    courier.save()

    #updates the state of the package
    dispatch.package.state=Package.STATE_SHIPPING
    dispatch.package.save()

    return HttpResponse('accepted')

@api_permission_required
def complete(request):
    courier = Courier.objects.get(id=request.user.id)

    #get the corresponding Dispatch object
    dispatch = get_object_or_404(Dispatch, courier=courier, state=Dispatch.STATE_SHIPPING)
    dispatch.state=Dispatch.STATE_SHIPPED
    dispatch.save()

    #updates the state of the Courier
    courier.state = Courier.STATE_IDLE
    courier.save()

    #updates the state of the package
    dispatch.package.state=Package.STATE_SHIPPED
    dispatch.package.save()

    return HttpResponse('completed')

@api_permission_required
def fail(request):
    courier = Courier.objects.get(id=request.user.id)

    #get the corresponding Dispatch object
    dispatch = get_object_or_404(Dispatch, courier=courier, state=Dispatch.STATE_SHIPPING)
    dispatch.state=Dispatch.STATE_FAILED
    dispatch.save()

    #updates the state of the Courier
    courier.state = Courier.STATE_IDLE
    courier.save()

    #updates the state of the package
    dispatch.package.state=Package.STATE_FAILED
    dispatch.package.save()

    return HttpResponse('failed')

@csrf_exempt
@api_permission_required
def loc_update(request):
    if not request.method=='POST':
        return HttpResponseBadRequest("post required")
    lat = request.POST.get('lat' or None)
    lng = request.POST.get('lng' or None)
    if not (lat and lng):
        return HttpResponseBadRequest("invalid or missing parameters")

    courier = Courier.objects.get(id=request.user.id)

    courier.lat = lat
    courier.lng = lng
    courier.last_pos_update = datetime.now()
    courier.save()

    logger = logging.getLogger('location_logger')
    logger.info("%s: %s, %s @ %s" % (courier,lat,lng,courier.last_pos_update.isoformat()))

    return HttpResponse('location updated')

@csrf_exempt
@api_permission_required
def c2dmkey_update(request):
    if not request.method=='POST':
        return HttpResponseBadRequest("post required")
    registration_id = request.POST.get('registration_id')
    if not registration_id:
        return HttpResponseBadRequest("invalid or missing parameters")

    courier = Courier.objects.get(id=request.user.id)
    courier.c2dmkey = registration_id
    courier.save()

    logger = logging.getLogger('c2dm_logger')
    logger.info("%s: %s @ %s" % (courier,registration_id,datetime.now()))

    return HttpResponse('c2dm key updated')

