package com.hfad.restaurantsforme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

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
                    "1","09:00","21:00",
                    "0514251435",R.drawable.qr_code_ex,
                    -6.592709,34.254067);

            Restaurant restaurant2 = new Restaurant("mini chicken",R.drawable.icon_restau,
                    "2","09:00","23:00",
                    "0514951435",R.drawable.qr_code_ex,
                    -6.593478,34.259726);
            Restaurant restaurant3 = new Restaurant("pizza hot",R.drawable.icon_restau,
                    "3","09:00","22:00",
                    "0515671435",R.drawable.qr_code_ex,
                    -6.581346,34.257065);
            Restaurant restaurant4 = new Restaurant("Macdonald",R.drawable.icon_restau,
                    "4","07:00","23:00",
                    "0515671435",R.drawable.qr_code_ex,
                    -6.595930,34.262264);

            long[] food_restaurant1 = {1,2,3,4,5,6};
            long[] food_restaurant2 = {1,4,5,6};
            long[] food_restaurant3 = {1,2,3};


            db.createRestaurant(restaurant1,food_restaurant1);
            db.createRestaurant(restaurant2,food_restaurant2);
            db.createRestaurant(restaurant3,food_restaurant3);
            db.createRestaurant(restaurant4,food_restaurant1);

            Log.d("restaurant count","Restaurants Count: " + db.getAllRestaurants().size());
            db.close();
        }


        Intent intent = getIntent();
        int id = intent.getIntExtra("foodCategorie_id",1);

        db = new DatabaseHelper(getApplicationContext());

        List <Restaurant> restaurants ;
        restaurants = db.getFoodRestaurants(id);
        RestaurantAdapter restaurantAdapter = new RestaurantAdapter(this,R.layout.list_restaurant,restaurants);
        listView.setAdapter(restaurantAdapter);
    }
}
