from django.db import models

# Create your models here.
class ETModelBase(models.Model):
    date_created = models.DateTimeField(auto_now_add = True)
    date_modified = models.DateTimeField(auto_now = True)
    class Meta:
        abstract = True

class Courier(ETModelBase):
    STATE_ENUM = [
        ('IDLE', 1),
        ('STANDING_BY', 2),
        ('PENDING', 3),
        ('SHIPPING', 4), 
    ]
    state = models.IntegerField(choices = STATE_ENUM)
    name = models.CharField()

class Package(ETModelBase):
    STATE_ENUM = [
        ('NEW', 1),
        ('PENDING', 2),
        ('SHIPPING', 3),
        ('SHIPPED', 4),
        ('FAILED', 5),   
    ]
    name = models.CharField()
    source = models.CharField()
    destination = models.CharField()

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

