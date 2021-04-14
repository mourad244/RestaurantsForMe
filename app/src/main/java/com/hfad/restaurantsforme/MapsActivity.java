package com.hfad.restaurantsforme;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private MarkerOptions placeDestination, placeOrigin;
    private GoogleMap mMap;
    private LatLng mOrigin;

    FusedLocationProviderClient fusedLocationProviderClient;
    // Itineraire button
    Button btnItineraire;

    // Call button
    Button btnAppeler;

    // database helper
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        Intent intent = getIntent();
        long restaurantId = intent.getLongExtra("restaurantId", -1);

        db = new DatabaseHelper(getApplicationContext());

        Restaurant restaurant = db.getRestaurant(restaurantId);

        // Add a marker in Sydney and move the camera
        LatLng position = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());

        placeDestination = new MarkerOptions().position(position).title(restaurant.getNom());
        mMap.addMarker(placeDestination.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))).showInfoWindow();
        float zoomLevel = 14.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoomLevel));


        // appeler restaurant
        btnAppeler = findViewById(R.id.appeler);
        btnAppeler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri nTelephone = Uri.parse("tel: " + restaurant.getTelephone());
                Intent intent = new Intent(Intent.ACTION_DIAL,nTelephone);

                startActivity(intent);
            }
        });

        // afficher localisation
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
                MapsActivity.this);

        if (ActivityCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        // Initialize location
                        Location location = task.getResult();
                        if (location != null) {
                            // Initialize address
                            try {

                                // Initialize geocoder
                                Geocoder geocoder = new Geocoder(MapsActivity.this,
                                        Locale.getDefault());

                                List<Address> addresses = geocoder.getFromLocation(
                                        location.getLatitude(),
                                        location.getLongitude(),
                                        1
                                );


                                mOrigin = new LatLng(addresses.get(0).getLatitude(),
                                        addresses.get(0).getLongitude());

                                // getting the direction

                                mMap.getUiSettings().setZoomControlsEnabled(true);
                                if (ActivityCompat
                                        .checkSelfPermission(MapsActivity.this,
                                                Manifest.permission.ACCESS_FINE_LOCATION)
                                        != PackageManager.PERMISSION_GRANTED
                                        && ActivityCompat
                                        .checkSelfPermission(MapsActivity.this,
                                                Manifest.permission.ACCESS_COARSE_LOCATION)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                mMap.setMyLocationEnabled(true);
                                mMap.getUiSettings().setMyLocationButtonEnabled(true);

                                // Show marker on the screen and adjust the zoom level
                                //mMap.addMarker(new MarkerOptions().position(mOrigin)
                                //        .title("Origin"));

                                // code to have the zoom adapted to the distance between two points
                                        /*  float[] results = new float[1];
                                Location.distanceBetween(mDestination.latitude,
                                        mDestination.longitude,
                                        mOrigin.latitude,
                                        mOrigin.longitude
                                        ,results);
                                float zoom = results[0] * 3/115f;*/
//                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mOrigin,
//                                        14f));
                                // 580m ---> 15f
                                // distance ---> zoom

                                // Getting URL to the Google Directions API
                               


                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                });
        // afficher itineraire
        btnItineraire = findViewById(R.id.itineraire);


        /*btnItineraire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
                        MapsActivity.this);

                if (ActivityCompat.checkSelfPermission(MapsActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MapsActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                fusedLocationProviderClient.getLastLocation()
                        .addOnCompleteListener(new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                // Initialize location
                                Location location = task.getResult();
                                if (location != null) {
                                    // Initialize address
                                    try {

                                        // Initialize geocoder
                                        Geocoder geocoder = new Geocoder(MapsActivity.this,
                                                Locale.getDefault());

                                        List<Address> addresses = geocoder.getFromLocation(
                                                location.getLatitude(),
                                                location.getLongitude(),
                                                1
                                        );


                                        mOrigin = new LatLng(addresses.get(0).getLatitude(),
                                                addresses.get(0).getLongitude());
                                        mDestination = position;

                                        // getting the direction

                                        mMap.getUiSettings().setZoomControlsEnabled(true);
                                        if (ActivityCompat
                                                .checkSelfPermission(MapsActivity.this,
                                                Manifest.permission.ACCESS_FINE_LOCATION)
                                                != PackageManager.PERMISSION_GRANTED
                                                && ActivityCompat
                                                .checkSelfPermission(MapsActivity.this,
                                                Manifest.permission.ACCESS_COARSE_LOCATION)
                                                != PackageManager.PERMISSION_GRANTED) {
                                            // TODO: Consider calling
                                            //    ActivityCompat#requestPermissions
                                            // here to request the missing permissions, and then overriding
                                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                            //                                          int[] grantResults)
                                            // to handle the case where the user grants the permission. See the documentation
                                            // for ActivityCompat#requestPermissions for more details.
                                            return;
                                        }
                                        mMap.setMyLocationEnabled(true);
                                        mMap.getUiSettings().setMyLocationButtonEnabled(true);

                                        // Show marker on the screen and adjust the zoom level
                                        //mMap.addMarker(new MarkerOptions().position(mOrigin)
                                        //        .title("Origin"));

                                        // code to have the zoom adapted to the distance between two points
                                        *//*  float[] results = new float[1];
                                        Location.distanceBetween(mDestination.latitude,
                                                mDestination.longitude,
                                                mOrigin.latitude,
                                                mOrigin.longitude
                                                ,results);
                                        float zoom = results[0] * 3/115f;*//*
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mOrigin,
                                              14f));
                                        // 580m ---> 15f
                                        // distance ---> zoom

                                        // Getting URL to the Google Directions API
                                        placeOrigin = new MarkerOptions().position(mOrigin).title("Origine");
                                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                                .findFragmentById(R.id.map);

                                        mapFragment.getMapAsync(MapsActivity.this);


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }

                        });
                }
        });*/
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}