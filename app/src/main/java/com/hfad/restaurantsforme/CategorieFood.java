package com.hfad.restaurantsforme;

public class CategorieFood {
    private int id;
    private int image;
    private String nom;


    public CategorieFood(){}

    public CategorieFood(int image, String nom){
        this.image = image;
        this.nom = nom;
    }

    public CategorieFood(int id, int image, String nom){
        this.id = id;
        this.image = image;
        this.nom = nom;
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
