package com.BetterTogether.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private String[] dataSet;

    public MyAdapter(Context context, String[] dataSet){
        this.dataSet = dataSet;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        if(dataSet!=null) {
            return dataSet.length;
        }
        else{
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        if(i<dataSet.length) {
            return dataSet[i];
        }
        else return -1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.example_list, parent, false);

        TextView name = rowView.findViewById(R.id.username);
        ImageView image = rowView.findViewById(R.id.profile_image);

        name.setText(dataSet[position]);
        return rowView;
    }
}
