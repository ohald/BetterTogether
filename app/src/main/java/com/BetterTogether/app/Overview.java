package com.BetterTogether.app;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import DB.DatabaseThreadHandler;
import DB.SQLiteDB;


public class Overview extends AppCompatActivity {

    private SQLiteDB db;
    private DatabaseThreadHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDBWithHandler();
        setContentView(R.layout.activity_overview);

        setSupportActionBar(findViewById(R.id.toolbar));

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