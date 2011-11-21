import logging
from datetime import datetime
import urllib
import urllib2
from django.core.cache import cache

def get_google_token():
    auth_token = cache.get('google_auth_token',None)
    if auth_token:
        return auth_token
    
    data = urllib.urlencode({
                    'Email':'ernestturtle@gmail.com',
                    'Passwd':'turtleadmin',
                    'accountType':'HOSTED_OR_GOOGLE',
                    'source':'Google-cURL-Example',
                    'service':'ac2dm',
            })
    request = urllib2.Request(
            url='https://www.google.com/accounts/ClientLogin',
            data = data)

    response = urllib2.urlopen(request)
    code = response.getcode()
    if response.getcode()==200:
        s = response.read()
        auth_token = s.split('Auth=')[1]
        auth_token = auth_token.strip()
        cache.set('google_auth_token', auth_token)
        return auth_token

    raise Exception('Google auth token error:%s' % code)

def push(courier, message):
    registration_id = courier.c2dmkey
    token = get_google_token()

    data = urllib.urlencode({
                    'registration_id':registration_id,
                    'data.message':message,
                    'collapse_key':1
            })

    request = urllib2.Request(
            url='https://android.apis.google.com/c2dm/send',
            data = data)
    request.add_header("Authorization","GoogleLogin auth=%s" % token)
    response = urllib2.urlopen(request)

    code = response.getcode()
    body = response.read()

    logger = logging.getLogger('c2dm_logger')
    logger.info("%s | %s | %s | %s" % (courier, datetime.now().isoformat(), code, body.strip()))
    return body

def run_dispatcher():
    #get un-assigned packages
    #assign them efficiently:
    #    1. create Dispatch(PENDING)
    #    2. notify cliend (c2dm)
    return None