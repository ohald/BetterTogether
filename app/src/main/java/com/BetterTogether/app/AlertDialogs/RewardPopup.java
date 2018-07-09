package com.BetterTogether.app.AlertDialogs;

import android.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.BetterTogether.app.R;
import com.BetterTogether.app.UserListFragment;

import DB.RewardType;


public class RewardPopup extends PopupView {

    public RewardPopup(UserListFragment userListFragment) {
        super(userListFragment);
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
            userListFragment.setPopupIsActiveFalse();
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
            userListFragment.getManager().setUseVariableToTrue(rewardType);
            Toast confirmMsg = Toast.makeText(userListFragment.getContext(), "You have claimed " + rewardType.toString(), Toast.LENGTH_SHORT);
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
