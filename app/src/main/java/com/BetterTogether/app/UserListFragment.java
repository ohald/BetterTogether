package com.BetterTogether.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import DB.DatabaseThreadHandler;
import DB.Tables.Pair;
import DB.Tables.Person;
import io.reactivex.disposables.Disposable;

public class UserListFragment extends Fragment {

    private ArrayList<Integer> selectedItems;

    private List<Person> users;

    private GridView gridView;
    private TextView numPairs;
    private ImageView user_image;

    private DatabaseThreadHandler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        gridView = getView().findViewById(R.id.user_list);
        numPairs = getView().findViewById(R.id.num_of_pairs);
        handler = new DatabaseThreadHandler(getContext());

        Button add_user = getView().findViewById(R.id.add_user);
        add_user.setOnClickListener(view_user -> add_user());

        Button okBtn = getView().findViewById(R.id.create_pair_button);
        okBtn.setOnClickListener(view1 -> createPair());

        Button cancelBtn = getView().findViewById(R.id.reset_selection_button);
        cancelBtn.setOnClickListener(view12 -> resetSelectedPersons());
        selectedItems = new ArrayList<>();
        Disposable d = handler.allActivePersons().subscribe(
                persons -> setUpGridView(persons),
                error -> Toast.makeText(getContext(), "Failed loading users from database", Toast.LENGTH_SHORT).show());
        writePairCountToScreen();
    }

    private void selectItemAtPosition(int position) {
        if (selectedItems.contains(position)) {
            gridView.getChildAt(position).setBackgroundColor(Color.TRANSPARENT);
            selectedItems.remove(selectedItems.indexOf(position));
            return;
        }

        if (selectedItems.size() > 1) return;

        selectedItems.add(position);
        gridView.getChildAt(position).setBackgroundColor(Color.argb(126, 0, 255, 0));
    }

    private void setUpGridView(List<Person> persons) {
        users = persons;
        UserListAdapter adapter = new UserListAdapter(getContext(), users);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener((adapterView, view1, position, l) -> selectItemAtPosition(position));
    }

    private void createPair() {
        if (selectedItems.size() < 2) {
            Toast.makeText(getContext(), "You need to select two users for pair programming", Toast.LENGTH_SHORT).show();
            return;
        }
        Pair pair = new Pair(new Date());
        pair.setPerson1(users.get(selectedItems.get(0)).getUsername());
        pair.setPerson2(users.get(selectedItems.get(1)).getUsername());
        Disposable d = handler.addPair(pair).subscribe(
                longs -> {
                    Toast.makeText(getContext(),
                            "Added pair programming with: " + pair.getPerson1() + " and " + pair.getPerson2(), Toast.LENGTH_SHORT).show();
                    writePairCountToScreen();
                    resetSelectedPersons();
                },
                error -> Toast.makeText(getContext(), "Something went wrong while inserting to database.", Toast.LENGTH_SHORT).show());
    }

    private void add_user() {
        try {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popUpView = inflater.inflate(R.layout.popup_layout, null);
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
            alertBuilder.setView(popUpView);
            alertBuilder.setTitle("Create User");
            EditText username = (EditText) popUpView.findViewById(R.id.username);
            EditText firstName = (EditText) popUpView.findViewById(R.id.first_name);
            EditText lastName = (EditText) popUpView.findViewById(R.id.last_name);
            user_image = (ImageView) popUpView.findViewById(R.id.mImageView);
            Button image = popUpView.findViewById(R.id.image);
            Button add = popUpView.findViewById(R.id.add);
            Button cancel = popUpView.findViewById(R.id.cancel);

            AlertDialog dialog = alertBuilder.create();
            dialog.show();

            image.setOnClickListener(btn -> openCameraActivity());
            add.setOnClickListener(btn -> submitUser(dialog, username, firstName, lastName));
            cancel.setOnClickListener(btn -> dialog.dismiss());

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void openCameraActivity() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (camera.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(camera, 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            user_image.setImageBitmap(imageBitmap);
        }
    }


    private void submitUser(Dialog dlog, EditText username, EditText firstName, EditText lastName){
        dlog.dismiss();
        Person newUser = new Person(username.getText().toString(), firstName.getText().toString(), lastName.getText().toString());
        Toast.makeText(getContext(), "Hei "+newUser.getUsername(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(getContext(), "User added", Toast.LENGTH_SHORT).show();
    }

    private void writePairCountToScreen() {
        Disposable d = handler.getPairHistory(new Date(new GregorianCalendar(1900, 01, 01, 00, 00, 00).getTimeInMillis()))
                .subscribe(pairs -> numPairs.setText("#Pairs: " + pairs.size()));
    }

    public void resetSelectedPersons() {
        for (Integer i : selectedItems)
            gridView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
        selectedItems.clear();
    }
}
