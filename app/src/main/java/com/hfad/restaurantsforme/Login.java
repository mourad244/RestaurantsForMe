package com.hfad.restaurantsforme;

public class Login {

    private String email;
    private String password;
    /* instance variable should be same as key defined in JSON
    otherwise explicitly define @SerializedName annotation */

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // setters and getters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
