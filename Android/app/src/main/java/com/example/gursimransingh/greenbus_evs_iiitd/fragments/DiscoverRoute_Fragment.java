package com.example.gursimransingh.greenbus_evs_iiitd.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.gursimransingh.greenbus_evs_iiitd.R;
import com.example.gursimransingh.greenbus_evs_iiitd.lv_adapters.Discover_LVAdapter;
import com.example.gursimransingh.greenbus_evs_iiitd.sqlite.DatabaseHelper;
import com.example.gursimransingh.greenbus_evs_iiitd.sqlite.dataholder.Bus_Coordinates;
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

public class DiscoverRoute_Fragment extends Fragment implements OnMapReadyCallback {

    EditText bus_number;
    Button search_btn;
    ListView lv;


    //name of bus and color icon which is there in drawable
    String color_names[];
    Integer image_id[] = {R.drawable.a};

    private GoogleMap mMap;
    MapView mapView;

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

        getActivity().setTitle("Discover Bus Route");
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_discover_route, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bus_number = (EditText) getView().findViewById(R.id.dr_busnumber);
        search_btn = (Button) getView().findViewById(R.id.dr_btn_search);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchClicked();
            }
        });

        //setting list adapter here
        lv = (ListView) getView().findViewById(R.id.dr_listView);
        lv.setAdapter(null);
        mapView = (MapView) mView.findViewById(R.id.googleMap);

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

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
            mMap.addMarker(new MarkerOptions().position(location).title(bus_coordinates[i].stop_number + ". " + bus_coordinates[i].stop_name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
    }

    public void onSearchClicked () {
        String bn = bus_number.getText().toString();
        boolean valid = true;

        bus_number.setError(null);
        if (bn.isEmpty())
        {
            valid = false;
            bus_number.setError("Empty");
        }

        if (!valid)
            return;

        color_names = new String[1];
        color_names[0] = bn;

        DatabaseHelper db = DatabaseHelper.getInstance(getActivity());
        ArrayList<Bus_Coordinates> data = db.getData_Bus_Coordinates(bn);
        Discover_LVAdapter adapter = new Discover_LVAdapter (getActivity(), image_id, color_names);
        lv.setAdapter(adapter);

        bus_coordinates = new Bus_Coordinates[data.size()];
        for (int i=0; i < data.size(); i++)
        {
            bus_coordinates[i] = data.get(i);
        }

        mMap.clear();
        onMapReady (mMap);
    }
}



