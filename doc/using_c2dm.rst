Authenticate with Google
------------------------
First, you have to obtain an authetication token form Google:

::

  curl https://www.google.com/accounts/ClientLogin -d Email=**EMAIL** -d Passwd=**PASSWORD** -d accountType=HOSTED_OR_GOOGLE -d source=Google-cURL-Example -d service=ac2dm

The respone will be like this:
::

  SID=DQAAAM0AAABmg_E91Wj9Fh9LUulIIk-S0s--   vVdhx2gRHByCfd3kf28LrWvvDJO2UMDrjruag4JpDlZ54IkwURLpiJLLE_fW0VZLtvsAExIQtbab24rq09rWmBEZRSAnUjceQI8oZDOXJySbYKmP0CYPLmuFobzJbTDIdeEkFCymwoxZ7HbOpLOHLoalpRBUQCJ_0MTil9R_AemkWkf-jbLQDNL1Xdbjxlz6Smk-U4H__lg13enUyQgJYGXD4qhrLYJ1DgjOHajLQjxGNw_G5gcOECYunj8z
  LSID=DQAAAM4AAABR_RcciS4YtglfKTZCCpjBkNmEyqHzgVdws7upm4ZpM81_yRbpXlgCkwnaHYx0m9yr1mkW7_FtSZb5PT-wwigUR82kRj5cwz3jn40Dzh95d63HsR5DXGJAzDeAsKPhH-XKleNFgMlhnuXvb6G23Co_NpZyfyz13nvanHiAppCtJK23Jsi1r07_Nf0PuHRJe6lWd5MP4L5ymnsLR69l7M3G1oYrSyF4oWuSIHfKQRJJL0mHZss3M4IgDfGHPtqTS42CMSSH4i2lHX7KjpQCxBkc
  Auth=DQAAAM4AAAD3QULH5SziAGM6RALrE8ydTQhk40nhvXSBzo-ZZLDD8rmjOerLvMniEZGqbXWTjl22o1niJvqK-ghQ4aFfk1z7zq7cMGSWu2mKFgcWduI6FCh665yGdRUzS44OuFMwnEBXbVsWTMo64Zgsgq1YHN85As4ryrPbLx8XcFlacrbg4WAm2q1TF6UpmmgL2Oj-bUHCy_drxkM1HLhodQ3BXk_B3UcOmvK51RP51w6wL2S10eZ_8jUPGkDVFhtaeorXR0dN5Df134VW5fbYlBh5Y9_8

Your auth key is after "`Auth=`"

Sending message
---------------
Now you are authenticated with Google.
When the phone registrates itself at the c2dm service, it receives a `registration_id`.

To send a message, you will need:

- your auth token (above) (**AUTH_KEY**)
- phone registration id (**REGISTRATION_ID**)

The sendmessage api call:
::

  curl --header "Authorization: GoogleLogin auth=**AUTH_KEY**" "https://android.apis.google.com/c2dm/send" -d registration_id=**REGISTRATION_ID** -d "data.message=hellohallo" -d collapse_key=something -k

