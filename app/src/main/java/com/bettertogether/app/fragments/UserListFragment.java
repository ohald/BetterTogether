package com.bettertogether.app.fragments;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
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
import java.util.Stack;

import db.RewardType;

import com.bettertogether.app.Pair;
import com.bettertogether.app.Person;


public class UserListFragment extends Fragment implements DataUpdateListener {
    private ArrayList<Integer> selectedItems;

    private DataManager manager;
    private GridView gridView;

    private int selectionColor;
    private int pimpedButtonColor;
    private Stack<Pair> undoStack;
    private Button undo;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_list, container, false);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        selectedItems = new ArrayList<>();
        undoStack = new Stack<>();

        int energyRed = getResources().getColor(R.color.energyRed);
        selectionColor = energyRed;
        pimpedButtonColor = energyRed;

        askForToken(false);

        gridView = getView().findViewById(R.id.user_list);
        selectedItems = new ArrayList<>();

        refreshLayout = getView().findViewById(R.id.swiperefresh);
        refreshLayout.setOnRefreshListener(() -> manager.updateActiveUsers());

        //creates load symbol on startup
        refreshLayout.setRefreshing(true);

        undo = getView().findViewById(R.id.undo_pair_button);
        undo.setOnClickListener(btn -> {
            if (!undoStack.isEmpty()) {
                Pair p = undoStack.pop();
                showToast(p.getPerson1() + " & " + p.getPerson2() + " undone");
                updateStatus();
            }
            if(undoStack.isEmpty())
                unPimpButton(undo);
        });

    }

    private void showToast(String s){
        Toast t = Toast.makeText(getContext(), s, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0,8);
        t.show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //nothing is fetched from DB yet.
        if(manager == null){
            return;
        }

        //set number of columns in grid
        int num = manager.getActiveUsers().size();
        setGridColumnNumber(num);

        //deselect on screen orientation change
        selectedItems.clear();


    }

    private void selectItemAtPosition(int position) {
        if (selectedItems.contains(position)) {
            gridView.getChildAt(position).setBackgroundColor(Color.TRANSPARENT);
            selectedItems.remove(selectedItems.indexOf(position));
            return;
        }

        if (selectedItems.size() >= 2) return;

        selectedItems.add(position);
        gridView.getChildAt(position).setBackgroundColor(selectionColor);

        if (selectedItems.size() >= 2)
            createPair();
    }

    private void pimpButton(Button button) {
        button.getBackground().setColorFilter(pimpedButtonColor, PorterDuff.Mode.MULTIPLY);
    }

    @SuppressLint("CheckResult")
    private void createPair() {
        if (selectedItems.size() < 2) {
            showToast("You need to select two users for pair programming");
            return;
        }

        Pair pair = new Pair(
                manager.getActiveUsers().get(selectedItems.get(0)).getUsername(),
                manager.getActiveUsers().get(selectedItems.get(1)).getUsername());

        undoStack.push(pair);
        pimpButton(undo);
        addPairIfNotUndone(pair);
        resetSelectedWithDelay();
        updateStatus();

        showToast("Added pair programming with: " + pair.getPerson1() +
                        " and " + pair.getPerson2());
    }

    // Used to show the selection of the second person
    // for a brief time, before deselecting both on add.
    private void resetSelectedWithDelay(){
        new Handler().postDelayed(this::resetSelectedPersons, 500);

    }

    private void addPairIfNotUndone(Pair p) {
        new Handler().postDelayed(() -> {
            if (undoStack.contains(p)) {
                manager.addPair(p);
                undoStack.remove(p);
                if(undoStack.isEmpty())
                    unPimpButton(undo);
            }
        }, 5000);
    }

    @Override
    public void rewardReached(RewardType type) {
        new RewardPopup(this).whistle(type);
    }

    @Override
    public void updateStatus() {
        TextView lastPair = getView().findViewById(R.id.last_event);

        Pair p;
        if(!undoStack.isEmpty()){
            p = undoStack.peek();
            lastPair.setText(p.getPerson1() + " & " + p.getPerson2());
        } else if (!manager.getAllPairs().isEmpty()) {
            p = manager.getAllPairs().get(manager.getAllPairs().size()-1);
            lastPair.setText(p.getPerson1() + " & " + p.getPerson2());
        }
    }

    private void unPimpButton(Button button) {
        button.getBackground().clearColorFilter();
    }


    private void resetSelectedPersons() {
        for (Integer i : selectedItems)
            gridView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
        selectedItems.clear();
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
        List<Person> persons = manager.getActiveUsers();
        refreshLayout.setRefreshing(false);

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
