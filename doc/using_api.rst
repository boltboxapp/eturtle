Client api
----------
Login
^^^^^
First, you need to log in.

- **Method**: `POST`
- **URL**: `/api/login/`
- **Parameters**: `username,password`

Only `couriers` can log in with this method (users in courier group with api access permission)


::

  curl -c cookies.txt -d "username=roka&password=alma" http://localhost:9000/api/login/ --verbose

Requests
^^^^^^^^
You can make regular requests with the cookie (sessionid) received on login.

::

  curl -b cookies.txt http://localhost:9000/api/complete/ --verbose

Available URLs
^^^^^^^^^^^^^^

======== ================
name     url
======== ================
Check in `/api/check_in/`
Leave    `/api/leave/`
Accept   `/api/accept/`
Decline  `/api/decline/`
Complete `/api/complete/`
Fail     `/api/fail/`
======== ================

