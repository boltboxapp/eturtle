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
- **Returns**: `200, 401`

Only `couriers` can log in with this method (users in courier group with api access
permission)


::

  curl -c cookies.txt -d "username=roka&password=roka" http://localhost:8000/api/login/ --verbose

Send requests
^^^^^^^^^^^^^
You can make regular requests with the cookie (sessionid) received on login.

::

  curl -b cookies.txt http://localhost:8000/api/complete/ --verbose


List of functions
-----------------
======== =========================================================== ======================
name     description                                                 url
======== =========================================================== ======================
Login    Logs in the courier                                         `/api/login/`
Check in Sets Courier state to STANDING_BY                           `/api/check_in/`
Location Updates the position of the courier                         `/api/loc_update/`
C2DM key Updates the c2dm registration key                           `/api/c2dmkey_update/`
Leave    Sets Courier state to IDLE, rejects current PENDING package `/api/leave/`
Get      Gets the details of the current dispatched PENDING package  `/api/get/`
Accept   Accpets current PENDING package                             `/api/accept/`
Decline  Declines current PENDING package                            `/api/decline/`
Complete Sets package to SHIPPED, Courier to IDLE                    `/api/complete/`
Fail     Sets package to FAILED, Courier to IDLE                     `/api/fail/`
======== =========================================================== ======================

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

Location
^^^^^^^^

Location update of the courier.

- **Method**: `POST`
- **URL**: `/api/loc_update/`
- **Parameters**: `lat,lng`
- **Returns**: `200`

Example:

::

  curl -b cookies.txt -d "lat=47.47307989999999&lng=19.053034199999956" http://localhost:8000/api/loc_update/ --verbose

C2DM key
^^^^^^^^

Updates the c2dm registration key in the database. It is esseintial for using android
push service.

- **Method**: `POST`
- **URL**: `/api/c2dmkey_update/`
- **Parameters**: `registration_id`
- **Returns**: `200`

Example:

::

  curl -b cookies.txt -d "registration_id=asdasd2" http://localhost:8000/api/c2dmkey_update/ --verbosee

Leave
^^^^^

Sets the state of logged in user to IDLE.
Also rejects the current package if exists.

- **Method**: `GET`
- **URL**: `/api/leave/`
- **Parameters**: `none`
- **Returns**: `200`

Example:

::

  curl -b cookies.txt http://localhost:8000/api/leave/ --verbose

Get
^^^

Gets the details of the current dispatched package. First it tries to get the `Dispatch`
instace of the logged in user. There should be only one with `PENDING` state. After that,
gets the `Package` object according to the `Dispatch`,
and dumps out the details.

- **Method**: `GET`
- **URL**: `/api/get/`
- **Parameters**: `none`
- **Returns**: `200 + json data on succes, 404 if no package dispatched`

Example:

::

  curl -b cookies.txt http://localhost:8000/api/get/ --verbose | python -mjson.tool

  {
    "date_created": "2011-11-25T20:46:22.608605", 
    "destination": "1116 Budapest, S\\u00e1fr\\u00e1ny St 42, Hungary", 
    "dst_lat": "47.4459724", 
    "dst_lng": "19.034110300000066", 
    "name": "Genyo", 
    "source": "1091 Budapest, \\u00dcll\\u0151i Way 111, Hungary", 
    "src_lat": "47.4807631", 
    "src_lng": "19.08428409999999"
  }

The same format is used in the c2dm push message.

Accept
^^^^^^

Accepts the current dispatched package. Updates the state of the `Package`, `Dispatch`,
 and `Courier` to `SHIPPING`

- **Method**: `GET`
- **URL**: `/api/accept/`
- **Parameters**: `none`
- **Returns**: `200 on succes, 404 if no package dispatched`

Example:

::

  curl -b cookies.txt http://localhost:8000/api/accept/ --verbose

Decline
^^^^^^^

Declines the current dispatched package. Updates the state of the `Package` and the
`Dispatch` to `REJECTED`, and updates the state of the `Courier` to `IDLE`.

- **Method**: `GET`
- **URL**: `/api/decline/`
- **Parameters**: `none`
- **Returns**: `200 on succes, 404 if no package dispatched`

Example:

::

  curl -b cookies.txt http://localhost:8000/api/decline/ --verbose

Complete
^^^^^^^^

Mark the current dispatched SHIPPING package as successfully SHIPPED.
Updates the state of the `Package` and the `Dispatch` to `SHIPPED` and the state of the
`Courier` to `IDLE`.

- **Method**: `GET`
- **URL**: `/api/complete/`
- **Parameters**: `none`
- **Returns**: `200 on succes, 404 if no package dispatched`

Example:

::

  curl -b cookies.txt http://localhost:8000/api/complete/ --verbose


Fail
^^^^

Mark the current dispatched SHIPPING package as FAILED.
Updates the state of the `Package` and the `Dispatch` to `FAILED` and the state of the
`Courier` to `IDLE`.

- **Method**: `GET`
- **URL**: `/api/fail/`
- **Parameters**: `none`
- **Returns**: `200 on succes, 404 if no package dispatched`

Example:

::

  curl -b cookies.txt http://localhost:8000/api/fail/ --verbose


