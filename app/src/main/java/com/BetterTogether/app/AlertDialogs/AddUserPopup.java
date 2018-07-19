package com.BetterTogether.app.AlertDialogs;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.BetterTogether.app.R;
import com.BetterTogether.app.Fragments.UserListFragment;

import com.BetterTogether.app.Logic.Person;
import DB.ImageReader;

public class AddUserPopup extends PopupView {

    private ImageView userImage;
    private String defaultImage = "unknown";

    private View popupView;

    private TextView staticUsername;
    private EditText username;
    private EditText name;

    private Button image;
    private Button add;
    private Button cancel;

    private AlertDialog dialog;

    public AddUserPopup(UserListFragment userListFragment) {
        super(userListFragment);
        setUpAlertDialog();
    }

    private void setUpAlertDialog() {
        popupView = layoutInflater.inflate(R.layout.add_user_layout, null);

        alertBuilder.setView(popupView);
        staticUsername = popupView.findViewById(R.id.editUsername);
        username = popupView.findViewById(R.id.username);
        name = popupView.findViewById(R.id.first_name);
        userImage = popupView.findViewById(R.id.mImageView);
        userImage.setImageBitmap(ImageReader.imageToBitmap(userListFragment.getContext(), defaultImage));

        cancel = popupView.findViewById(R.id.cancel);
    }

    private void openDialog() {
        dialog = alertBuilder.create();
        dialog.show();
        cancel.setOnClickListener(btn -> dialog.dismiss());
    }

    public void closeDialog() {
        dialog.dismiss();
    }

    public void openCreateDialog() {
        openDialog();
        staticUsername.setVisibility(View.INVISIBLE);
        alertBuilder.setTitle("Add User");
        add.setText("Add User");
    }

    public void openEditDialog(Person person) {
        openDialog();
        alertBuilder.setTitle("Edit User");
        add.setText("Edit User");
        staticUsername.setText(person.getUsername());
        username.setText(person.getUsername());
        name.setText(person.getName());
        userImage.setImageBitmap(ImageReader.byteArrayToBitmap(person.getImage()));
        username.setVisibility(View.INVISIBLE);
    }

    public View getView() {
        return popupView;
    }

    public Person getPerson() {
        return new Person(username.getText().toString(), name.getText().toString(), ImageReader.bitmapToByte(
                ((BitmapDrawable) userImage.getDrawable()).getBitmap()));
    }

    public void setUserImage(Bitmap bitmap) {
        userImage.setImageBitmap(bitmap);
    }

    public void setAddButton(Button add) {
        this.add = add;
    }

    public void setImageButton(Button image) {
        this.image = image;
    }


    /*
        manager.addUser(username.getText().

    toString(),
                name.getText().

    toString(),lastName.

    getText().

    toString(),
                ImageReader.bitmapToByte(
                        ((BitmapDrawable)userImage.getDrawable()).

    getBitmap()));

}



                if(person==null){
                        manager.addUser(username.getText().toString(),
                        name.getText().toString(),lastName.getText().toString(),
                        ImageReader.bitmapToByte(
                        ((BitmapDrawable)userImage.getDrawable()).getBitmap()));
                        }else
                        manager.editUser(person,name.getText().toString(),
                        lastName.getText().toString(),
                        ((BitmapDrawable)userImage.getDrawable()).getBitmap());
                        //reset camera image
                        userImage=null;
                        });
    */


}
