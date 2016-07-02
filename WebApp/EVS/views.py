from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.models import User
from django.shortcuts import render
from .forms import SignUpForm, FeedbackForm, BusStopCoordinatesForm, MyForm
from .models import SignUp, Feedback, BusStopCoordinates, Edges, Vertices, Bus
from django.core import serializers
import itertools

#for sending message to mobile phone via www.way2sms.com

import urllib2
import cookielib
import sys

Stops = []
msg = []

# Create your views here.
def home(request):
	vertices = Vertices.objects.all()
	Vertex = []

	for node in vertices:
		Vertex.append(node.name)

	form = MyForm()

	context = {
		"form":form,
		"Vertex":Vertex,
	}

	return render(request, "index.html", context)

def route(request):
	return render(request, "route.html", {})

def route_search(request):
	bno = -1
	
	if request.method == "POST":
		bno = request.POST['busno']

	queryset = BusStopCoordinates.objects.all().filter(bus_number = bno);
	serialized_queryset = serializers.serialize('json', list(queryset));

	if not queryset:
		context = {
			'message' : 'No such bus number exist. Please try again with a valid bus number',
		}
		return render(request, "message.html", context)
	else:
		context = {
			"queryset": serialized_queryset,
			"data": queryset,
			"bus_number": bno,
		}
		return render(request, "map.html", context)

def feedback(request):
	if request.method == "POST":
		if request.user.is_active:
			
			form = FeedbackForm(request.POST)

			if form.is_valid():
				bus_number = request.POST['bus_number']
				feedback = request.POST['feedback']
				rating = request.POST['rating']
				first_name = request.user.first_name
				last_name = request.user.last_name

				#add info to our database
				Feedback(bus_number = bus_number, feedback = feedback, rating = rating, first_name = first_name, last_name = last_name).save()

				context = {
					'message' : 'Thank you for your feedback.'
				}
				return render(request, "message.html", context)
			else:
				form = SignUpForm()
		else:
			context = {
				'message' : 'You are not logged in. Please login to give your feedback.'
			}
			return render(request, "message.html", context)

	return render(request, "feedback.html", {})

def signup(request):
	if request.method == "POST":
		form = SignUpForm(request.POST)
			
		first_name = request.POST['first_name']
		last_name = request.POST['last_name']
		mobile_number = request.POST['mobile_number']
		password = request.POST['password']

		#check if user already exists
		if User.objects.filter(username=mobile_number).exists():
			context = {
				'message' : 'Mobile number already exists. Please register with a different mobile number.'
			}

			return render(request, "message.html", context)
		else:
			#add info to our database
			SignUp(first_name = first_name, last_name = last_name, mobile_number = mobile_number).save()

			#create user for django authentication
			user = User.objects.create_user(username = mobile_number, password = password, first_name = first_name, last_name = last_name)
			user.save()

	context = {
		'message' : 'You have been successfully registered.'
	}

	return render(request, "message.html", context)

def login_check(request):
	if request.method == "POST":
		username = request.POST['mobile_number']
		password = request.POST['password']

		user = authenticate(username = username, password = password)

		if user is not None:
			if user.is_active:				
				login(request, user)

				return render(request, "index.html", {})
		else:
			return render(request, "login-fail.html", {})

def log_out(request):
	logout(request)

	return render(request, "index.html", {})

class Vertex:
	def __init__(self, node):
		self.id = node
		self.adjacent = {}
		
	def __str__(self):
		return str([x.id for x in self.adjacent])
	
	def add_neighbor(self, neighbor, weight=0):
		self.adjacent[neighbor] = weight
	
	def get_connections(self):
		return self.adjacent.keys()  
	
	def get_id(self):
		return self.id
	
	def get_weight(self, neighbor):
		return self.adjacent[neighbor]

class Graph:
	def __init__(self):
		self.vert_dict = {}
		self.num_vertices = 0

	def __iter__(self):
		return iter(self.vert_dict.values())

	def add_vertex(self, node):
		self.num_vertices = self.num_vertices + 1
		new_vertex = Vertex(node)
		self.vert_dict[node] = new_vertex
		return new_vertex

	def get_vertex(self, n):
		if n in self.vert_dict:
			return self.vert_dict[n]
		else:
			return None

	def add_edge(self, frm, to, cost ):
		if frm not in self.vert_dict:
			self.add_vertex(frm)
		if to not in self.vert_dict:
			self.add_vertex(to)

		self.vert_dict[frm].add_neighbor(self.vert_dict[to], cost)
		self.vert_dict[to].add_neighbor(self.vert_dict[frm], cost)

	def get_vertices(self):
		return self.vert_dict.keys()

