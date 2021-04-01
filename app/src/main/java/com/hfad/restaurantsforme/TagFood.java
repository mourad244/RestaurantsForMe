package com.hfad.restaurantsforme;

public class TagFood {
    private int id;
    private String nom;
    private int categorieFoodId;

    public TagFood(){}
    public TagFood(String nom, int categorieFoodId){
        this.nom = nom;
        this.categorieFoodId = categorieFoodId;
    }
    public TagFood(int id, String nom, int categorieFoodId) {
        this.id = id;
        this.nom = nom;
        this.categorieFoodId = categorieFoodId;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getCategorieFoodId() {
        return categorieFoodId;
    }
    public void setCategorieFoodId(int categorieFoodId) {
        this.categorieFoodId = categorieFoodId;
    }
}
