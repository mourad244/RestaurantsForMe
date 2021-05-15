package com.hfad.restaurantsforme;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SecondActivity extends AppCompatActivity {
    public static String  PREFS_NAME="mypre";

    Retrofit retrofit = RetrofitFactory.getRetrofit();
    RetrofitServices retrofitServices = retrofit
            .create(RetrofitServices.class);

    CategorieAdapter categorieAdapter;
    List<CategorieFood> categorieFoodList;

    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }
    @Override
    public void onResume(){
        super.onResume();
        gridView = findViewById(R.id.gvFood);

        Call<List<CategorieFood>> call = retrofitServices.getCategoriesFood();
        call.enqueue(new Callback<List<CategorieFood>>() {
            @Override
            public void onResponse(Call<List<CategorieFood>> call, Response<List<CategorieFood>> response) {
                categorieFoodList = response.body();
                categorieAdapter = new CategorieAdapter(categorieFoodList,SecondActivity.this);
                gridView.setAdapter(categorieAdapter);
            }

            @Override
            public void onFailure(Call<List<CategorieFood>> call, Throwable t) {

            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent2 = new Intent(SecondActivity.this,ThirdActivity.class);
                String categorieFood_id = categorieFoodList.get(position).getId();
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