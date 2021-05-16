package com.hfad.restaurantsforme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ThirdActivity extends Activity {

    Retrofit retrofit = RetrofitFactory.getRetrofit();
    RetrofitServices retrofitServices = retrofit
            .create(RetrofitServices.class);

    RestaurantAdapter restaurantAdapter;
    List <Restaurant> restaurants ;

    @BindView(R.id.listView)
    ListView listView;

    EditText filteredRestaurant;
    ListView listFiltered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

    }

    @Override
    public void onResume(){
        super.onResume();
        ButterKnife.bind(this);
        filteredRestaurant = findViewById(R.id.search);
        Intent intent = getIntent();
        String id = intent.getStringExtra("foodCategorie_id");



        Call<List<Restaurant>> call = retrofitServices.getRestaurantsByCategorie(id);

        call.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {

                restaurants = response.body();

                restaurantAdapter = new RestaurantAdapter(ThirdActivity.this,R.layout.list_restaurant,restaurants);
                listView.setAdapter(restaurantAdapter);
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {

            }
        });
        filteredRestaurant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                List<Restaurant> listFilteredRestaurants = searching(filteredRestaurant
                        .getText().toString());
                if (filteredRestaurant.getText() != null){

                    restaurantAdapter = new RestaurantAdapter(
                            ThirdActivity.this,R.layout.list_restaurant,listFilteredRestaurants);
                    listView.setAdapter(restaurantAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(ThirdActivity.this,MapsActivity.class);

                String restaurant_id = restaurants.get(position).getId();
                intent1.putExtra("restaurant_id",restaurant_id);
                startActivity(intent1);
            }
        });
    }

    private List<Restaurant> searching(String text){
        List<Restaurant> filteredRestaurants = new ArrayList();
        boolean founded;

        for(int i =0; i < restaurants.size();i++){
            founded = restaurants.get(i).getNom().toLowerCase().contains(text.toLowerCase());
            if (founded) filteredRestaurants.add(restaurants.get(i));
        }
        return filteredRestaurants;
    }

}
