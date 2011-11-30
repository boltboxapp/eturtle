#!/usr/local/bin/python
# -*- coding: utf-8 -*-
from django.contrib.auth.models import User

from dispatch.models import Package, Courier
from django import forms

class PackageForm(forms.ModelForm):
    src_lat = forms.CharField(widget=forms.HiddenInput(), required=False)
    src_lng = forms.CharField(widget=forms.HiddenInput(), required=False)

    dst_lat = forms.CharField(widget=forms.HiddenInput(), required=False)
    dst_lng = forms.CharField(widget=forms.HiddenInput(), required=False)

    def clean(self):
        cleaned_data = self.cleaned_data

        if self.instance:
            if not getattr(self.instance, 'state', None)==Package.STATE_NEW:
                raise forms.ValidationError(u"A csomag már kiosztásra került, most már \
                nem módosítható. Kérjük lépjen kapcsolatba az ügyfélszolgálattal.")
        return cleaned_data
    
    class Meta:
        model = Package
        exclude = ('client','state')

class CourierForm(forms.ModelForm):
    pw1 = forms.CharField( widget=forms.PasswordInput, label=u'Jelszó' )
    pw2 = forms.CharField( widget=forms.PasswordInput, label=u'Jelszó ismét' )

    def clean(self):
        cleaned_data = self.cleaned_data
        pw1 = cleaned_data.get("pw1")
        pw2 = cleaned_data.get("pw2")

        if not pw1==pw2:
            raise forms.ValidationError(u"A két jelszónak meg kell egyeznie!")

        return cleaned_data

    def clean_username(self):
        username = self.cleaned_data.get('username')

        if User.objects.filter(username=username).count():
            raise forms.ValidationError(u"Már van ilyen nevű felhasználó!")

        return username

    class Meta:
        model = Courier
        fields = ('username','first_name','last_name','pw1','pw2')

class CourierEditForm(forms.ModelForm):
    class Meta:
        model = Courier
        fields = ('first_name','last_name','email')