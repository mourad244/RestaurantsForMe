package com.hfad.restaurantsforme;

public class CategorieFood {
    private int id;
    private String nom;
    private byte[] image;


    public CategorieFood(){}

    public CategorieFood(String nom, byte[] image ){
        this.nom = nom;
        this.image = image;
    }

    public CategorieFood(int id,  String nom,byte[] image){
        this.id = id;
        this.nom = nom;
        this.image = image;

    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

}
