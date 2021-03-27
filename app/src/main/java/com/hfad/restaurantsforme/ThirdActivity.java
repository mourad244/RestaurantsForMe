package com.hfad.restaurantsforme;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThirdActivity extends Activity {
    // Array of restaurants objects

    // database helper
    DatabaseHelper db;

    @BindView(R.id.listView)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        ButterKnife.bind(this);

        //create restaurant
        db = new DatabaseHelper(getApplicationContext());

        // Check if table restaurant is empty to add restaurant
        if(db.getAllRestaurants().isEmpty()){
            // Create restaurant
            Restaurant restaurant1 = new Restaurant("mini world",R.drawable.)
        }

    }
}
