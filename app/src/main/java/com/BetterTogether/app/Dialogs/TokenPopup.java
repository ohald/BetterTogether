package com.BetterTogether.app.Dialogs;

import android.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.BetterTogether.app.Fragments.UserListFragment;
import com.BetterTogether.app.R;
import com.BetterTogether.app.TokenListener;

public class TokenPopup extends PopupView {

    private TokenListener listener;

    public TokenPopup(UserListFragment userListFragment, TokenListener listener) {
        super(userListFragment);
        this.listener = listener;
    }

    public void setUpGetTokenView(){
        alertBuilder
                .setMessage("You need an access token to access the database.")
                .setTitle("Access token");

        View view = layoutInflater.inflate(R.layout.token_popup, null);
        alertBuilder.setView(view)
                .setPositiveButton("Confirm", (dialogInterface, i) -> {
                    EditText tokenInput = view.findViewById(R.id.token);
                    Log.d("TOKEN", tokenInput.getText().toString());
                    listener.onTokenReceived(tokenInput.getText().toString());
                });


        AlertDialog dialog = alertBuilder.create();
        dialog.show();
    }

}
