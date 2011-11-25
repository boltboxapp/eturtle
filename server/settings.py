# Django settings for eturtle project.

import os
PROJECT_DIR = os.path.dirname(os.path.abspath(__file__))

DEBUG = True
TEMPLATE_DEBUG = DEBUG

ADMINS = (
    # ('Your Name', 'your_email@example.com'),
)

MANAGERS = ADMINS

DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.sqlite3', # Add 'postgresql_psycopg2', 'postgresql', 'mysql', 'sqlite3' or 'oracle'.
        'NAME': 'turtledb',                      # Or path to database file if using sqlite3.
        'USER': '',                      # Not used with sqlite3.
        'PASSWORD': '',                  # Not used with sqlite3.
        'HOST': '',                      # Set to empty string for localhost. Not used with sqlite3.
        'PORT': '',                      # Set to empty string for default. Not used with sqlite3.
    }
}

# Local time zone for this installation. Choices can be found here:
# http://en.wikipedia.org/wiki/List_of_tz_zones_by_name
# although not all choices may be available on all operating systems.
# On Unix systems, a value of None will cause Django to use the same
# timezone as the operating system.
# If running in a Windows environment this must be set to the same as your
# system time zone.
TIME_ZONE = 'Europe/Budapest'

# Language code for this installation. All choices can be found here:
# http://www.i18nguy.com/unicode/language-identifiers.html
LANGUAGE_CODE = 'hu-HU'

SITE_ID = 1

# If you set this to False, Django will make some optimizations so as not
# to load the internationalization machinery.
USE_I18N = True

# If you set this to False, Django will not format dates, numbers and
# calendars according to the current locale
USE_L10N = True

MEDIA_ROOT = os.path.join(PROJECT_DIR, 'media/')
MEDIA_URL = '/media/'

STATIC_ROOT = os.path.join(PROJECT_DIR, 'static_deploy/')
STATIC_URL = '/static/'

ADMIN_MEDIA_PREFIX = '/static/admin/'

# Additional locations of static files
STATICFILES_DIRS = (
    os.path.join(PROJECT_DIR, 'static'),
)

# List of finder classes that know how to find static files in
# various locations.
STATICFILES_FINDERS = (
    'django.contrib.staticfiles.finders.FileSystemFinder',
    'django.contrib.staticfiles.finders.AppDirectoriesFinder',
#    'django.contrib.staticfiles.finders.DefaultStorageFinder',
)

# Make this unique, and don't share it with anybody.
SECRET_KEY = '0-2_+!a$-ww9y@2f*hbntg+m&d6#ylyc*0(qh796ijx$)oz3-@'

# List of callables that know how to import templates from various sources.
TEMPLATE_LOADERS = (
    'django.template.loaders.filesystem.Loader',
    'django.template.loaders.app_directories.Loader',
#     'django.template.loaders.eggs.Loader',
)

MIDDLEWARE_CLASSES = (
    'django.middleware.common.CommonMiddleware',
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.csrf.CsrfViewMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
)

ROOT_URLCONF = 'server.urls'

TEMPLATE_DIRS = (
    os.path.join(PROJECT_DIR, 'templates'),
)

INSTALLED_APPS = (
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.sites',
    'django.contrib.messages',
    'django.contrib.staticfiles',
    'django.contrib.admin',
    'django.contrib.admindocs',
    'dispatch',
    'tastypie',
    'registration',
)

# A sample logging configuration. The only tangible logging
# performed by this configuration is to send an email to
# the site admins on every HTTP 500 error.
# See http://docs.djangoproject.com/en/dev/topics/logging for
# more details on how to customize your logging configuration.
LOGGING = {
    'version': 1,
    'disable_existing_loggers': False,
    'handlers': {
        'mail_admins': {
            'level': 'ERROR',
            'class': 'django.utils.log.AdminEmailHandler'
        },
        'file_loc':{
            'level': 'INFO',
            'class': 'logging.FileHandler',
                'filename': 'location_updates.log',
        },
        'file_c2dm':{
            'level': 'INFO',
            'class': 'logging.FileHandler',
                'filename': 'c2dm_updates.log',
        },
        'file_dispatcher':{
            'level': 'INFO',
            'class': 'logging.FileHandler',
                'filename': 'dispatch.log',
        }
    },
    'loggers': {
        'location_logger': {
            'handlers':['file_loc'],
            'propagate': False,
            'level':'INFO',
        },
        'c2dm_logger': {
            'handlers':['file_c2dm'],
            'propagate': False,
            'level':'INFO',
        },
        'dispatch_logger': {
            'handlers':['file_dispatcher'],
            'propagate': False,
            'level':'INFO',
        },
        'django.request': {
            'handlers': ['mail_admins'],
            'level': 'ERROR',
            'propagate': True,
        },
    }
}

#registration
ACCOUNT_ACTIVATION_DAYS = 7

#redirect after login
LOGIN_REDIRECT_URL = '/'

#email (CONFIDENTAL, DO NOT REUSE)
EMAIL_HOST = 'post.symbler.net'
EMAIL_HOST_USER = 'rop.post'
EMAIL_HOST_PASSWORD = 'riprop'

#debug email
EMAIL_BACKEND = 'django.core.mail.backends.console.EmailBackend'

try:
    from settings_local import *
except Exception:
    pass