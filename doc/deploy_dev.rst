Deployment (dev)
----------------
Requirements:

- python 2.6
- pip
- virtualenv

----

1. Checkout the code:

`git clone git@github.com:lepilepi/eturtle.git`

----

2. Create virtual environment:

`cd eturtle`

`virtualenv virtual_eturtle --no-site-package`

----

3. Activate virtualenv and install required python libraries:

`source virtual_eturtle/bin/activate`

`pip install -r requirements.py`

If everything is successful, the last row will be:

`Cleaning up...`


----

4. Set up database and initial data:

`cd server`

`python manage.py syncdb --noinput`

`python manage.py loaddata fixtures/*.json`

`python manage.py initialpermissions`

----

5. Run django devserver on local machine:

`python manage.py runserver 0.0.0.0:8000`

----

6. After these steps you will be able to open http://localhost:8000 in your favorite browser.

Here are some user:pass pairs for testing:

Admin users:

- alma:alma

Clients:

- bela:bela
- elek:elek

Couriers:

- roka:roka
- teki:teki
 

