============================
ETurtle groups documentation
============================
Usage
-----
Instead of:

>>> some_user.add(some_group)

Use ETurtleGroup:

>>> from dispatch.models import ETurtleGroup as Group
>>> some_user.groups.add(Group.courier())


The Model
---------
ETurtleGroup is a django proxy call over django.auth's Group
It uses 3 hard-coded group names:

- GROUP_ADMIN = 'admin'
- GROUP_COURIER = 'courier'
- GROUP_CLIENT = 'client'

And the class has 3 classmethods:

- ETurtleGroup.admin()
- ETurtleGroup.courier()
- ETurtleGroup.client()

You can access the 3 default groups through these methods.
If one of the group does not exist, ImproperlyConfigured exception will be risen.

There is initial data for groups is included in: `fixtures/initial_groups.json <https://github.com/lepilepi/eturtle/blob/master/fixtures/initial_groups.json>`_
