package com.hfad.restaurantsforme;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {
    public static String  PREFS_NAME="mypre";

    // database helper
    DatabaseHelper db;

    List<CategorieFood> categorieFoodList;
    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        db = new DatabaseHelper(getApplicationContext());

        // Check if table categorieFood is empty to add categorie of food
        if(db.getAllCategorieFoods().isEmpty()){
            // Creating categorie of food
            Bitmap pizzaMap = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.pizza_icon);
            Bitmap fastFoodMap = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.fastfood_icon);
            Bitmap tacosMap = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.tacos_icon);
            Bitmap marocainMap = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.marocain_icon);
            Bitmap asiatiqueMap = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.asiatique_icon);
            Bitmap italienMap = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.italien_icon);

            CategorieFood pizza = new CategorieFood("Pizza",
                    DbBitmapUtility.getBytes(pizzaMap));
            CategorieFood fastFood = new CategorieFood("Fast Food",
                    DbBitmapUtility.getBytes(fastFoodMap));
            CategorieFood tacos = new CategorieFood("Tacos",
                    DbBitmapUtility.getBytes(tacosMap));
            CategorieFood marocain = new CategorieFood("Marocain",
                    DbBitmapUtility.getBytes(marocainMap));
            CategorieFood asiatique = new CategorieFood("Asiatique",
                    DbBitmapUtility.getBytes(asiatiqueMap));
            CategorieFood italien = new CategorieFood("Italien",
                    DbBitmapUtility.getBytes(italienMap));

            // Inserting categorie of food in db

            db.createCategorieFood(pizza);
            db.createCategorieFood(fastFood);
            db.createCategorieFood(tacos);
            db.createCategorieFood(marocain);
            db.createCategorieFood(asiatique);
            db.createCategorieFood(italien);
        }

        // check if table tagfood is empty to add tag of foods
        if(db.getAllTagFoods().isEmpty()){
            TagFood sandwich = new TagFood("sandwich",2);
            TagFood hamberger = new TagFood("hamberger",2);
            TagFood panini = new TagFood("panini",2);
            TagFood pizza = new TagFood("pizza",1);
            TagFood tacos = new TagFood("tacos",3);
            TagFood seffa = new TagFood("seffa",4);
            TagFood rfissa = new TagFood("rfissa",4);
            TagFood couscous = new TagFood("couscous",4);
            TagFood marocain = new TagFood("marocain",4);
            TagFood harira = new TagFood("harira",4);
            TagFood tajine = new TagFood("tajine",4);
            TagFood sushi = new TagFood("sushi",5);
            TagFood aromaki = new TagFood("aromaki",5);
            TagFood saumon = new TagFood("saumon",5);
            TagFood sishmi = new TagFood("sishimi",5);
            TagFood pasta = new TagFood("pasta",6);

            List tags = new ArrayList();
            tags.add(sandwich);
            tags.add(hamberger);
            tags.add(panini);
            tags.add(pizza);
            tags.add(tacos);
            tags.add(seffa);
            tags.add(rfissa);
            tags.add(couscous);
            tags.add(harira);
            tags.add(tajine);
            tags.add(sushi);
            tags.add(aromaki);
            tags.add(saumon);
            tags.add(sishmi);
            tags.add(pasta);
            tags.add(marocain);

            // Inserting tag food in db
            db.createTagFood(tags);
            db.closeDB();
        }

//        Log.d("Categorie count","Categorie Count: " + db.getAllCategorieFoods().size());
        db = new DatabaseHelper(getApplicationContext());
        categorieFoodList = db.getAllCategorieFoods();
        gridView = findViewById(R.id.gvFood);
        CategorieAdapter categorieAdapter = new CategorieAdapter(categorieFoodList,this);
        gridView.setAdapter(categorieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent2 = new Intent(SecondActivity.this,ThirdActivity.class);
                int categorieFood_id = categorieFoodList.get(position).getId();
                intent2.putExtra("foodCategorie_id",categorieFood_id);
                startActivity(intent2);
            }
        });
    }

    public void login(View view){


        //show login form
        Intent intent=new Intent(this, AuthenActivity.class);
        startActivity(intent);
    }
   /* public void logout(View view){
        SharedPreferences sharedPrefs =getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.apply();

        //show login form
        Intent intent=new Intent(this, Authentification.class);
        startActivity(intent);
    }*/


}