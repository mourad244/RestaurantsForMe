package com.hfad.restaurantsforme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategorieAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<> maplist =  new ArrayList<>();

    public CategorieAdapter(@NonNull Context context) {
        this.mContext = context;

    }

    @Override
    public int getCount() {
        return ;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

        convertView = layoutInflater.inflate(R.layout.layout_grid_item_categorie,parent, false);

        ImageView imageCategorie = convertView.findViewById(R.id.iv_categorie_image);
        TextView txtNom = convertView.findViewById(R.id.tv_categorie_nom);


        txtNom.setText(getItem(position).getNom());
        imageCategorie.setImageResource(getItem(position).getImage());

        return convertView;
    }
}
