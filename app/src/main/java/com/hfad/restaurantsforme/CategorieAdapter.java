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

    private final String noms[];
    private final int images[];
    Context mContext;

    public CategorieAdapter(String[] noms, int[] images, Context mContext) {
        this.noms = noms;
        this.images = images;
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        return 0 ;
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
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_grid_item_categorie, null);

        ImageView imageCategorie = (ImageView) view.findViewById(R.id.iv_categorie_image);
        TextView txtNom = (TextView) view.findViewById(R.id.tv_categorie_nom);

        txtNom.setText(noms[position]);
        imageCategorie.setImageResource(images[position]);

        return view;
    }
}
