package com.hfad.restaurantsforme;

import android.Manifest;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RestaurantAdapter extends ArrayAdapter<Restaurant>  {
    private  Context mContext;
    private int mResource;

    FusedLocationProviderClient fusedLocationProviderClient;


    public RestaurantAdapter(@NonNull Context context, int resource, @NonNull List<Restaurant> objects) {
        super(context, resource, objects);

        this.mContext=context;
        this.mResource =resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        convertView=layoutInflater.inflate(R.layout.list_restaurant,parent,false);
        ImageView image=  convertView.findViewById(R.id.image);
        TextView nom =convertView.findViewById(R.id.nom);
        TextView distance =convertView.findViewById(R.id.distance);
        TextView status = convertView.findViewById(R.id.status);

        DbBitmapUtility.setImageViewWithByteArray(image,getItem(position).getImage());

        nom.setText(getItem(position).getNom());

        // -----------------status (calculating hour)-------------------- ----
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int min = now.get(Calendar.MINUTE);

        String[] splitOuverture = getItem(position).getH_ouverture().split(":");
        int h_ouverture = Integer.parseInt(splitOuverture[0]);


        String[] splitFermeture = getItem(position).getH_fermeture().split(":");
        int h_fermeture = Integer.parseInt(splitFermeture[0]);

        if(hour > h_ouverture && hour < h_fermeture ){
            status.setText("Ouvert");
        }
        else if( hour == h_ouverture) {
            int m_ouverture = Integer.parseInt(splitOuverture[1]);
            int m_fermeture = Integer.parseInt(splitFermeture[1]);
            if (min > m_ouverture || min < m_fermeture)
                status.setText("Ouvert");
            else status.setText("Fermé");
        } else status.setText("Fermé");

        //-----------------****************---------------------

        // --------------- Distance ----------------------------

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);

        if (ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED ) {

            fusedLocationProviderClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {


                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            // Initialize location
                            Location location = task.getResult();
                            if(location != null){
                                // Initialize address
                                try {

                                    // Initialize geocoder
                                    Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

                                    List<Address> addresses = geocoder.getFromLocation(
                                            location.getLatitude(),
                                            location.getLongitude(),
                                            1
                                    );

                                    float[] results = new float[1];
                                    Location.distanceBetween(addresses.get(0).getLatitude(),
                                            addresses.get(0).getLongitude(),
                                            getItem(position).getLatitude(),
                                            getItem(position).getLongitude()
                                            ,results);

                                    if (results[0]>1000){
                                        distance.setText(String.format("%.2f",results[0]/1000)  +" Km");
                                    }
                                    else
                                    distance.setText((int)results[0]+" m");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
            } else{
            // when permission denid
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }
        return convertView;
    }
}
