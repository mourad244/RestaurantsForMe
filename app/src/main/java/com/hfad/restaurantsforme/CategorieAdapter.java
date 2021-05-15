package com.hfad.restaurantsforme;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CategorieAdapter extends BaseAdapter {

    private final List<CategorieFood> categorieFoods;
    private final Context mContext;

    public CategorieAdapter(List<CategorieFood> categorieFoods, Context mContext) {
        this.categorieFoods = categorieFoods;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return categorieFoods.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //1
        final CategorieFood categorieFood = categorieFoods.get(position);

        //2
        if (convertView == null){
            final LayoutInflater layoutInflater =
                    LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.layout_grid_item_categorie,null);
        }

        //3
        final ImageView image = (ImageView)convertView.findViewById(R.id.iv_categorie_image);
        final TextView nom = (TextView)convertView.findViewById(R.id.tv_categorie_nom);

        //4


        //5
    String[] path = categorieFood.getUrlImage();
        if(path.length !=0){
            Picasso.with(mContext).load("https://still-stream-25624.herokuapp.com/" +
                    categorieFood.getUrlImage()[0]).into(image);
//        DbBitmapUtility.setImageViewWithByteArray(image,categorieFood.getImage());
            nom.setText(categorieFood.getNom());
        }
       return convertView;
    }
}
