package com.hfad.restaurantsforme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import java.util.List;

import butterknife.BindView;

public class SecondActivity extends AppCompatActivity {
    public static String  PREFS_NAME="mypre";

    // database helper
    DatabaseHelper db;

    List<CategorieFood> categorieFoodList;
    CategorieAdapter categorieAdapter;

    @BindView(R.id.gvFood)
    GridView gridView;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        db = new DatabaseHelper(getApplicationContext());

        // Creating categorie of food
        CategorieFood pizza = new CategorieFood("Pizza",R.drawable.pizza_icon);
        CategorieFood fastFood = new CategorieFood("Fast Food",R.drawable.fastfood_icon);
        CategorieFood tacos = new CategorieFood("Tacos",R.drawable.tacos_icon);
        CategorieFood marocain = new CategorieFood("Marocain",R.drawable.marocain_icon);
        CategorieFood asiatique = new CategorieFood("Asiatique",R.drawable.asiatique_icon);
        CategorieFood italien = new CategorieFood("Italien",R.drawable.italien_icon);

        // Inserting categorie of food in db

        db.createCategorieFood(pizza);
        db.createCategorieFood(fastFood);
        db.createCategorieFood(tacos);
        db.createCategorieFood(marocain);
        db.createCategorieFood(asiatique);
        db.createCategorieFood(italien);

        Log.d("Categorie count","Categorie Count: " + db.getAllCategorieFoods().size());

        categorieFoodList = db.getAllCategorieFoods();
        db.closeDB();
        categorieAdapter = new CategorieAdapter(this,
                R.layout.layout_grid_item_categorie,
                categorieFoodList);
        gridView.setAdapter(categorieAdapter);

    }



    public void logout(View view){
        SharedPreferences sharedPrefs =getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.apply();

        //show login form
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
