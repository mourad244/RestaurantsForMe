package com.hfad.restaurantsforme;

public class Food {
    private int id;
    private String nom;
    private byte[] image;
    private String description;
    private int prix;
    private long restaurantId;

    public Food(){};
    public Food(String nom, byte[] image, String description, int prix, long restaurantId){
        this.nom = nom;
        this.image = image;
        this.description = description;
        this.prix = prix;
        this.restaurantId = restaurantId;
    }
    public Food(String nom, String description, int prix, long restaurantId){
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.restaurantId = restaurantId;
    }
    public Food(int id, String nom, byte[] image, String description, int prix, long restaurantId){
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

    public byte[] getImage() { return image; }

    public void setImage(byte[] image) { this.image = image; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public int getPrix() { return prix; }

    public void setPrix(int prix) { this.prix = prix; }

    public long getRestaurantId() { return restaurantId; }

    public void setRestaurantId(long restaurantId) { this.restaurantId = restaurantId; }
}
