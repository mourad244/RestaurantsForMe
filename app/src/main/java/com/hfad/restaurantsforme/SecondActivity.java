package com.hfad.restaurantsforme;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import java.util.List;
import butterknife.BindView;

public class SecondActivity extends AppCompatActivity {
    public static String  PREFS_NAME="mypre";

    // database helper
    DatabaseHelper db;

    List<CategorieFood> categorieFoodList;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        db = new DatabaseHelper(getApplicationContext());

        // Check if table categorieFood is empty to add categorie of food
        if(db.getAllCategorieFoods().isEmpty()){
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
        }


//        Log.d("Categorie count","Categorie Count: " + db.getAllCategorieFoods().size());

        categorieFoodList = db.getAllCategorieFoods();
        GridView gridView = (GridView) findViewById(R.id.gvFood);
        CategorieAdapter categorieAdapter = new CategorieAdapter(categorieFoodList,this);
        gridView.setAdapter(categorieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GridView gridView2 = (GridView) findViewById(R.id.gvFood);


                Intent intent2 = new Intent(SecondActivity.this,ThirdActivity.class);
                int categorieFood_id = categorieFoodList.get(position).getId();
                intent2.putExtra("foodCategorie_id",categorieFood_id);
                startActivity(intent2);
            }
        });
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