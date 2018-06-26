package com.BetterTogether.app;

import android.os.Bundle;

import DB.DatabaseThreadHandler;
import DB.SQLiteDB;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;


public class Overview extends AppCompatActivity {

    private SQLiteDB db;
    private DatabaseThreadHandler handler;

    private UserListAdapter myAdapter;
    private ArrayList<Integer> clicked;
    private String[] testData = {"1", "2", "3", "4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDBWithHandler();
        setContentView(R.layout.activity_overview);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(), new UserListFragment(), new GraphFragment());
        ViewPager viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(tabAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.bringToFront();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void createDBWithHandler(){
        db = SQLiteDB.getInstance(this);
        handler = new DatabaseThreadHandler(this);
    }

}