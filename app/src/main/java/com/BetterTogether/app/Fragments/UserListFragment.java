package com.BetterTogether.app.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.BetterTogether.app.Dialogs.RewardPopup;

import com.BetterTogether.app.DataManager;
import com.BetterTogether.app.Dialogs.TokenPopup;
import com.BetterTogether.app.R;
import com.BetterTogether.app.DataUpdateListener;
import com.BetterTogether.app.adapters.UserListAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import DB.RewardType;

import com.BetterTogether.app.Pair;
import com.BetterTogether.app.Person;


public class UserListFragment extends Fragment implements DataUpdateListener {
        private ArrayList<Integer> selectedItems;

    private DataManager manager;

    private GridView gridView;

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

        askForToken(false);

        gridView = getView().findViewById(R.id.user_list);
        selectedItems = new ArrayList<>();

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


    void setUpGridView() {
        List<Person> persons = manager.getActiveUsers();

        UserListAdapter adapter = new UserListAdapter(getContext(), persons);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener((adapterView, view, position, l) ->
                selectItemAtPosition(position));


        disableScrolling();
    }


    @SuppressLint("ClickableViewAccessibility")
    private void disableScrolling() {
        gridView.setOnTouchListener((View v, MotionEvent e) ->
                e.getAction() == MotionEvent.ACTION_MOVE);
        gridView.setVerticalScrollBarEnabled(false);
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
        if (manager.isRewardReached(RewardType.PIZZA)) {
            popupIsActive = true;
            new RewardPopup(this).whistle(RewardType.PIZZA);
        }
        if (manager.isRewardReached(RewardType.CAKE)) {
            popupIsActive = true;
            new RewardPopup(this).whistle(RewardType.CAKE);
        }
    }

    public void setPopupIsActiveFalse() {
        this.popupIsActive = false;

    }

    private void writeStatus() {
        if (!popupIsActive) {
            createRewardPopupIfReachedReward();
        }

        if (!manager.getAllPairs().isEmpty()) {
            TextView lastPair = getView().findViewById(R.id.last_event);
            lastPair.setText(manager.getAllPairs().get(manager.getAllPairs().size() - 1).getPerson1() +
                    " & " + manager.getAllPairs().get(manager.getAllPairs().size() - 1).getPerson2());
        }
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
    public void tokenRejected(){
        askForToken(true);
    }

    @Override
    public void updateGrid() {
        setUpGridView();
    }

    @Override
    public void updateStatus() {
        writeStatus();
    }

    @Override
    public void tokenReceived(String token) {
        manager = new DataManager(token, this);
    }

    private void askForToken(boolean rejected){
        String message = rejected ?
                "Token rejected." :
                "Valid token needed for access.";
        // Opens dialog containing void setPositiveButton() method
        // from android API.
        // tokenReceived() is called when token is received
        new TokenPopup(this).setUpGetTokenView(message);
    }

}
