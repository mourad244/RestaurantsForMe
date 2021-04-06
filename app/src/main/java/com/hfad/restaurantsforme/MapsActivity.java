package com.hfad.restaurantsforme;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.hfad.restaurantsforme.directionhelpers.FetchURL;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hfad.restaurantsforme.directionhelpers.TaskLoadedCallback;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private MarkerOptions placeOrigin, placeDestination;
    private GoogleMap mMap;
    private LatLng mOrigin;
    private LatLng mDestination;
    private Polyline currentPolyline;
    PolylineOptions lineOptions;
    ArrayList<LatLng> points;
    LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient2;
    GoogleApiClient mGoogleApiClient;
    Marker m ;

    boolean startTrack = false;
    // distance and duration;
    TextView distanceDuration;

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


    public void drawRoute(View view) {

        // Getting URL to the Google Directions API
        String url = getUrl(mOrigin, mDestination);
        Log.d("onMapClick", url.toString());
        FetchUrl FetchUrl = new FetchUrl();

        // Start downloading json data from Google Directions API
        FetchUrl.execute(url);
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mOrigin));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        startTrack = true;
    }





    private String getUrl(LatLng origin, LatLng dest) {


        //  String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters + "&key=" + MY_API_KEY
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters
                + "&key=" + "AIzaSyB7r-0tHNUGgnWbljMlY5AvRej9E5R4ZIE";


        return url;
    }

    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.e("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DirectionParser parser = new DirectionParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            //  ArrayList<LatLng> points;
            lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
        mGoogleApiClient2.disconnect();
    }



    public void rotateMarker(final Marker marker, final float toRotation, final float st) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final float startRotation = st;
        final long duration = 1555;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);

                float rot = t * toRotation + (1 - t) * startRotation;

                marker.setRotation(-rot > 180 ? rot / 2 : rot);
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    public void animateMarker(final LatLng toPosition, final boolean hideMarke) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(m.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 5000;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                m.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarke) {
                        m.setVisible(false);
                    } else {
                        m.setVisible(true);
                    }
                }
            }
        });
    }

    private double bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }
}