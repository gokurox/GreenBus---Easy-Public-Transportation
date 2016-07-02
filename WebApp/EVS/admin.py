from django.contrib import admin

# Register your models here.
from .forms import SignUpForm, FeedbackForm, BusStopCoordinatesForm, BusForm, EdgesForm, VerticesForm
from .models import SignUp, Feedback, BusStopCoordinates, Bus, Edges, Vertices

class SignUpAdmin(admin.ModelAdmin):
	list_display = ["first_name","last_name","mobile_number","timestamp","updated"]
	form = SignUpForm

admin.site.register(SignUp, SignUpAdmin)

class FeedbackAdmin(admin.ModelAdmin):
	list_display = ["bus_number","feedback","rating","first_name","last_name","timestamp","updated"]
	form = FeedbackForm

admin.site.register(Feedback, FeedbackAdmin)

class BusStopCoordinatesAdmin(admin.ModelAdmin):
	list_display = ["marker","bus_number","bus_stop_name","longitude","latitude"]
	form = BusStopCoordinatesForm

admin.site.register(BusStopCoordinates, BusStopCoordinatesAdmin)

class BusAdmin(admin.ModelAdmin):
	list_display = ["Number","Path"]
	form = BusForm

admin.site.register(Bus, BusAdmin)

class EdgesAdmin(admin.ModelAdmin):
	list_display = ["Vertex1", "Vertex2", "Weight"]
	form = EdgesForm

admin.site.register(Edges, EdgesAdmin)

class VerticesAdmin(admin.ModelAdmin):
	list_display = ["name"]
	form = VerticesForm

admin.site.register(Vertices, VerticesAdmin)