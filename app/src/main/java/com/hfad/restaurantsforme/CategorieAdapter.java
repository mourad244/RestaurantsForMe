package com.hfad.restaurantsforme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategorieAdapter extends ArrayAdapter<Integer> {

    private final String noms[];
    private final int images[];
    Context mContext;

    public CategorieAdapter(String[] noms, int[] images, Context mContext) {
        super(mContext,R.layout.layout_grid_item_categorie);
        this.noms = noms;
        this.images = images;
        this.mContext = mContext;
    }






    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_grid_item_categorie, null);

        ImageView imageCategorie = (ImageView) view.findViewById(R.id.iv_categorie_image);
        TextView txtNom = (TextView) view.findViewById(R.id.tv_categorie_nom);

        imageCategorie.setImageResource(images[position]);
        txtNom.setText(noms[position]);

        return view;
    }
}