class NewPath:
	def __init__(self, path, weight, bus, checkpoints, Graph, Adults, Children):
		self.path = path
		self.weight = weight
		self.bus = bus
		self.checkpoints = checkpoints
		self.Graph = Graph
		self.Adults = Adults
		self.Children = Children
		if(len(bus)!=0):
			self.listA = zip()
		else:
			self.listA = []

	def zip(self):
		dict = {}
		i = 0
		listA = []

		sum2 = 0
		for buses in self.bus:
			list2 = []
			list2.append(buses.Number)
			list2.append(self.checkpoints[i])
			list2.append(self.checkpoints[i+1])
			temp = self.checkpoints[i]
			sum2 = 0
			j = 0

			while(temp!=self.checkpoints[i+1]):
				flag=0
				for v in self.Graph:
					if(v.get_id()==self.path[j]):
						for w in v.get_connections():
							if(w.get_id()==self.path[j+1]):
								sum2 = sum2 + v.get_weight(w)
								j = j+1
								temp = self.path[j]
								flag=1
								break
					if(flag==1):
						break

			list2.append(sum2)
			#print list2
			if(list2[3]>0 and list2[3]<=4):
				list2.append(5)
				list2.append(3)
			elif(list2[3]>4 and list2[3]<=10):
				list2.append(10)
				list2.append(5)
			elif(list2[3]>10):
				list2.append(15)
				list2.append(8)

			listA.append(list2)
			i = i + 1
		sum = 0
		sum2=0
		for listitem in listA:
			sum = sum+listitem[4]
			sum2 = sum2+listitem[5]
		listA.append(sum*int(self.Adults)+sum2*int(self.Children))
		#print listA
		return listA

def find_all_paths(graph, start, end, path=[]):
	path = path + [start]
	if start == end:
		return [path]
	if not graph.has_key(start):
	    return []
	paths = []
	for node in graph[start]:
		if node not in path:
			newpaths = find_all_paths(graph, node, end, path)
			for newpath in newpaths:
				paths.append(newpath)
	return paths

def FindBusCombinations(Path,Buses,BusCombination,Stops):
	if(len(Path)==1):
		# print 1
		# print BusCombination
		return BusCombination
	else:
		# print BusCombination
		List = BusCombination[0::]
		k = 1
		Busk =  Buses[0]
		BusDB = []
		k = 0
		for Bus2 in Buses:
			temp = Bus2.Path.split(",")
			i=0
			BusDB = []
			flag=0
			# print Path[0],Path[1]
			for i in range(0,len(temp)-1):
				# print "Temps",temp[i],temp[i+1]
				if(temp[i]==Path[0] and temp[i+1]==Path[1]):
					flag=1
					j=i
					# print temp[i],temp[i+1],Bus2
					# print Path,Bus2.Number
					break
					# BusDB.append(Bus2.Number)
			i=0
			# print flag
			# print "yolo"
			if(flag==1):
				while(i<len(Path) and j<len(temp) and temp[j] == Path[i]):
					i = i+1
					j = j+1
				# print i
				if(i>k):
					k = i
					Busk = Bus2
					# print k,Busk
		# print "k",k
		if k<1:
			# print 2
			return []
		RemStations = Path[k-1::]
		List.append(Busk)
		# print "YOLO", RemStations
		# print 3
		# print List
		# print RemStations
		# print List,RemStations
		Stops.append(RemStations[0])
		return FindBusCombinations(RemStations,Buses,List,Stops)

