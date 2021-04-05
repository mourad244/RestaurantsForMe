package com.hfad.restaurantsforme;

public class Restaurant {
    private int id;
    private String nom;
    private byte[] image;
    private String description;
    private String h_ouverture;
    private String h_fermeture;
    private String telephone;
    private byte[] QR_code;
    private Double longitude;
    private Double latitude;


    public Restaurant (){};

    public Restaurant (String nom, byte[] image, String description, String h_ouverture,
                       String h_fermeture, String telephone, byte[] QR_code,Double latitude,
                       Double longitude ){
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
    public Restaurant (String nom, byte[] image, String description, String h_ouverture,
                       String h_fermeture, String telephone, Double latitude,Double longitude
                       ){
        this.nom = nom;
        this.image = image;
        this.description = description;
        this.h_ouverture = h_ouverture;
        this.h_fermeture = h_fermeture;
        this.telephone = telephone;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Restaurant (int id, String nom, byte[] image, String description, String h_ouverture,
                       String h_fermeture, String telephone, byte[] QR_code,
                       Double latitude, Double longitude){
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

    public byte[] getImage() { return image; }

    public void setImage(byte[] image) { this.image = image; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getH_ouverture() { return h_ouverture; }

    public void setH_ouverture(String h_ouverture) { this.h_ouverture = h_ouverture; }

    public String getH_fermeture() { return h_fermeture; }

    public void setH_fermeture(String h_fermeture) { this.h_fermeture = h_fermeture; }

    public String getTelephone() { return telephone; }

    public void setTelephone(String telephone) { this.telephone = telephone; }

    public byte[] getQR_code() { return QR_code; }

    public void setQR_code(byte[] QR_code) { this.QR_code = QR_code; }

    public Double getLongitude() { return longitude; }

    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Double getLatitude() { return latitude; }

    public void setLatitude(Double latitude) { this.latitude = latitude; }
}
