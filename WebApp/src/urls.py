"""src URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.9/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.conf.urls import url, include
    2. Add a URL to urlpatterns:  url(r'^blog/', include('blog.urls'))
"""
from django.conf.urls import url
from django.contrib import admin
from EVS.views import home
from EVS.views import route
from EVS.views import feedback
from EVS.views import signup
from EVS.views import login_check
from EVS.views import log_out
from EVS.views import route_search
from EVS.views import ticket_search
from EVS.views import payment
from EVS.views import safety
from EVS.views import safety_search

urlpatterns = [
	url(r'^$', home, name='home'),
	url(r'^route/$', route, name='route'),
    url(r'^route_search/$', route_search, name='route_search'),
	url(r'^feedback/$', feedback, name='feedback'),
    url(r'^signup/$', signup, name='signup'),
    url(r'^login_check/$', login_check, name='login_check'),
    url(r'^log_out/$', log_out, name='log_out'),
    url(r'^ticket_search/$', ticket_search, name='ticket_search'),
    url(r'^payment/$', payment, name='payment'),
    url(r'^safety/$', safety, name='safety'),
    url(r'^safety_search/$', safety_search, name='safety_search'),
    url(r'^admin/', admin.site.urls),
]
