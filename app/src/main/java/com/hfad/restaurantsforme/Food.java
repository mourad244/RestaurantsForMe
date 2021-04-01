package com.hfad.restaurantsforme;

public class Food {
    private int id;
    private String nom;
    private int image;
    private String description;
    private int prix;
    private int restaurantId;

    public Food(int id, String nom, int image, String description, int prix, int restaurantId){
        this.id = id;
        this.nom = nom;
        this.image = image;
        this.description = description;
        this.prix = prix;
        this.restaurantId = restaurantId;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }

    public void setNom(String nom) { this.nom = nom; }

    public int getImage() { return image; }

    public void setImage(int image) { this.image = image; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public int getPrix() { return prix; }

    public void setPrix(int prix) { this.prix = prix; }

    public int getRestaurantId() { return restaurantId; }

    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }
}