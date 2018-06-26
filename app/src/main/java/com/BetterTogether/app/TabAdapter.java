package com.BetterTogether.app;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabAdapter extends FragmentStatePagerAdapter {

    private UserListFragment userListFragment;
    private GraphFragment graphFragment;

    private static final int NUM_TABS = 2;
    private static final String[] TAB_TITLES = {"User list", "Graph"};

    public TabAdapter(FragmentManager fm, UserListFragment userListFragment, GraphFragment graphFragment) {
        super(fm);
        this.userListFragment = userListFragment;
        this.graphFragment = graphFragment;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return userListFragment;
            case 1:
                return graphFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }
}
