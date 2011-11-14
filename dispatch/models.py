from django.db import models
from django.contrib.auth.models import User
# Create your models here.
class ETModelBase(models.Model):
    date_created = models.DateTimeField(auto_now_add = True)
    date_modified = models.DateTimeField(auto_now = True)
    class Meta:
        abstract = True

class Courier(ETModelBase, User):
    STATE_ENUM = (
        (1, 'IDLE'),
        (2, 'STANDING_BY'),
        (3, 'PENDING'),
        (4, 'SHIPPING'),
    )
    state = models.IntegerField(choices = STATE_ENUM, default = 1)

    def __unicode__(self):
        return u'%s' % self.username

class Package(ETModelBase):
    STATE_ENUM = [
        (1, 'NEW'),
        (2, 'PENDING'),
        (3, 'SHIPPING'),
        (4, 'SHIPPED'),
        (5, 'FAILED'),
    ]
    client = models.ForeignKey(User)

    name = models.CharField(max_length=100)
    source = models.CharField(max_length=100)
    destination = models.CharField(max_length=100)

    state = models.IntegerField(choices = STATE_ENUM, default = 1)

    def __unicode__(self):
        return u'%s' % self.name

class Dispatch(ETModelBase):
    STATE_ENUM = [
        (1, 'PENDING'),
        (2, 'REJECTED'),
        (3, 'SHIPPING'),
        (4, 'SHIPPED'),
        (5, 'FAILED'),
    ]
    state = models.IntegerField(choices = STATE_ENUM, default = 1)
    courier = models.ForeignKey('Courier')
    package = models.ForeignKey('Package')

    def __unicode__(self):
        return u'%s -> %s' % (self.package, self.courier)

