package com.hfad.restaurantsforme;

import android.app.Activity;
import android.content.Intent;
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
            Restaurant restaurant1 = new Restaurant("mini world",R.drawable.icon_restau,
                    "commander votre plat","09:00","22:00",
                    "0514251435",R.drawable.qr_code_ex,"34.76876",
                    "-6.6876870");

            Restaurant restaurant2 = new Restaurant("mini chiken",R.drawable.icon_restau,
                    "commander","09:00","22:00",
                    "0514951435",R.drawable.qr_code_ex,"34.7667",
                    "-6.6876345");
            Restaurant restaurant3 = new Restaurant("pizza hot",R.drawable.icon_restau,
                    "commander votre plat","09:00","22:00",
                    "0515671435",R.drawable.qr_code_ex,"34.23876",
                    "-6.6686870");

            long[] food_restaurant1 = {1,3};
            long[] food_restaurant2 = {2,4};
            long[] food_restaurant3 = {1,5};

            db.createRestaurant(restaurant1,food_restaurant1);
            db.createRestaurant(restaurant2,food_restaurant2);
            db.createRestaurant(restaurant3,food_restaurant3);
        }

        Intent intent = getIntent();

        int number = intent.getIntExtra("")

        db.getFoodRestaurants()

    }
}
