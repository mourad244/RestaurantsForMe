package com.hfad.restaurantsforme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class SecondActivity extends AppCompatActivity {
    public static String  PREFS_NAME="mypre";

    String user;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_second);

    }

    public void logout(View view){
        SharedPreferences sharedPrefs =getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.apply();
        user="";
        //show login form
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
