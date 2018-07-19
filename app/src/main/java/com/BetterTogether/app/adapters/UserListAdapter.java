package com.BetterTogether.app.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.BetterTogether.app.R;

import java.util.List;

import com.BetterTogether.app.Logic.Person;
import DB.ImageReader;

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
        View rowView = inflater.inflate(R.layout.user_list_element, parent, false);

        //set images to corresponding users
        byte[] image = dataSet.get(position).getImage();
        ImageView imageView = rowView.findViewById(R.id.profile_image);
        Bitmap bitmap;

        //TODO:find permanent solution -- remove null check.
        if(image == null){
            bitmap = ImageReader.imageToBitmap(rowView.getContext(), "unknown");
        } else
            bitmap = ImageReader.byteArrayToBitmap(image);

        imageView.setImageBitmap(bitmap);

        TextView name = rowView.findViewById(R.id.username);
        String displayedText = dataSet.get(position).getName();
        name.setText(displayedText);
        return rowView;
    }

}
