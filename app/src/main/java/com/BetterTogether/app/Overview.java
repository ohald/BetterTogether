package com.BetterTogether.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    }


    private void createDBWithHandler(){
        db = SQLiteDB.getInstance(this);
        handler = new DatabaseThreadHandler(this);
    }


}
