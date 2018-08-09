package com.bettertogether.app.dialogs;

import android.app.AlertDialog;
import android.view.View;

import com.bettertogether.app.fragments.UserListFragment;
import com.bettertogether.app.R;

import db.RewardType;



public class RewardPopup extends PopupView {

    public RewardPopup(UserListFragment userListFragment) {
        super(userListFragment);
    }

    public void whistle(RewardType rewardType) {
        View popup = layoutInflater.inflate(R.layout.reward_popup_layout, null);
        alertBuilder.setView(popup);

        if (rewardType == RewardType.CAKE) {
            alertBuilder.setTitle("IT IS CAKE TIME!!");
            popup.setBackgroundResource(R.drawable.cake_picture);

        } else {
            alertBuilder.setTitle("IT IS PIZZA TIME!!");
            popup.setBackgroundResource(R.drawable.pizza_and_beer);
        }

        alertBuilder.setPositiveButton(R.string.ok, (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = alertBuilder.create();
        dialog.show();

    }

}
