package com.hfad.restaurantsforme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThirdActivity extends Activity {

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
            Bitmap icon1 = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.icon_restau);
            Bitmap icon2 = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.icon_restau);
            Bitmap icon3 = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.icon_restau);
            Bitmap icon4 = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.icon_restau);
            Restaurant restaurant1 = new Restaurant("mini world",DbBitmapUtility.getBytes(icon1),
                    "1","09:00","21:00",
                    "0513251435",34.254067,
                    -6.592709);

            Restaurant restaurant2 = new Restaurant("mini chicken",DbBitmapUtility.getBytes(icon2),
                    "2","09:00","23:00",
                    "0514951435",34.259726,
                    -6.593478);
            Restaurant restaurant3 = new Restaurant("pizza hut",DbBitmapUtility.getBytes(icon3),
                    "3","09:00","22:00",
                    "0515671435",34.257065,
                    -6.581346);
            Restaurant restaurant4 = new Restaurant("Macdonald",DbBitmapUtility.getBytes(icon4),
                    "4","07:00","23:00",
                    "0516671435",34.262264,
                    -6.595930);

            long restaurant1Id = db.createRestaurant(restaurant1);
            long restaurant2Id = db.createRestaurant(restaurant2);
            long restaurant3Id = db.createRestaurant(restaurant3);
            long restaurant4Id = db.createRestaurant(restaurant4);

            // add menu to each restaurant
            Food tacosPoulet  = new Food("Tacos poulet","tacos poulet",
                    30,restaurant1Id);
            Food tacosMixte = new Food("Tacos mixte","tacos poulet",
                    45,restaurant1Id);
            Food sandwichPoulet = new Food("Sandwich poulet","sandwich poulet",
                    25,restaurant2Id);
            Food paniniPoulet = new Food("Panini poulet", "panini poulet ",
                    30,restaurant2Id);
            Food pizzaMargaritta = new Food("Pizza margarita", "margaritta",
                    40,restaurant3Id);
            Food seffa = new Food("Seffa", "seffa", 30,restaurant3Id);
            Food sushi = new Food("Sushi","sushi", 50,restaurant4Id);

            // affecte categorie of foods to a restaurant
            //   long[] food_restaurant1 = {1,2,3,4,5,6};
            //   long[] food_restaurant2 = {1,4,5,6};
            //   long[] food_restaurant3 = {1,2,3};

            //  db.createRestaurant(restaurant1,food_restaurant1);
            //   db.createRestaurant(restaurant2,food_restaurant2);
            //  db.createRestaurant(restaurant3,food_restaurant3);
            //   db.createRestaurant(restaurant4,food_restaurant1);

            db.createFood(tacosPoulet);
            db.createFood(tacosMixte);
            db.createFood(sandwichPoulet);
            db.createFood(paniniPoulet);
            db.createFood(pizzaMargaritta);
            db.createFood(seffa);
            db.createFood(sushi);

            db.close();
        }

        Intent intent = getIntent();
        int id = intent.getIntExtra("foodCategorie_id",-1);

        db = new DatabaseHelper(getApplicationContext());

        List <Restaurant> restaurants ;
        restaurants = db.getFoodRestaurants(id);
        db.close();
        RestaurantAdapter restaurantAdapter = new RestaurantAdapter(this,R.layout.list_restaurant,restaurants);
        listView.setAdapter(restaurantAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(ThirdActivity.this,MapsActivity.class);

                db = new DatabaseHelper(getApplicationContext());
                long foodRestaurantId =  restaurants.get(position).getId();
                long restaurantId = db.getRestaurantId(foodRestaurantId);

                intent1.putExtra("restaurantId",restaurantId);
                startActivity(intent1);
            }
        });
    }

}
