package com.bettertogether.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Overview extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //starts userListFragment-view that is our default display.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

    }


}
