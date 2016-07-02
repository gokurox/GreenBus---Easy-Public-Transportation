package com.example.gursimransingh.greenbus_evs_iiitd.lv_adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gursimransingh.greenbus_evs_iiitd.R;

/**
 * Created by Gursimran Singh on 30-04-2016.
 */
public class Discover_LVAdapter extends ArrayAdapter {
    String[] color_names;
    Integer[] image_id;
    Context context;
    public Discover_LVAdapter (Activity context,Integer[] image_id, String[] text){
        super(context, R.layout.discover_route_column_row, text);

        this.color_names = text;
        this.image_id = image_id;
        this.context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View single_row = inflater.inflate(R.layout.discover_route_column_row, null,
                true);
        TextView textView = (TextView) single_row.findViewById(R.id.drcr_textView);
        ImageView imageView = (ImageView) single_row.findViewById(R.id.drcr_imageView);
        textView.setText(color_names[position]);
        imageView.setImageResource(image_id[position]);
        return single_row;
    }
}
