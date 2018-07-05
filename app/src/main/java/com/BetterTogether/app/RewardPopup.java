package com.BetterTogether.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import DB.RewardType;


public class RewardPopup {

    private UserListFragment rewardStatus;


    public RewardPopup(UserListFragment userListFragment) {
        rewardStatus = userListFragment;
    }

    public void whistle(RewardType rewardType) {
        LayoutInflater inflater = (LayoutInflater) rewardStatus.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.reward_popup_layout, null);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(rewardStatus.getContext());
        alertBuilder.setView(popup);

        //MediaPlayer cakeSound = MediaPlayer.create(RewardPopup.this, R.raw.cake_sound);
        //cakeSound.start();

        if (rewardType == RewardType.CAKE){
            alertBuilder.setTitle("IT IS CAKE TIME!!");
            popup.setBackgroundResource(R.drawable.cake_picture);

        } else {
            alertBuilder.setTitle("IT IS PIZZA TIME!!");
            popup.setBackgroundResource(R.drawable.pizza_and_beer);
        }

        alertBuilder.setPositiveButton(R.string.ok, (dialog, id) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = alertBuilder.create();
        dialog.show();

    }

}
