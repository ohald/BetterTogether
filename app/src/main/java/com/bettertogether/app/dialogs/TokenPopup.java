package com.bettertogether.app.dialogs;

import android.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.bettertogether.app.fragments.UserListFragment;
import com.bettertogether.app.R;

public class TokenPopup extends PopupView {

    public TokenPopup(UserListFragment userListFragment) {
        super(userListFragment);
    }

    public void setUpGetTokenView(String message){
        alertBuilder
                .setMessage(message)
                .setTitle("Access token");

        View view = layoutInflater.inflate(R.layout.token_popup, null);
        alertBuilder.setView(view)
                .setPositiveButton("Confirm", (dialogInterface, i) -> {
                    EditText tokenInput = view.findViewById(R.id.token);

                    //notify userListFragment, which acts as listener
                    userListFragment.tokenReceived(tokenInput.getText().toString());
                });


        AlertDialog dialog = alertBuilder.create();
        dialog.show();
    }

}
