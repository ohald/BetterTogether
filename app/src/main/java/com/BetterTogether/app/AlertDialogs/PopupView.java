package com.BetterTogether.app.AlertDialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;

import com.BetterTogether.app.UserListFragment;

public class PopupView {

    protected UserListFragment userListFragment;
    protected LayoutInflater layoutInflater;
    protected AlertDialog.Builder alertBuilder;


    public PopupView(UserListFragment userListFragment) {
        this.userListFragment = userListFragment;
        layoutInflater = (LayoutInflater) this.userListFragment.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        alertBuilder = new AlertDialog.Builder(this.userListFragment.getContext());

    }
}
