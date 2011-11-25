# Create your views here.
from django.contrib.auth import authenticate, login
from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.http import HttpResponse, HttpResponseServerError, HttpResponseForbidden, HttpResponseNotFound, HttpResponseRedirect, Http404
from django.shortcuts import get_object_or_404
from django.views.decorators.csrf import csrf_exempt, csrf_protect
from django.views.generic import ListView
from django.views.generic.base import TemplateView
from django.views.generic.detail import DetailView
from django.views.generic.edit import CreateView, UpdateView, DeleteView
from dispatch.forms import PackageForm, CourierForm
from dispatch.models import Package, Courier, Client
from dispatch.models import ETurtleGroup as Group
from utils import AdminOr404Mixin, WebLoginRequiredMixin
from registration.signals import user_registered
from django.dispatch import receiver
from dispatcher import run_dispatcher


#signal catch for user registration
from server.dispatch.dispatcher import push
from server.dispatch.forms import CourierEditForm

@receiver(user_registered)
def set_group_of_registered_user(sender, **kwargs):
    user = kwargs.get('user')
    user.groups.add(Group.client())

class PackageListView(WebLoginRequiredMixin, ListView):
    model = Package

    def get_queryset(self):
        queryset = super(PackageListView,self).get_queryset()

        if not self.request.user.has_perm('dispatch.eturtle_admin'):
            queryset = queryset.filter(client = self.request.user)

        return queryset

class PackageCreateView(WebLoginRequiredMixin, CreateView):
    form_class = PackageForm
    template_name = 'dispatch/package_create.html'

    def form_valid(self, form):
        self.package = form.save(commit=False)
        self.package.client = Client().from_user(self.request.user)
        self.package.save()
        return super(PackageCreateView, self).form_valid(form)

    def get_success_url(self):
        return reverse('package_detail', kwargs={'pk':self.object.pk})

class PackageDetailView(WebLoginRequiredMixin, DetailView):
    queryset = Package.objects.all()

    def get_queryset(self, queryset=None):
        queryset = super(PackageDetailView,self).get_queryset()

        if not self.request.user.has_perm('dispatch.eturtle_admin'):
            queryset = queryset.filter(client = self.request.user)

        return queryset

class CourierListView(AdminOr404Mixin, ListView):
    model = Courier

class CourierCreateView(WebLoginRequiredMixin, CreateView):
    form_class = CourierForm
    template_name = 'dispatch/courier_create.html'

    def form_valid(self, form):
        self.courier = form.save(commit=False)
        self.courier.set_password(form.cleaned_data.get('pw1'))
        self.courier.save()
        self.courier.groups.add(Group.courier())
        return super(CourierCreateView, self).form_valid(form)

    def get_success_url(self):
        return reverse('courier_list', kwargs={})

class ClientListView(AdminOr404Mixin, ListView):
    model = Client
    queryset = Client.objects.filter(groups=Group.client)

class ClientToggleView(AdminOr404Mixin, DeleteView):
    model = Client

    def toggle(self, request, *args, **kwargs):
        self.object = self.get_object()
        self.object.is_active=not self.object.is_active
        self.object.save()
        return HttpResponseRedirect(self.get_success_url())

    def get(self, *args, **kwargs):
        return self.toggle(*args, **kwargs)

    def post(self, *args, **kwargs):
        return self.toggle(*args, **kwargs)

    def get_success_url(self):
        return reverse('client_list')

class CourierToggleView(ClientToggleView):
    model = Courier

    def get_success_url(self):
        return reverse('courier_list')

class CourierUpdateView(AdminOr404Mixin, UpdateView):
    model = Courier
    form_class = CourierEditForm

    def get_success_url(self):
        return reverse('courier_list')

class ProfileView(WebLoginRequiredMixin, DetailView):
    def get_object(self, queryset=None):
        return self.request.user

@login_required
def push_test(request,pk,message):
    courier = get_object_or_404(Courier, pk=pk)
    x = push(courier, message)

    return HttpResponse('%s' % x)

class RunDispatcherView(AdminOr404Mixin, TemplateView):
    template_name = "dispatch/run.html"

    def get_context_data(self, **kwargs):
        return {'data': run_dispatcher()}