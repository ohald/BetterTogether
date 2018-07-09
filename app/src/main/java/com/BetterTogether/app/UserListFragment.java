package com.BetterTogether.app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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

import com.BetterTogether.app.adapters.UserListAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import DB.DatabaseThreadHandler;
import DB.RewardType;
import DB.Tables.Pair;
import DB.Tables.Person;
import JSONReader.ImageReader;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

public class UserListFragment extends Fragment implements Observer {

    private ArrayList<Integer> selectedItems;

    private UserListDataManager manager;

    private GridView gridView;
    private ImageView userImage;
    private Bitmap userImageBitmap;

    private String defaultImage = "unknown";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_list, container, false);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        selectedItems = new ArrayList<>();

        manager = new UserListDataManager(new DatabaseThreadHandler(
                getActivity().getApplicationContext(), Schedulers.io(),
                AndroidSchedulers.mainThread()));
        manager.addObserver(this);
        gridView = getView().findViewById(R.id.user_list);
        selectedItems = new ArrayList<>();

        //add initial data to database
        manager.refreshDB(getContext());

        Button addUser = getView().findViewById(R.id.add_user);
        addUser.setOnClickListener(view_user -> setUpAlertDialog(null));

        Button okBtn = getView().findViewById(R.id.create_pair_button);
        okBtn.setOnClickListener(btn -> createPair());

        Button cancelBtn = getView().findViewById(R.id.reset_selection_button);
        cancelBtn.setOnClickListener(view12 -> resetSelectedPersons());

        Button claim_cake = getView().findViewById(R.id.reset_cake);
        claim_cake.setOnClickListener(btn -> {
            if (manager.getUnusedCake() != 0) {
                new RewardPopup(this).claimReward(RewardType.CAKE);
            } else {
                Toast.makeText(getContext(), "You don't have any cake to claim",
                        Toast.LENGTH_SHORT).show();
            }
        });

        Button claim_pizza = getView().findViewById(R.id.reset_pizza);
        claim_pizza.setOnClickListener(btn -> {
            if (manager.getUnusedPizza() != 0) {
                new RewardPopup(this).claimReward(RewardType.PIZZA);
            } else {
                Toast.makeText(getContext(), "You don't have any pizza to claim",
                        Toast.LENGTH_SHORT).show();
            }
        });
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

    void setUpGridView(List<Person> persons) {
        List<Person> activeUsers = manager.getActiveUsers();
        UserListAdapter adapter = new UserListAdapter(getContext(), activeUsers);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener((adapterView, view, position, l) ->
                selectItemAtPosition(position));
        gridView.setOnItemLongClickListener(((adapterView, view, position, l) ->
                setUpAlertDialog(persons.get(position))));
    }

    @SuppressLint("CheckResult")
    private void createPair() {
        if (selectedItems.size() < 2) {
            Toast.makeText(getContext(), "You need to select two users for pair programming",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Pair pair = new Pair(new Date());
        pair.setPerson1(manager.getActiveUsers().get(selectedItems.get(0)).getUsername());
        pair.setPerson2(manager.getActiveUsers().get(selectedItems.get(1)).getUsername());
        manager.addPair(pair);
        resetSelectedPersons();
    }

    private boolean setUpAlertDialog(Person person) {
        try {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.add_user_layout, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            builder.setView(popupView);
            TextView staticUsername = popupView.findViewById(R.id.editUsername);
            EditText username = popupView.findViewById(R.id.username);
            EditText firstName = popupView.findViewById(R.id.first_name);
            EditText lastName = popupView.findViewById(R.id.last_name);
            userImage = popupView.findViewById(R.id.mImageView);

            Button image = popupView.findViewById(R.id.image);
            Button add = popupView.findViewById(R.id.add);
            Button cancel = popupView.findViewById(R.id.cancel);

            if (person == null) {
                builder.setTitle("Create User");
                staticUsername.setVisibility(View.INVISIBLE);
            } else {
                builder.setTitle("Edit user");
                add.setText("Done");
                staticUsername.setText(person.getUsername());
                username.setText(person.getUsername());
                firstName.setText(person.getFirstName());
                lastName.setText(person.getLastName());
                userImage.setImageBitmap(ImageReader.byteArrayToBitmap(person.getImage()));
                username.setVisibility(View.INVISIBLE);
            }
            AlertDialog dialog = builder.create();
            dialog.show();

            image.setOnClickListener(btn -> openCameraActivity());
            add.setOnClickListener(btn -> {
                if (username.getText().toString().isEmpty() ||
                        firstName.getText().toString().isEmpty() ||
                        lastName.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Username, first name and last name must be filled out!", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
                if (person == null) {
                    manager.addUser(username.getText().toString(),
                            firstName.getText().toString(), lastName.getText().toString()
                            , userImageBitmap == null
                                    ? ImageReader.imageToByte(getContext(), defaultImage)
                                    : ImageReader.bitmapToByte(userImageBitmap));
                } else
                    manager.editUser(person, firstName.getText().toString(),
                            lastName.getText().toString(),
                            ((BitmapDrawable) userImage.getDrawable()).getBitmap());
                //reset camera image
                userImage = null;
                userImageBitmap = null;
            });
            cancel.setOnClickListener(btn -> dialog.dismiss());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
            userImage.setImageBitmap(imageBitmap);
            userImageBitmap = imageBitmap;
        }
    }

    private void writeStatus() {
        if (manager.getCakePairs().size() == manager.getCakeThreshold()) {
            RewardPopup cakeReached = new RewardPopup(this);
            cakeReached.whistle(RewardType.CAKE);
            manager.addReward(RewardType.CAKE);
            return;
        }

        if (manager.getPizzaPairs().size() == manager.getPizzaThreshold()) {
            RewardPopup pizzaReached = new RewardPopup(this);
            pizzaReached.whistle(RewardType.PIZZA);
            manager.addReward(RewardType.PIZZA);
            return;
        }

        TextView numPairs = getView().findViewById(R.id.num_of_pairs);

        TextView pizzaCount = getView().findViewById(R.id.pizza_text);
        TextView cakeCount = getView().findViewById(R.id.cake_text);

        TextView pizzaClaim = getView().findViewById(R.id.pizza_iou);
        TextView cakeClaim = getView().findViewById(R.id.cake_iou);

        numPairs.setText(Integer.toString(manager.getAllPairs().size()));

        pizzaCount.setText(Integer.toString(manager.getAllPairs().size()) + "/" +
                Integer.toString(manager.getPizzaThreshold()));
        cakeCount.setText(Integer.toString(manager.getCakePairs().size()) + "/" +
                Integer.toString(manager.getCakeThreshold()));

        pizzaClaim.setText(Integer.toString(manager.getUnusedPizza()));
        cakeClaim.setText(Integer.toString(manager.getUnusedCake()));

        if (manager.getAllPairs().isEmpty()) {
            return;
        }
        TextView lastPair = getView().findViewById(R.id.last_event);
        lastPair.setText(manager.getAllPairs().get(manager.getAllPairs().size() - 1).getPerson1() +
                " & " + manager.getAllPairs().get(manager.getAllPairs().size() - 1).getPerson2());
    }

    private void resetSelectedPersons() {
        for (Integer i : selectedItems)
            gridView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
        selectedItems.clear();
    }

    public UserListDataManager getManager() {
        return manager;
    }

    @Override
    public void update(Observable observable, Object o) {
        setUpGridView(manager.getActiveUsers());
        writeStatus();

    }
}
