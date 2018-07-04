package com.BetterTogether.app;

import android.annotation.SuppressLint;
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
import DB.RewardType;
import DB.Tables.Pair;
import DB.Tables.Person;
import DB.Tables.Reward;

import static android.app.Activity.RESULT_OK;

public class UserListFragment extends Fragment {

    private ArrayList<Integer> selectedItems;

    private int cakeThreshold;
    private int pizzaThreshold;

    private int unusedCake;
    private int unusedPizza;

    private List<Person> users;

    private GridView gridView;
    private TextView numPairs;
    private ImageView user_image;

    private List<Pair> allPairs;
    private List<Pair> pizzaPairs;
    private List<Pair> cakePairs;


    private TextView pizzaCount;
    private TextView cakeCount;
    private TextView pizzaClaim;
    private TextView cakeClaim;

    private DatabaseThreadHandler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_list, container, false);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        gridView = getView().findViewById(R.id.user_list);
        handler = new DatabaseThreadHandler(getContext());
        selectedItems = new ArrayList<>();
        pizzaThreshold = -1;
        cakeThreshold = -1;
        unusedPizza = -1;
        unusedCake = -1;

        Button add_user = getView().findViewById(R.id.add_user);
        add_user.setOnClickListener(view_user -> add_user());

        Button okBtn = getView().findViewById(R.id.create_pair_button);
        okBtn.setOnClickListener(btn -> createPair());

        Button cancelBtn = getView().findViewById(R.id.reset_selection_button);
        cancelBtn.setOnClickListener(view12 -> resetSelectedPersons());
        selectedItems = new ArrayList<>();
        handler.allActivePersons().subscribe(
                persons -> setUpGridView(persons),
                error -> Toast.makeText(getContext(), "Failed loading users from database", Toast.LENGTH_SHORT).show());
        getPairs();
        getThresholds();
        getUnusedRewards();
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

    @SuppressLint("CheckResult")
    private void createPair() {
        if (selectedItems.size() < 2) {
            Toast.makeText(getContext(), "You need to select two users for pair programming", Toast.LENGTH_SHORT).show();
            return;
        }
        Pair pair = new Pair(new Date());
        pair.setPerson1(users.get(selectedItems.get(0)).getUsername());
        pair.setPerson2(users.get(selectedItems.get(1)).getUsername());
        handler.addPair(pair).subscribe(
                longs -> {
                    Toast.makeText(getContext(),
                            "Added pair programming with: " + pair.getPerson1() + " and " + pair.getPerson2(), Toast.LENGTH_SHORT).show();
                    getPairs();
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

        } catch (Exception e) {
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
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            user_image.setImageBitmap(imageBitmap);
        }
    }


    private void submitUser(Dialog dlog, EditText username, EditText firstName, EditText lastName) {
        dlog.dismiss();
        Person newUser = new Person(username.getText().toString(), firstName.getText().toString(), lastName.getText().toString());
        if (users.contains(newUser)) {
            Toast.makeText(getContext(), "User already exists", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getContext(), "Welcome " + newUser.getUsername(), Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("CheckResult")
    private void getUnusedRewards() {
        handler.getUnusedRewardsCount(RewardType.CAKE).subscribe(count -> {
            unusedCake = count;
            writeStatusIfAble();
        });

        handler.getUnusedRewardsCount(RewardType.PIZZA).subscribe(count -> {
            unusedPizza = count;
            writeStatusIfAble();
        });
    }

    @SuppressLint("CheckResult")
    private void getPairs() {
        handler.getPairHistory(new Date(new GregorianCalendar(1900, 01, 01, 00, 00, 00).getTimeInMillis()))
                .subscribe(pairs -> {
                    allPairs = pairs;
                    writeStatusIfAble();
                });

        handler.getPairsSinceLastReward(RewardType.PIZZA).subscribe(pairs -> {
            pizzaPairs = pairs;
            writeStatusIfAble();
        });

        handler.getPairsSinceLastReward(RewardType.CAKE).subscribe(pairs -> {
            cakePairs = pairs;
            writeStatusIfAble();
        });
    }

    @SuppressLint("CheckResult")
    private void getThresholds() {
        handler.getThreshold(RewardType.PIZZA).subscribe(threshold -> {
            pizzaThreshold = threshold;
            writeStatusIfAble();
        });

        handler.getThreshold(RewardType.CAKE).subscribe(threshold -> {
            cakeThreshold = threshold;
            writeStatusIfAble();
        });
    }

    private void writeStatusIfAble() {
        if (cakeThreshold == 0 || pizzaThreshold == 0 || cakePairs == null || pizzaPairs == null || allPairs == null || unusedPizza == -1 || unusedCake == -1)
            return;

        if (cakePairs.size() == cakeThreshold) {
            cakePairs = null;
            unusedCake = -1;
            pizzaPairs = null;
            unusedPizza = -1;
            addReward(RewardType.CAKE);
            return;
        }

        if(pizzaPairs.size() == pizzaThreshold) {
            cakePairs = null;
            unusedCake = -1;
            pizzaPairs = null;
            unusedPizza = -1;
            addReward(RewardType.PIZZA);
            return;
        }

        numPairs = getView().findViewById(R.id.num_of_pairs);

        pizzaCount = getView().findViewById(R.id.pizza_text);
        cakeCount = getView().findViewById(R.id.cake_text);

        pizzaClaim = getView().findViewById(R.id.pizza_iou);
        cakeClaim = getView().findViewById(R.id.cake_iou);

        numPairs.setText(Integer.toString(allPairs.size()));

        pizzaCount.setText(Integer.toString(pizzaPairs.size()) + "/" + Integer.toString(pizzaThreshold));
        cakeCount.setText(Integer.toString(cakePairs.size()) + "/" + Integer.toString(cakeThreshold));

        pizzaClaim.setText(Integer.toString(unusedPizza));
        cakeClaim.setText(Integer.toString(unusedCake));

        if(allPairs.isEmpty()){
            return;
        }
        TextView lastPair = getView().findViewById(R.id.last_event);
        lastPair.setText(allPairs.get(allPairs.size()-1).getPerson1() + " & " + allPairs.get(allPairs.size()-1).getPerson2());

    }

    @SuppressLint("CheckResult")
    private void addReward(RewardType type){
        handler.addNewReward(new Reward(new Date(), type)).subscribe(longs -> {
            getPairs();
            getUnusedRewards();
        });
    }

    private void resetSelectedPersons() {
        for (Integer i : selectedItems)
            gridView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
        selectedItems.clear();
    }

}
