# Create your views here.
from django.contrib.auth import authenticate, login
from django.http import HttpResponse, HttpResponseServerError, HttpResponseForbidden, HttpResponseNotFound
from django.views.decorators.csrf import csrf_exempt, csrf_protect

@csrf_exempt
def loginview(request):
    username = request.POST['username']
    password = request.POST['password']
    print 'alma'
    print username, password
    user = authenticate(username=username, password=password)
    if user is not None:
        if user.is_active:
            login(request, user)
            return HttpResponse("Logged in")
        else:
            return HttpResponseForbidden('Account disabled')
    else:
        return HttpResponseForbidden('Invalid login')

def index(request):
	
    if request.user.is_authenticated():
        print 'auth'

    return HttpResponse("""
	<html>
	<body>

	<form method="POST" action="/dispatch/login/">
<input type="text" name="username">
<input type="text" name="password"> 
<input type="submit" > 
</form>

	</body>
	</html>



	""")