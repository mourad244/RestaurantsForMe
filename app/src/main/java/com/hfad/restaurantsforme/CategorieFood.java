package com.hfad.restaurantsforme;

public class CategorieFood {
    private int id;
    private String nom;
    private int image;


    public CategorieFood(){}

    public CategorieFood(String nom, int image ){
        this.nom = nom;
        this.image = image;
    }

    public CategorieFood(int id,  String nom,int image){
        this.id = id;
        this.nom = nom;
        this.image = image;

    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

}
