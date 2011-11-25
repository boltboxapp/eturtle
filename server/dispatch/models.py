from django.core.exceptions import ImproperlyConfigured
from django.db import models
from django.contrib.auth.models import User, Group
import json

class ETurtleGroup(Group):
    GROUP_ADMIN = 'eturtle_admin'
    GROUP_COURIER = 'courier'
    GROUP_CLIENT = 'client'

    @classmethod
    def get_group_or_raise(cls,group_name):
        try:
            return cls.objects.get(name=group_name)
        except cls.DoesNotExist:
            raise ImproperlyConfigured('You must create the base user groups:(%s,%s,%s)' % (cls.GROUP_ADMIN, cls.GROUP_COURIER, cls.GROUP_CLIENT))

    @classmethod
    def admin(cls):
        return cls.get_group_or_raise(cls.GROUP_ADMIN)

    @classmethod
    def courier(cls):
        return cls.get_group_or_raise(cls.GROUP_COURIER)

    @classmethod
    def client(cls):
        return cls.get_group_or_raise(cls.GROUP_CLIENT)

    class Meta:
        proxy = True

class ETModelBase(models.Model):
    date_created = models.DateTimeField(auto_now_add = True)
    date_modified = models.DateTimeField(auto_now = True)
    class Meta:
        abstract = True

class Courier(ETModelBase, User):
    STATE_IDLE = 1
    STATE_STANDING_BY = 2
    STATE_PENDING = 3
    STATE_SHIPPING = 4

    STATE_CHOICES = (
        (1, 'IDLE'),
        (2, 'STANDING_BY'),
        (3, 'PENDING'),
        (4, 'SHIPPING'),
    )
    state = models.IntegerField(choices = STATE_CHOICES, default = 1)

    lat = models.CharField(max_length=20, default='', blank=True)
    lng = models.CharField(max_length=20, default='', blank=True)
    last_pos_update = models.DateTimeField(null=True, blank=True)

    c2dmkey = models.CharField(max_length='256', null=True, blank=True)

    def __unicode__(self):
        return u'%s' % self.username

    class Meta:
        permissions = (
            ("api_access", "Can login via api and access api functions."),
            ("web_access", "Can login via browser and access the web application."),
        )

class ClientManager(models.Manager):
    def get_query_set(self):
        return super(ClientManager, self).get_query_set().filter(groups__name=ETurtleGroup.GROUP_CLIENT)

class Client(User):
    objects = ClientManager()

    def from_user(self, user):
        self.__dict__ = user.__dict__
        return self

    class Meta:
        proxy = True

class Package(ETModelBase):
    STATE_NEW = 1
    STATE_PENDING = 2
    STATE_SHIPPING = 3
    STATE_SHIPPED = 4
    STATE_FAILED = 5

    STATE_ENUM = [
        (1, 'NEW'),
        (2, 'PENDING'),
        (3, 'SHIPPING'),
        (4, 'SHIPPED'),
        (5, 'FAILED'),
    ]
    client = models.ForeignKey(Client)

    name = models.CharField(max_length=100)

    source = models.CharField(max_length=100)
    src_lat = models.CharField(max_length=20)
    src_lng = models.CharField(max_length=20)

    destination = models.CharField(max_length=100)
    dst_lat = models.CharField(max_length=20)
    dst_lng = models.CharField(max_length=20)

    state = models.IntegerField(choices = STATE_ENUM, default = 1)

    def serialize(self):
        data={'name':self.name,
               'source':self.source,
               'src_lat':self.src_lat,
               'src_lng':self.src_lng,
               'destination':self.destination,
               'dst_lat':self.dst_lat,
               'dst_lng':self.dst_lng,
               'date_created':self.date_created.isoformat()}
        return json.dumps(data)

    def __unicode__(self):
        return u'%s' % self.name

class Dispatch(ETModelBase):
    STATE_PENDING = 1
    STATE_REJECTED = 2
    STATE_SHIPPING = 3
    STATE_SHIPPED = 4
    STATE_FAILED = 5

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

    def save(self, force_insert=False, force_update=False, using=None):

        if not self.id and Dispatch.objects.filter(courier=self.courier, state=1).count():
            raise Exception("This user already has a pending dispatch.")

        super(Dispatch,self).save(force_insert=force_insert,
                                  force_update=force_update,
                                  using=using)



    class Meta:
        ordering = ('-date_created',)

    def __unicode__(self):
        return u'%s -> %s' % (self.package, self.courier)

