from django import forms
from .models import SignUp
from .models import Feedback
from .models import BusStopCoordinates
from .models import Edges
from .models import Vertices
from .models import Bus

class SignUpForm(forms.ModelForm):
	class Meta:
		model = SignUp
		fields = ['first_name','last_name','mobile_number']

	first_name = forms.CharField(required = True)
	last_name = forms.CharField(required = True)
	mobile_number = forms.CharField(required = True)
	password = forms.CharField(required = True)

class FeedbackForm(forms.ModelForm):
	class Meta:
		model = Feedback
		fields = ['bus_number','feedback','rating']

	bus_number = forms.CharField(required = True)
	feedback = forms.CharField(widget = forms.Textarea, required = True)

class BusStopCoordinatesForm(forms.ModelForm):
	class Meta:
		model = BusStopCoordinates
		fields = ['marker','bus_stop_name','latitude','longitude']

	marker = forms.CharField(required = True)
	bus_number = forms.CharField(required = True)
	bus_stop_name = forms.CharField(required = True)
	latitude = forms.DecimalField(required = True)
	longitude = forms.DecimalField(required = True)

class EdgesForm(forms.ModelForm):
	class Meta:
		model = Edges
		fields = ['Vertex1','Vertex2','Weight']

	Vertex1 = forms.CharField()
	Vertex2 = forms.CharField()
	Weight = forms.IntegerField()

class VerticesForm(forms.ModelForm):
	class Meta:
		model = Vertices
		fields = ['name']

	name = forms.CharField()

class BusForm(forms.ModelForm):
	class Meta:
		model = Bus
		fields = ['Number','Path']

	Number = forms.CharField()
	Path = forms.CharField()

class MyForm(forms.Form): 
    InitialP = forms.CharField(max_length=20)
    FinalP = forms.CharField(max_length=20)