package com.bettertogether.app.dialogs;

import android.app.AlertDialog;
import com.bettertogether.app.fragments.UserListFragment;

public class ErrorPopup extends PopupView {
    public ErrorPopup(UserListFragment userListFragment) {
        super(userListFragment);
    }

    public void setUpErrorPopup(String message){
        alertBuilder
                .setMessage(message)
                .setTitle("ERROR");

        alertBuilder.setPositiveButton("OK", (dialogInterface, i) -> {}
                );

        AlertDialog dialog = alertBuilder.create();
        dialog.show();
    }
}
