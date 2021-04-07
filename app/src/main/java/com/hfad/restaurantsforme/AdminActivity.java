package com.hfad.restaurantsforme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends Activity {

    LinearLayout layout;
    EditText filteredRestaurant;
    ListView listFiltered;
    DatabaseHelper db;
    List<Restaurant> restaurantList;
    Button formFoodBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    public void formRestaurant(View v) {
        Intent intent = new Intent(AdminActivity.this,FormRestaurant.class);
        startActivity(intent);
    }


    public void searchRestaurant(View v){
        layout = findViewById(R.id.searchingRestau);
        if(layout.getVisibility() == View.GONE){
            layout.setVisibility(View.VISIBLE);
        } else layout.setVisibility(View.GONE);

        filteredRestaurant = findViewById(R.id.searchRestaurant);

        filteredRestaurant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                formFoodBtn = findViewById(R.id.ajouterFood);
                listFiltered = findViewById(R.id.filteredRestau);
                if (listFiltered.getVisibility() == View.GONE &&
                        formFoodBtn.isClickable()){

                    listFiltered.setVisibility(View.VISIBLE);
                    formFoodBtn.setClickable(false);
                }

                List<Restaurant> listFilteredRestaurants = searching(filteredRestaurant
                        .getText().toString());
                List listNomFilteredRestau = new ArrayList();

                for (Restaurant restaurant: listFilteredRestaurants
                ) {
                    listNomFilteredRestau.add(restaurant.getId()+". "+ restaurant.getNom());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter(AdminActivity.this,
                        android.R.layout.simple_list_item_1,listNomFilteredRestau);
                listFiltered.setAdapter(adapter);
                listFiltered.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String[] restaurant = adapter.getItem(position).split("\\.");
                        String nomRrestaurant = restaurant[1].trim();
                        filteredRestaurant.setText(nomRrestaurant);
                        listFiltered.setVisibility(View.GONE);
                        formFoodBtn.setClickable(true);
                        formFoodBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(AdminActivity.this,FormFood.class);
                                int restaurantId = Integer.parseInt(restaurant[0]);
                                intent.putExtra("restaurantId",restaurantId);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    private List<Restaurant> searching(String text){
        db= new DatabaseHelper(getApplicationContext());
        List<Restaurant> filteredRestaurants = new ArrayList();
        boolean founded;
        restaurantList = db.getAllRestaurants();

        for(int i =0; i < restaurantList.size();i++){
            founded = restaurantList.get(i).getNom().toLowerCase().contains(text);
            if (founded) filteredRestaurants.add(restaurantList.get(i));
        }
        return filteredRestaurants;
    }
}
