package com.BetterTogether.app;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import DB.RewardType;


public class RewardPopup {

    private UserListFragment rewardStatus;
    private LayoutInflater layoutInflater;
    private AlertDialog.Builder alertBuilder;


    public RewardPopup(UserListFragment userListFragment) {
        rewardStatus = userListFragment;
        layoutInflater = (LayoutInflater) rewardStatus.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        alertBuilder = new AlertDialog.Builder(rewardStatus.getContext());

    }

    public void whistle(RewardType rewardType) {
        View popup = layoutInflater.inflate(R.layout.reward_popup_layout, null);
        alertBuilder.setView(popup);

        //MediaPlayer cakeSound = MediaPlayer.create(RewardPopup.this, R.raw.cake_sound);
        //cakeSound.start();

        if (rewardType == RewardType.CAKE) {
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

    public void claimReward(RewardType rewardType) {
        View claim_popup = layoutInflater.inflate(R.layout.claim_reward_layout, null);
        alertBuilder.setView(claim_popup);

        alertBuilder.setMessage("Do you want to claim a " + rewardType.toString() + " event?");

        alertBuilder.setPositiveButton(R.string.yes, (dialog, id) -> {
            rewardStatus.getManager().setUseVariableToTrue(rewardType);
            Toast confirmMsg = Toast.makeText(rewardStatus.getContext(), "You have claimed " + rewardType.toString(), Toast.LENGTH_SHORT);
            confirmMsg.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
            confirmMsg.show();
            dialog.dismiss();
        });

        alertBuilder.setNegativeButton(R.string.cancel, (dialog, id) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = alertBuilder.create();
        dialog.show();
    }

}
