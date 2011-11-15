#!/usr/local/bin/python
# -*- coding: utf-8 -*-

from dispatch.models import Package, Courier
from django import forms

class PackageForm(forms.ModelForm):
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

        # Always return the full collection of cleaned data.
        return cleaned_data

    class Meta:
        model = Courier
        fields = ('username','first_name','last_name','pw1','pw2')