package com.hfad.restaurantsforme;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthenActivity extends AppCompatActivity {

    public static String  PREFS_NAME="mypre";
    public static String PREF_USERNAME="username";
    public static String PREF_PASSWORD="password";

    @Nullable
    @BindView(R.id.sName)
    EditText Name;
    @BindView(R.id.sPassword)
    EditText Password;
    @Nullable
    @BindView(R.id.msgError)
    TextView messageError;
    /*@BindView(R.id.btnLogin) Button connecter;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);
        ButterKnife.bind(this);
        loadData();

        /*Name = (EditText)findViewById(R.id.sName);
        Password = (EditText)findViewById(R.id.sPassword);
        connecter = (Button)findViewById(R.id.btnLogin);*/
    }

    @OnClick(R.id.btnLogin)
    public void onClick(View v) {
        validate(Name.getText().toString(), Password.getText().toString());
    }

    private void  validate(String userName, String userPassword) {
        String username="mourad244";
        String password="1234";
        if ((userName.equals(username) && (userPassword.equals(password)))){
            CheckBox ch=(CheckBox)findViewById(R.id.ch_rememberme);
            if(ch.isChecked())
                rememberMe(username,password); //save username and password
            //show logout activity

            Intent  intent =new Intent(AuthenActivity.this, AdminActivity.class);
            startActivity(intent);
        }

        else if (Name.getText().toString().trim().equals("")){
            Name.setError("Enter Name");
        }

        else if (Password.getText().toString().trim().equals("")) {
            Password.setError("Enter Password");
        }

        else messageError.setVisibility(View.VISIBLE);
    }



    public void rememberMe(String user, String password){
        //save username and password in SharedPreferences
        getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                .edit()
                .putString(PREF_USERNAME,user)
                .putString(PREF_PASSWORD,password)
                .apply();
    }
    public void loadData(){
        Name.setText(getSharedPreferences(PREFS_NAME,MODE_PRIVATE).getString(PREF_USERNAME,""));
        Password.setText(getSharedPreferences(PREFS_NAME,MODE_PRIVATE).getString(PREF_PASSWORD,""));
//        if(Name!=null && Password!=null)
//            validate(Name.getText().toString(),Password.getText().toString());
    }
}