def ticket_search(request):
	g = Graph()
	Vertex = Vertices.objects.all()
	Buses = Bus.objects.all()
	
	for node in Vertex:
		g.add_vertex(node.name)

	Edge = Edges.objects.all()
	
	for road in Edge:
		g.add_edge(road.Vertex1,road.Vertex2,road.Weight)
	
	dist = []
	DICT = {}
	
	for v in g:
		x = g.vert_dict[v.get_id()]
		DICT[v.id] = eval(str(x))
	
	PATHS = []

	if request.method == 'POST':
		if request.user.is_active:
			form = MyForm(request.POST)
			A = request.POST.get('dropdown')
			B = request.POST.get('dropdown2')
			C = request.POST.get('Adult')
			D = request.POST.get('Children')
			
			msg.append(A)
			msg.append(B)
			msg.append(C)
			msg.append(D)

			print msg[0]
			# print B
			x = find_all_paths(DICT,A,B)
			if(D==None):
				D = 0

			i = 0
			PATHS = []
			BusDatabase =[]
			
			for path in x:
				Stops = []
				sum = 0
				dest = path[0]
				for i in range (1,len(path)):
					init = dest
					dest = path[i]
					for v in g:
						if(v.get_id()==init):
							for w in v.get_connections():
								if(w.get_id()==dest):
									sum = sum + v.get_weight(w)

				BusDatabase = []
				# for Bus2 in Buses:
				# 	temp = Bus2.Path.split(",")
				# 	i=0
				# 	BusDB = []
				# 	if(temp == path):
				# 		BusDB.append(Bus2.Number)
				# 		BusDatabase.append(BusDB)
				BusDB = []
				if(len(path)>1):
					BusDatabase = []
					Busk = Buses[0]
					k = 0
					for Bus2 in Buses:
						# print Bus2.Number
						temp = Bus2.Path.split(",")
						i=0
						BusDB = []
						flag=0
						# print temp
						for i in range(0,len(temp)-1):
							# print temp
							# print i
							if(temp[i]==path[0] and temp[i+1]==path[1]):
								flag=1
								# print path,Bus2.Number
								j=i
								break
								# print path,Bus2.Number
								
								# BusDB.append(Bus2.Number)
						# print "i",i,path[0],path[1],flag
						i=0

						if(flag==1):
							while(i<len(path) and j<len(temp) and temp[j] == path[i]):
								i = i+1
								j = j+1
							if(i>=k):
								k = i
								Busk = Bus2
								# print k
					# print Busk,flag
					Stops.append(path[0])
					Stops.append(path[k-1])
					RemStations = path[k-1::]
					# print RemStations
					BusDB.append(Busk)
					BusComb = []
					# print path,"BUSKY: " + Busk.Number, k
					BusDB =  BusDB + FindBusCombinations(RemStations,Buses,BusComb,Stops)
					#print "Stops",Stops
					# print BusDB
				# print "BUS",BusDB 
				flag2=0
				flag3=0
				if(BusDB!=[]):
					temporaryArray = BusDB[-1].Path.split(",")
					for pathe in temporaryArray:
						if(pathe==Stops[-1]):
							flag3=1
						if(pathe == path[-1] and flag3==1):
							flag2=1
				
				if(BusDB!=[] and flag2==1):
					BusDatabase = BusDB
					# print BusDatabase
				else:
					BusDatabase = []
					# print BusDatabase
				# print BusDatabase
				# print Stops
				PATHS.append(NewPath(path,sum,BusDatabase,Stops,g,C,D))

				context = {
					"A": A,
					"B": B,
					"Method2":"POST",
					"Paths":PATHS,
					"Adults":C,
					"Children":D,
				}

			return render(request, "ticket_search.html", context)

		else:
			context = {
				'message' : 'You are not logged in. Please login to buy tickets.'
			}
			return render(request, "message.html", context)		

def payment(request):
	username = "9868058344"
	passwd = "123456"
	message = "Your tickets for " + msg[2] + " adult passenger(s) and " + msg[3] + " children passenger(s) from " + msg[0] + " to " + msg[1] + " has been booked."
	number = request.user.username
	 
	#Logging into the SMS Site
	url = 'http://site24.way2sms.com/Login1.action?'
	data = 'username='+username+'&password='+passwd+'&Submit=Sign+in'
	 
	#For Cookies:
	cj = cookielib.CookieJar()
	opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cj))
	 
	# Adding Header detail:
	opener.addheaders = [('User-Agent','Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.120 Safari/537.36')]
	 
	try:
	    usock = opener.open(url, data)
	except IOError:
	    print "Error while logging in."
	    sys.exit(1)
	 
	 
	jession_id = str(cj).split('~')[1].split(' ')[0]
	send_sms_url = 'http://site24.way2sms.com/smstoss.action?'
	send_sms_data = 'ssaction=ss&Token='+jession_id+'&mobile='+number+'&message='+message+'&msgLen=136'
	opener.addheaders = [('Referer', 'http://site25.way2sms.com/sendSMS?Token='+jession_id)]
	 
	try:
	    sms_sent_page = opener.open(send_sms_url,send_sms_data)
	except IOError:
	    print "Error while sending message"
	    sys.exit(1)

	context = {
		'message' : 'Payment successful. Your tickets have been booked. You will receive confirmation on your registered mobile number.'
	}

	return render(request, "message.html", context)

def safety(request):
	return render(request, "safety.html", {})

def safety_search(request):
	bno = -1
	
	if request.method == "POST":
		bno = request.POST['busno']

	queryset = BusStopCoordinates.objects.all().filter(bus_number = bno)

	if not queryset:
		context = {
			'message' : 'No such bus number exist. Please try again with a valid bus number',
		}
		return render(request, "message.html", context)
	else:
		search_queryset_use = Feedback.objects.all().filter(bus_number = bno).values('rating')
		search_queryset_pass = Feedback.objects.all().filter(bus_number = bno)
		if not search_queryset_use:
			context = {
				'message' : 'Sorry, no feedback for bus number ' + bno + " exists",
			}
			return render(request, "message.html", context)

		rating = 0
		count = 0

		for i in search_queryset_use:
			rating = rating + i['rating']
			count = count + 1

		rating = rating/count

		context = {
			"queryset": search_queryset_pass,
			"bus_number": bno,
			"rating": rating,
		}
		return render(request, "safety_search.html", context)