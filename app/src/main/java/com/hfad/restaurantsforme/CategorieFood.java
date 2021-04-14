package com.hfad.restaurantsforme;

import com.google.gson.annotations.SerializedName;

public class CategorieFood {
    @SerializedName("_id")
    private String id;

    private String nom;

    @SerializedName("images")
    private String[] urlImage;


    public CategorieFood(){}

    public CategorieFood(String nom, String[] urlImage){
        this.nom = nom;
        this.urlImage = urlImage;
    }

    public CategorieFood(String id,  String nom,String[] image){
        this.id = id;
        this.nom = nom;
        this.urlImage = image;

    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String[] getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String[] urlImage) {
        this.urlImage = urlImage;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

}
