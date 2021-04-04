package com.hfad.restaurantsforme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class FormRestaurant extends Activity {
    private static final int GALLERY_REQUEST = 1;
    DatabaseHelper db;
    EditText nomRestau, descriptionRestau,ouvertureRestau, fermetureRestau ,telephoneRestau;
    EditText latitudeRestau, longitudeRestau;
    Button uploadImage, uploadQRCode, validerRestau;
    ImageView imageRestau, imageQRCode;
    Boolean firstImage, secondImage = false;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_form_restaurant);
        // upload image restaurant
        uploadImage = findViewById(R.id.uploadBtn);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                firstImage = true;
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }

        });
        // upload image QRCode
        uploadQRCode = findViewById(R.id.uploadQRCode);
        uploadQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                secondImage = true;
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }

        });

        // bind edittext with values in classe Food
        nomRestau = findViewById(R.id.txtNom);
        descriptionRestau = findViewById(R.id.txtDescription);
        ouvertureRestau = findViewById(R.id.txtHOuverture);
        fermetureRestau = findViewById(R.id.txtHFermeture);
        telephoneRestau = findViewById(R.id.txtTelephone);
        latitudeRestau = findViewById(R.id.txtLatitude);
        longitudeRestau = findViewById(R.id.txtLongitude);

        validerRestau = findViewById(R.id.validerRestaurant);
        validerRestau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Restaurant restaurant = new Restaurant(nomRestau.getText().toString(),
                        R.id.imageRestau,
                        descriptionRestau.getText().toString(),ouvertureRestau.getText().toString(),
                        fermetureRestau.getText().toString(),
                        telephoneRestau.getText().toString(),R.id.imageQRCode,
                        Double.parseDouble(latitudeRestau.getText().toString()),
                        Double.parseDouble(longitudeRestau.getText().toString()));
                db= new DatabaseHelper(getApplicationContext());
                db.createRestaurant(restaurant);
                Intent intent = new Intent(FormRestaurant.this,AdminActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                if(firstImage){
                    imageRestau = findViewById(R.id.imageRestau);
                    imageRestau.setImageBitmap(selectedImage);
                    firstImage = false;
                }
                if(secondImage){
                    imageQRCode = findViewById(R.id.imageQRCode);
                    imageQRCode.setImageBitmap(selectedImage);
                    secondImage = false;
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(FormRestaurant.this, "Something went wrong",
                        Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(FormRestaurant.this, "You haven't picked Image",
                    Toast.LENGTH_LONG).show();
        }
    }
}
