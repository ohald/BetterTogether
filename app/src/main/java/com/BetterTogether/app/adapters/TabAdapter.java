package com.BetterTogether.app.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.BetterTogether.app.Fragments.GraphFragment;
import com.BetterTogether.app.Fragments.UserListFragment;

public class TabAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_TABS = 2;
    private static final String[] TAB_TITLES = {"User list", "Graph"};
    private UserListFragment userListFragment;
    private GraphFragment graphFragment;

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

    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }
}
