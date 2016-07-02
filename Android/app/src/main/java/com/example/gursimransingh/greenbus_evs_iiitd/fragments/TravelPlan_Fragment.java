package com.example.gursimransingh.greenbus_evs_iiitd.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gursimransingh.greenbus_evs_iiitd.R;
import com.example.gursimransingh.greenbus_evs_iiitd.sqlite.DatabaseHelper;
import com.example.gursimransingh.greenbus_evs_iiitd.sqlite.dataholder.Bus_Coordinates;
import com.example.gursimransingh.greenbus_evs_iiitd.sqlite.dataholder.Edges;
import com.example.gursimransingh.greenbus_evs_iiitd.sqlite.dataholder.Vertice;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TravelPlan_Fragment extends Fragment implements OnMapReadyCallback {
    private EditText departureLocation, arrivalLocation;
    private Button search_btn, bookTicket;
    boolean plan;

    private GoogleMap mMap;
    MapView mapView;

    List<Vertex> nodes;
    List<Edge> edges;
    Graph graph;

    //string array to store bus no- 8A destination,latitude and longitude and distance as per shubham maheshwari's data
    Bus_Coordinates[] bus_coordinates;

    View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(getContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Plan Your Travel");
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_travel_plan, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        departureLocation = (EditText) getView().findViewById(R.id.input_departureloc);
        arrivalLocation = (EditText) getView().findViewById(R.id.input_arrivalloc);
        bookTicket = (Button) getView().findViewById(R.id.tp_bookTicket);
        search_btn = (Button) getView().findViewById(R.id.searchButton_travelplan);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchClicked();
            }
        });
        bookTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBookClicked();
            }
        });
        plan = false;

        mapView = (MapView) mView.findViewById(R.id.googleMap2);

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync (this);
        }

        DatabaseHelper db = DatabaseHelper.getInstance (getActivity());

        // Create Graph
        ArrayList<Vertice> vertex_names = db.getData_Vertice();
        ArrayList<Edges> edge_list = db.getData_Edges();

        nodes = new ArrayList<Vertex>();
        edges = new ArrayList<Edge>();

        for (int i = 0; i < vertex_names.size(); i++) {
            Vertex location = new Vertex (Integer.toString(i), vertex_names.get(i).vertex_name);
            nodes.add(location);
        }

        for (int i = 0; i < edge_list.size(); i++)
        {
            Edges temp = edge_list.get(i);
            addLane ("Edge_" + i, findByVertexName(nodes, temp.vertex_1), findByVertexName(nodes, temp.vertex_2), temp.weight);
        }

        graph = new Graph(nodes, edges);

    }

    private void onBookClicked() {
        if (plan)
        {
            departureLocation.setText(null);
            arrivalLocation.setText(null);
            mMap.clear();
            Toast.makeText (getActivity(), "Ticket has been Booked !", Toast.LENGTH_LONG).show();
        }
    }

    private int findByVertexName (List<Vertex> list, String name) {
        int i;
        for (i = 0; i < list.size(); i++) {
            if (list.get(i).getName().compareToIgnoreCase(name) == 0)
                return i;
        }
        return -1;
    }

    private int findByVertexName (ArrayList<Bus_Coordinates> list, String name) {
        int i;
        for (i = 0; i < list.size(); i++) {
            if (list.get(i).stop_name.compareToIgnoreCase(name) == 0)
                return i;
        }
        return -1;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //this latitude and longitude are the points from which map will start
        LatLng cp = new LatLng(28.564615, 77.280885);
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(cp, 10);   //here 10 is the zoom
        mMap.moveCamera(yourLocation);

        int count;

        if (bus_coordinates != null)
            count = bus_coordinates.length;
        else
            count = 0;

        //running the for loop for adding marker with title and different colors for each "bus"
        //for changing color just change HUE_RED to different colors
        for(int i=0; i<count; i++)
        {
            LatLng location = new LatLng (bus_coordinates[i].latitude, bus_coordinates[i].longitude);
            mMap.addMarker(new MarkerOptions().position(location).title(i + ". " + bus_coordinates[i].stop_name).snippet("BusNumber: " + bus_coordinates[i].bus_number).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
    }

    public void onSearchClicked () {
        String dep = departureLocation.getText().toString();
        String arr = arrivalLocation.getText().toString();
        boolean valid = true;
        plan = false;

        departureLocation.setError(null);
        arrivalLocation.setError(null);
        if (dep.isEmpty())
        {
            valid = false;
            departureLocation.setError("Empty");
        }
        if (arr.isEmpty())
        {
            valid = false;
            arrivalLocation.setError("Empty");
        }

        if (!valid)
            return;

        DatabaseHelper db = DatabaseHelper.getInstance(getActivity());
        ArrayList<Bus_Coordinates> data = db.getData_Bus_Coordinates();

        if (findByVertexName (nodes, dep) == -1)
        {
            valid = false;
            departureLocation.setError ("Cont find in vertices");
        }
        if (findByVertexName (nodes, arr) == -1)
        {
            valid = false;
            arrivalLocation.setError ("Cant find in vertices");
        }
        if (findByVertexName (data, dep) == -1)
        {
            valid = false;
            departureLocation.setError ("Cant find in bus_coordinates");
        }
        if (findByVertexName (data, arr) == -1)
        {
            valid = false;
            arrivalLocation.setError ("Cant find in bus_coordinates");
        }

        if (!valid)
            return;

        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm (graph);
        dijkstra.execute(nodes.get(findByVertexName(nodes, dep)));
        LinkedList<Vertex> path = dijkstra.getPath(nodes.get(findByVertexName(nodes, arr)));


        bus_coordinates = new Bus_Coordinates[path.size()];


        for (int i = 0; i < path.size(); i++) {
            Vertex vertex = path.get(i);
            bus_coordinates[i] = data.get(findByVertexName (data, vertex.toString()));
        }

        mMap.clear();
        onMapReady(mMap);
        plan = true;
    }

    private void addLane(String laneId, int sourceLocNo, int destLocNo, Double duration) {
        Edge lane = new Edge(laneId, nodes.get(sourceLocNo), nodes.get(destLocNo), duration);
        edges.add(lane);
    }
}

class Vertex {
    final private String id;
    final private String name;


    public Vertex(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vertex other = (Vertex) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

}

class Edge  {
    private final String id;
    private final Vertex source;
    private final Vertex destination;
    private final Double weight;

    public Edge(String id, Vertex source, Vertex destination, Double weight) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }
    public Vertex getDestination() {
        return destination;
    }

    public Vertex getSource() {
        return source;
    }
    public Double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return source + " " + destination;
    }

}

