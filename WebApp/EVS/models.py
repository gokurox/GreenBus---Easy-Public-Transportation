from __future__ import unicode_literals
from django.utils.encoding import smart_unicode
from django.db import models

# Create your models here.
class SignUp(models.Model):
	first_name = models.CharField(max_length = 120, blank = False)
	last_name = models.CharField(max_length = 120, blank = False)	
	#maximum 120 characters allowed in full_name
	#blank = False means that form field field is mandatory
	mobile_number = models.CharField(max_length = 10, blank = False, unique = True)
	timestamp = models.DateTimeField(auto_now_add = True, auto_now = False)
	#auto_now_add = True and auto_now = False means timestamp will be added only once when the
	#model is created
	updated = models.DateTimeField(auto_now_add = False, auto_now = True)
	#auto_now_add = False and auto_now = True means timestamp will be added everytime the model
	#is updated

class Feedback(models.Model):
	bus_number = models.CharField(max_length = 10, blank = False)
	feedback = models.CharField(max_length = 1024, blank = False)
	rating = models.DecimalField(max_digits=2, decimal_places=0, null = True)
	first_name = models.CharField(max_length = 120, blank = False, null = True)
	last_name = models.CharField(max_length = 120, blank = False, null = True)
	timestamp = models.DateTimeField(auto_now_add = True, auto_now = False)
	#auto_now_add = True and auto_now = False means timestamp will be added only once when the
	#model is created
	updated = models.DateTimeField(auto_now_add = False, auto_now = True)
	#auto_now_add = False and auto_now = True means timestamp will be added everytime the model
	#is update

class BusStopCoordinates(models.Model):
	marker = models.CharField(max_length = 2, blank = True)
	bus_number = models.CharField(max_length = 10, blank = False)
	bus_stop_name = models.CharField(max_length = 50, blank = False)
	latitude = models.DecimalField(max_digits=19, decimal_places=10, blank = False)
	longitude = models.DecimalField(max_digits=19, decimal_places=10, blank = False)

class Edges(models.Model):
	Vertex1 = models.CharField(max_length=150)
	Vertex2 = models.CharField(max_length=150)
	Weight = models.BigIntegerField()
	
class Vertices(models.Model):
	name = models.CharField(max_length=150)

class Bus(models.Model):
	Number = models.CharField(max_length=150)
	Path = models.TextField(max_length=400000)
	def __unicode__(self):
		return smart_unicode(self.Number)
	def Path_as_list(self):
		return self.Path.split(",")