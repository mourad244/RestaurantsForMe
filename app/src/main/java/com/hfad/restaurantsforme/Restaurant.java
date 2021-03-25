package com.hfad.restaurantsforme;

public class Restaurant {
    private int id;
    private String nom;
    private int image;
    private String description;
    private String h_ouverture;
    private String h_fermeture;
    private String telephone;
    private int QR_code;
    private String longitude;
    private String latitude;



    public Restaurant (int id, String nom, int image, String description, String h_ouverture,
                       String h_fermeture, String telephone, int QR_code, String longitude,
                       String latitude){
        this.id = id;
        this.nom = nom;
        this.image = image;
        this.description = description;
        this.h_ouverture = h_ouverture;
        this.h_fermeture = h_fermeture;
        this.telephone = telephone;
        this.QR_code = QR_code;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }

    public void setNom(String nom) { this.nom = nom; }

    public int getImage() { return image; }

    public void setImage(int image) { this.image = image; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getH_ouverture() { return h_ouverture; }

    public void setH_ouverture(String h_ouverture) { this.h_ouverture = h_ouverture; }

    public String getH_fermeture() { return h_fermeture; }

    public void setH_fermeture(String h_fermeture) { this.h_fermeture = h_fermeture; }

    public String getTelephone() { return telephone; }

    public void setTelephone(String telephone) { this.telephone = telephone; }

    public int getQR_code() { return QR_code; }

    public void setQR_code(int QR_code) { this.QR_code = QR_code; }

    public String getLongitude() { return longitude; }

    public void setLongitude(String longitude) { this.longitude = longitude; }

    public String getLatitude() { return latitude; }

    public void setLatitude(String latitude) { this.latitude = latitude; }
}
