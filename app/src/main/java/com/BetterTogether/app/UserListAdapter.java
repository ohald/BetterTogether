package com.BetterTogether.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import DB.Tables.Person;

public class UserListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Person> dataSet;

    public UserListAdapter(Context context, List<Person> dataSet) {
        this.dataSet = dataSet;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        if (dataSet != null) {
            return dataSet.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        if (i < dataSet.size()) {
            return dataSet.get(i);
        } else return -1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.example_list, parent, false);
        TextView name = rowView.findViewById(R.id.username);
        String displayedText = dataSet.get(position).getFirstName() + " " + dataSet.get(position).getLastName();
        name.setText(displayedText);
        return rowView;
    }
}
