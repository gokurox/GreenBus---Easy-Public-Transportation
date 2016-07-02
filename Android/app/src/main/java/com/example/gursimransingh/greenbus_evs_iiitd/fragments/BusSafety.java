package com.example.gursimransingh.greenbus_evs_iiitd.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gursimransingh.greenbus_evs_iiitd.R;
import com.example.gursimransingh.greenbus_evs_iiitd.lv_adapters.BusSafety_ListAdapter;
import com.example.gursimransingh.greenbus_evs_iiitd.sqlite.DatabaseHelper;
import com.example.gursimransingh.greenbus_evs_iiitd.sqlite.dataholder.Feedback;

import java.util.ArrayList;

public class BusSafety extends Fragment {

    EditText busNumber;
    Button submitButton;
    ListView lv;
    TextView avgRating;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bus_safety, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        busNumber = (EditText) getView().findViewById(R.id.bus_safety_busNumber);
        avgRating = (TextView) getView().findViewById(R.id.bus_safety_avg);
        submitButton = (Button) getView().findViewById(R.id.bus_safety_button);
        lv = (ListView) getView().findViewById(R.id.bus_safety_list);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitPressed();
            }
        });
    }

    public void onSubmitPressed () {
        String bn = busNumber.getText().toString();
        boolean valid = true;

        busNumber.setError(null);
        if (bn.isEmpty())
        {
            valid = false;
            busNumber.setError("Empty");
        }

        if (!valid)
            return;

        busNumber.setText(null);

        DatabaseHelper db = DatabaseHelper.getInstance(getActivity());
        ArrayList<Feedback> data = db.getData_Feedback(bn);
        Feedback[] data2 = new Feedback[data.size()];

        Float avg = 0f;
        for (int i = 0; i < data.size(); i++) {
            data2[i] = data.get(i);
            avg += data2[i].rating;
        }
        avg /= data.size();
        avgRating.setText ("Average Rating: " + avg);

        BusSafety_ListAdapter la = new BusSafety_ListAdapter (getActivity(), data2);
        lv.setAdapter(null);
        lv.setAdapter (la);
    }
}
