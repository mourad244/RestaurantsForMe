package com.hfad.restaurantsforme;

public class CategorieFood {
    private int image;
    private String nom;

    public CategorieFood(int image, String nom){
        this.image = image;
        this.nom = nom;
    }

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
