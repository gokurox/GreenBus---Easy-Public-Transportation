package com.example.gursimransingh.greenbus_evs_iiitd.lv_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gursimransingh.greenbus_evs_iiitd.R;
import com.example.gursimransingh.greenbus_evs_iiitd.sqlite.dataholder.Feedback;

/**
 * Created by Gursimran Singh on 29-04-2016.
 */
public class BusSafety_ListAdapter extends ArrayAdapter<Feedback> {
    Context context;
    Feedback[] values;

    public BusSafety_ListAdapter(Context context, Feedback[] values) {
        super(context, R.layout.bus_safety_list, values);
        this.values = values;
        this.context = context;
    }

    @Override
    public View getView (int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.bus_safety_list, parent, false);

        TextView ID = (TextView) rowView.findViewById(R.id.bsl_number);
        TextView reviewer = (TextView) rowView.findViewById(R.id.bsl_reviwer);
        TextView feedback = (TextView) rowView.findViewById(R.id.bsl_feedback);
        TextView rating = (TextView) rowView.findViewById(R.id.bsl_rating);

        ID.setText (Integer.toString(position + 1));
        reviewer.setText (values[position].email);
        feedback.setText (values[position].feedback);
        rating.setText (Float.toString(values[position].rating));

        return rowView;
    }
}
