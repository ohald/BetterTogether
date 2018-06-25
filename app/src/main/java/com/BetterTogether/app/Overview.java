package com.BetterTogether.app;

import android.graphics.Color;
import android.os.Bundle;
import DB.DatabaseThreadHandler;
import DB.SQLiteDB;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class Overview extends AppCompatActivity {

    private SQLiteDB db;
    private DatabaseThreadHandler handler;

    private MyAdapter myAdapter;
    private ArrayList<Integer> clicked;
    private String[] testData = {"1", "2", "3", "4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDBWithHandler();
        setContentView(R.layout.activity_overview);
        clicked = new ArrayList<>();
        myAdapter = new MyAdapter(this, testData);
        final ListView listView = findViewById(R.id.user_list);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (clicked.contains(i)) {
                    listView.getChildAt(i).setBackgroundColor(Color.WHITE);
                    clicked.remove(clicked.indexOf(i));
                } else {
                    if(clicked.size()<2) {
                        clicked.add(i);
                        listView.getChildAt(i).setBackgroundColor(Color.GREEN);
                    }
                }
            }
        });
    }

    private void createDBWithHandler(){
        db = SQLiteDB.getInstance(this);
        handler = new DatabaseThreadHandler(this);
    }


}