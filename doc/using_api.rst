==========
Client API
==========
Accessing API
-------------

Logging in
^^^^^^^^^^
First, you need to log in.

- **Method**: `POST`
- **URL**: `/api/login/`
- **Parameters**: `username,password`

Only `couriers` can log in with this method (users in courier group with api access permission)


::

  curl -c cookies.txt -d "username=roka&password=alma" http://localhost:9000/api/login/ --verbose

Send requests
^^^^^^^^^^^^^
You can make regular requests with the cookie (sessionid) received on login.

::

  curl -b cookies.txt http://localhost:9000/api/complete/ --verbose


List of functions
-----------------
======== =========================================================== ================
name     description                                                 url
======== =========================================================== ================
Login    Logs in the courier                                         `/api/login/`
Check in Sets Courier state to STANDING_BY                           `/api/check_in/`
Leave    Sets Courier state to IDLE, rejects current PENDING package `/api/leave/`
Get      Gets the details of the current dispatched PENDING package  `/api/get/`
Accept   Accpets current PENDING package                             `/api/accept/`
Decline  Declines current PENDING package                            `/api/decline/`
Complete Sets package to SHIPPED, Courier to IDLE                    `/api/complete/`
Fail     Sets package to FAILED, Courier to IDLE                     `/api/fail/`
======== =========================================================== ================

Functions
---------
Check in
^^^^^^^^

Sets the state of logged in user to STANDING_BY.

- **Method**: `GET`
- **URL**: `/api/check_in/`
- **Parameters**: `none`
- **Returns**: `200`

Example:

::

  curl -b cookies.txt http://localhost:8000/api/check_in/ --verbose

Leave
^^^^^

Sets the state of logged in user to IDLE. 

Also rejects the current package if exists (NOT IMPLEMENTED YET)

- **Method**: `GET`
- **URL**: `/api/leave/`
- **Parameters**: `none`
- **Returns**: `200`

Example:

::

  curl -b cookies.txt http://localhost:8000/api/leave/ --verbose

Get
^^^

Gets the details of the current dispatched package. First it tries to get the `Dispatch` instace of the logged in user. 
There should be only one with `PENDING` state. After that, gets the `Package` object according to the `Dispatch`, 
and dumps out the details.

- **Method**: `GET`
- **URL**: `/api/get/`
- **Parameters**: `none`
- **Returns**: `200 + json data on succes, 404 if no package dispatched`

Example:

::

  curl -b cookies.txt http://localhost:8000/api/get/ --verbose | python -mjson.tool

  {
    "fields": {
        "client": 1, 
        "date_created": "2011-11-19 09:43:49", 
        "date_modified": "2011-11-19 09:43:49", 
        "destination": "1071 Budapest, Damjanich St 21, Hungary", 
        "dst_lat": "47.50721497157391", 
        "dst_lng": "19.076939363476527", 
        "name": "Mákosguba", 
        "source": "1094 Budapest, Liliom St 58-62, Hungary", 
        "src_lat": "47.48394254622929", 
        "src_lng": "19.072144421998587", 
        "state": 1
    }, 
    "model": "dispatch.package", 
    "pk": 6
  }


Accept
^^^^^^

Accepts the current dispatched package. Updates the state of the `Package` and the `Dispatch` to `SHIPPING`

- **Method**: `GET`
- **URL**: `/api/accept/`
- **Parameters**: `none`
- **Returns**: `200 on succes, 404 if no package dispatched`

Example:

::

  curl -b cookies.txt http://localhost:8000/api/accept/ --verbose

Decline
^^^^^^^

NOT IMPLEMENTED YET

Complete
^^^^^^^^

NOT IMPLEMENTED YET

Fail
^^^^

NOT IMPLEMENTED YET
