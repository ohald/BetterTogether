package com.bettertogether.app.fragments;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
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

import com.bettertogether.app.dialogs.RewardPopup;
import com.bettertogether.app.dialogs.ErrorPopup;
import com.bettertogether.app.DataManager;
import com.bettertogether.app.dialogs.TokenPopup;
import com.bettertogether.app.R;
import com.bettertogether.app.DataUpdateListener;
import com.bettertogether.app.adapters.UserListAdapter;

import java.util.ArrayList;
import java.util.List;

import db.RewardType;

import com.bettertogether.app.Pair;
import com.bettertogether.app.Person;


public class UserListFragment extends Fragment implements DataUpdateListener {
        private ArrayList<Integer> selectedItems;

    private DataManager manager;

    private GridView gridView;

    private boolean popupIsActive;
    private Button claimCake;
    private Button claimPizza;
    private Button resetSelection;

    private int selectionColor;
    private int pimpedButtonColor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_list, container, false);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        selectedItems = new ArrayList<>();

        int energyRed = getResources().getColor(R.color.energyRed);
        selectionColor = energyRed;
        pimpedButtonColor = energyRed;

        askForToken(false);

        gridView = getView().findViewById(R.id.user_list);
        selectedItems = new ArrayList<>();

        resetSelection = getView().findViewById(R.id.reset_selection_button);
        resetSelection.setOnClickListener(view12 -> resetSelectedPersons());

        claimCake = getView().findViewById(R.id.reset_cake);
        claimCake.setOnClickListener(btn -> {
            if (manager.getUnusedCake() > 0) {
                new RewardPopup(this).claimReward(RewardType.CAKE);
            } else {
                Toast.makeText(getContext(), "You don't have any cake to claim",
                        Toast.LENGTH_SHORT).show();
            }
        });


        claimPizza = getView().findViewById(R.id.reset_pizza);
        claimPizza.setOnClickListener(btn -> {
            if (manager.getUnusedPizza() > 0) {
                new RewardPopup(this).claimReward(RewardType.PIZZA);
            } else {
                Toast.makeText(getContext(), "You don't have any pizza to claim",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //nothing is fetched from DB yet.
        if(manager == null){
            return;
        }
        int num = manager.getActiveUsers().size();
        setGridColumnNumber(num);

    }

    private void selectItemAtPosition(int position) {
        if (selectedItems.contains(position)) {
            gridView.getChildAt(position).setBackgroundColor(Color.TRANSPARENT);
            selectedItems.remove(selectedItems.indexOf(position));

            // if nothing to de-select
            if(selectedItems.size() == 0)
                unPimpButton(resetSelection);
            return;
        }

        if (selectedItems.size() >= 2) return;

        selectedItems.add(position);
        pimpButton(resetSelection);

        gridView.getChildAt(position).setBackgroundColor(selectionColor);

        if(selectedItems.size() >= 2)
            createPair();
    }
    
    private void pimpButton(Button button) {
        button.getBackground().setColorFilter(pimpedButtonColor, PorterDuff.Mode.MULTIPLY);
    }

    void setUpGridView() {
        List<Person> persons = manager.getActiveUsers();

        setGridColumnNumber(persons.size());
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

        Pair pair = new Pair(
                manager.getActiveUsers().get(selectedItems.get(0)).getUsername(),
                manager.getActiveUsers().get(selectedItems.get(1)).getUsername());
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

        pimpIfAvailableRewards();
        if (!manager.getAllPairs().isEmpty()) {
            TextView lastPair = getView().findViewById(R.id.last_event);
            lastPair.setText(manager.getAllPairs().get(manager.getAllPairs().size() - 1).getPerson1() +
                    " & " + manager.getAllPairs().get(manager.getAllPairs().size() - 1).getPerson2());
        }
    }

    private void unPimpButton(Button button) {
        button.getBackground().clearColorFilter();
    }

    private void pimpIfAvailableRewards() {
        if (manager.getUnusedCake() > 0)
            pimpButton(claimCake);
        else
            unPimpButton(claimCake);
        if (manager.getUnusedPizza() > 0)
            pimpButton(claimPizza);
        else
            unPimpButton(claimPizza);
    }

    private void resetSelectedPersons() {
        for (Integer i : selectedItems)
            gridView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
        selectedItems.clear();
        unPimpButton(resetSelection);
    }

    public DataManager getManager() {
        return manager;
    }


    @Override
    public void responseError(int code, String message) {
        if (code == 403)
            askForToken(true);
        else
            new ErrorPopup(this).setUpErrorPopup(message);
    }

    @Override
    public void updateGrid() {
        setUpGridView();
    }

    @Override
    public void updateStatus() {
        writeStatus();
    }

    public void setGridColumnNumber(int people) {
        int orientation = this.getResources().getConfiguration().orientation;
        int numCols;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            //calculate number of colums. 7 people fit in one column
            numCols = (int) Math.ceil(people / 7.0);
        } else {
            //calculate number of colums. 4 people fit in one column
            numCols = (int) Math.ceil(people / 4.0);
        }
        gridView.setNumColumns(numCols);

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
