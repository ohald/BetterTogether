package com.BetterTogether.app;

import android.annotation.SuppressLint;
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
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.BetterTogether.app.AlertDialogs.AddUserPopup;
import com.BetterTogether.app.AlertDialogs.RewardPopup;
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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

public class UserListFragment extends Fragment implements Observer {

    private ArrayList<Integer> selectedItems;

    private DataManager manager;

    private GridView gridView;

    private AddUserPopup addUserPopup;

    private boolean popupIsActive;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_list, container, false);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        selectedItems = new ArrayList<>();

        manager = new DataManager(new DatabaseThreadHandler(
                getActivity().getApplicationContext(), Schedulers.io(),
                AndroidSchedulers.mainThread()));
        manager.addObserver(this);
        gridView = getView().findViewById(R.id.user_list);
        selectedItems = new ArrayList<>();

        //add initial data to database
        manager.refreshDB(getContext());

        Button addUser = getView().findViewById(R.id.add_user);
        addUser.setOnClickListener(view_user -> createOrEditUser(null));

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
                createOrEditUser(persons.get(position))));
    }

    private boolean createOrEditUser(Person person) {
        addUserPopup = new AddUserPopup(this);
        Button add = addUserPopup.getView().findViewById(R.id.add);
        Button image = addUserPopup.getView().findViewById(R.id.image);
        image.setOnClickListener(btn -> openCameraActivity());
        addUserPopup.setImageButton(image);
        if (person == null) {
            createUser(add);
        } else {
            editUser(add, person);
        }
        return true;
    }

    private void createUser(Button add) {
        add.setOnClickListener(btn -> {
            if (!isValidInput(addUserPopup.getPerson()))
                return;
            manager.addUser(addUserPopup.getPerson());
            addUserPopup.closeDialog();
            Toast.makeText(getContext(), addUserPopup.getPerson().getUsername() + " added", Toast.LENGTH_SHORT).show();
        });
        addUserPopup.setAddButton(add);
        addUserPopup.openCreateDialog();
    }

    private void editUser(Button add, Person person) {
        add.setOnClickListener(btn -> {
            if (!isValidInput(addUserPopup.getPerson()))
                return;
            manager.editUser(addUserPopup.getPerson());
            addUserPopup.closeDialog();
            Toast.makeText(getContext(), addUserPopup.getPerson().getUsername() + " edited", Toast.LENGTH_SHORT).show();
        });
        addUserPopup.setAddButton(add);
        addUserPopup.openEditDialog(person);
    }

    private boolean isValidInput(Person person) {
        if (manager.getAllUsers().contains(person)) {
            Toast.makeText(getContext(), "Username already taken", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (person.getUsername().equals("") || person.getFirstName().equals("") || person.getLastName().equals("")) {
            Toast.makeText(getContext(), "You need to fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
        Toast.makeText(getContext(),
                "Added pair programming with: " + pair.getPerson1() +
                        " and " + pair.getPerson2(), Toast.LENGTH_SHORT).show();
    }

    public void createRewardPopupIfReachedReward() {
        if (manager.getPizzaPairs().size() == manager.getPizzaThreshold()) {
            popupIsActive = true;
            manager.addReward(RewardType.PIZZA);
            RewardPopup popup = new RewardPopup(this);
            popup.whistle(RewardType.PIZZA);
            return;
        }
        if (manager.getCakePairs().size() == manager.getCakeThreshold()) {
            popupIsActive = true;
            manager.addReward(RewardType.CAKE);
            RewardPopup popup = new RewardPopup(this);
            popup.whistle(RewardType.CAKE);
            return;
        }
    }

    public void setPopupIsActiveFalse() {
        this.popupIsActive = false;

    }

    private void writeStatus() {
        if (!popupIsActive) {
            createRewardPopupIfReachedReward();
        }

        TextView numPairs = getView().findViewById(R.id.num_of_pairs);

        TextView pizzaCount = getView().findViewById(R.id.pizza_text);
        TextView cakeCount = getView().findViewById(R.id.cake_text);

        TextView pizzaClaim = getView().findViewById(R.id.pizza_iou);
        TextView cakeClaim = getView().findViewById(R.id.cake_iou);

        numPairs.setText(Integer.toString(manager.getAllPairs().size()));

        pizzaCount.setText(Integer.toString(manager.getPizzaPairs().size()) + "/" +
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

    public DataManager getManager() {
        return manager;
    }

    @Override
    public void update(Observable observable, Object o) {
        setUpGridView(manager.getActiveUsers());
        writeStatus();

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
            addUserPopup.setUserImage(imageBitmap);
        }
    }
}