class Graph {
    private final List<Vertex> vertexes;
    private final List<Edge> edges;

    public Graph(List<Vertex> vertexes, List<Edge> edges) {
        this.vertexes = vertexes;
        this.edges = edges;
    }

    public List<Vertex> getVertexes() {
        return vertexes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

}

class DijkstraAlgorithm {

    private final List<Vertex> nodes;
    private final List<Edge> edges;
    private Set<Vertex> settledNodes;
    private Set<Vertex> unSettledNodes;
    private Map<Vertex, Vertex> predecessors;
    private Map<Vertex, Double> distance;

    public DijkstraAlgorithm(Graph graph) {
        // create a copy of the array so that we can operate on this array
        this.nodes = new ArrayList<Vertex>(graph.getVertexes());
        this.edges = new ArrayList<Edge>(graph.getEdges());
    }

    public void execute(Vertex source) {
        settledNodes = new HashSet<Vertex>();
        unSettledNodes = new HashSet<Vertex>();
        distance = new HashMap<Vertex, Double>();
        predecessors = new HashMap<Vertex, Vertex>();
        distance.put(source, 0D);
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0) {
            Vertex node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }

    private void findMinimalDistances(Vertex node) {
        List<Vertex> adjacentNodes = getNeighbors(node);
        for (Vertex target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node)
                    + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node) + getDistance(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        }

    }

    private Double getDistance(Vertex node, Vertex target) {
        for (Edge edge : edges) {
            if (edge.getSource().equals(node)
                    && edge.getDestination().equals(target)) {
                return edge.getWeight();
            }
        }
        throw new RuntimeException("Should not happen");
    }

    private List<Vertex> getNeighbors(Vertex node) {
        List<Vertex> neighbors = new ArrayList<Vertex>();
        for (Edge edge : edges) {
            if (edge.getSource().equals(node)
                    && !isSettled(edge.getDestination())) {
                neighbors.add(edge.getDestination());
            }
        }
        return neighbors;
    }

    private Vertex getMinimum(Set<Vertex> vertexes) {
        Vertex minimum = null;
        for (Vertex vertex : vertexes) {
            if (minimum == null) {
                minimum = vertex;
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }

    private boolean isSettled(Vertex vertex) {
        return settledNodes.contains(vertex);
    }

    private Double getShortestDistance(Vertex destination) {
        Double d = distance.get(destination);
        if (d == null) {
            return Double.MAX_VALUE;
        } else {
            return d;
        }
    }

    /*
     * This method returns the path from the source to the selected target and
     * NULL if no path exists
     */
    public LinkedList<Vertex> getPath(Vertex target) {
        LinkedList<Vertex> path = new LinkedList<Vertex>();
        Vertex step = target;
        // check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        // Put it into the correct order
        Collections.reverse(path);
        return path;
    }
}

class TestDijkstraAlgorithm {

    private List<Vertex> nodes;
    private List<Edge> edges;


    public void testExcute() {
        nodes = new ArrayList<Vertex>();
        edges = new ArrayList<Edge>();
        for (int i = 0; i < 11; i++) {
            Vertex location = new Vertex("Node_" + i, "Node_" + i);
            nodes.add(location);
        }



        // Lets check from location Loc_1 to Loc_10
        Graph graph = new Graph(nodes, edges);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(nodes.get(0));
        LinkedList<Vertex> path = dijkstra.getPath(nodes.get(10));



        for (Vertex vertex : path) {
            System.out.println(vertex);
        }

    }


}
