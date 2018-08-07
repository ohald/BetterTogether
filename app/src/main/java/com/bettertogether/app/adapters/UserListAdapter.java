package com.bettertogether.app.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bettertogether.app.R;
import java.util.List;

import com.bettertogether.app.Person;

public class UserListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Person> dataSet;
    private Drawable defaultImage;

    public UserListAdapter(Context context, List<Person> dataSet) {
        this.dataSet = dataSet;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        defaultImage = inflater.getContext().getDrawable(R.drawable.unknown);
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
        View cellView = inflater.inflate(R.layout.user_list_element, parent, false);
        String image = dataSet.get(position).getImage();
        ImageView imageView = cellView.findViewById(R.id.profile_image);


        //user has no image
        if(image.equalsIgnoreCase("unknown")){
            imageView.setImageDrawable(defaultImage);
        } else {
            imageView.setImageBitmap(base64ToScaledBitmap(image));
        }


        TextView name = cellView.findViewById(R.id.username);

        name.setText(getFirstName(dataSet.get(position).getName()));
        return cellView;
    }

    private String getFirstName(String name){
        String[] parts = name.split(" ");
        return parts[0];
    }

    private Bitmap base64ToScaledBitmap(String decodedImage){
        byte [] image = Base64.decode(decodedImage, Base64.DEFAULT);
        Bitmap b = BitmapFactory.decodeByteArray(image, 0, image.length);
        Bitmap scaled = Bitmap.createScaledBitmap(b, 100, 100, false);
        b.recycle();
        return scaled;
    }

}
