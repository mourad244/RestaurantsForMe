package com.hfad.restaurantsforme;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;

public class RestaurantAdapter extends ArrayAdapter<Restaurant> {
    private  Context mContext;
    private int mResource;

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

        image.setImageResource(getItem(position).getImage());
        nom.setText(getItem(position).getNom());

        // status (calculating hour)
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

        return convertView;
    }
}
