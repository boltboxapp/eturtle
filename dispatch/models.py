from django.db import models
from django.contrib.auth.models import User
# Create your models here.
class ETModelBase(models.Model):
    date_created = models.DateTimeField(auto_now_add = True)
    date_modified = models.DateTimeField(auto_now = True)
    class Meta:
        abstract = True

class Courier(ETModelBase, User):
    STATE_ENUM = [
        ('IDLE', 1),
        ('STANDING_BY', 2),
        ('PENDING', 3),
        ('SHIPPING', 4), 
    ]
    state = models.IntegerField(choices = STATE_ENUM)

class Package(ETModelBase):
    STATE_ENUM = [
        ('NEW', 1),
        ('PENDING', 2),
        ('SHIPPING', 3),
        ('SHIPPED', 4),
        ('FAILED', 5),   
    ]
    name = models.CharField(max_length=100)
    source = models.CharField(max_length=100)
    destination = models.CharField(max_length=100)

class Dispatch(ETModelBase):
    STATE_ENUM = [
        ('PENDING', 1),
        ('REJECTED', 2),
        ('SHIPPING', 3),
        ('SHIPPED', 4),
        ('FAILED', 5),   
    ]
    state = models.IntegerField(choices = STATE_ENUM)
    courier = models.ForeignKey('Courier')
    package = models.ForeignKey('Package')

