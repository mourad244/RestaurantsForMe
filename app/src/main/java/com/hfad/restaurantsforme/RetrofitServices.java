package com.hfad.restaurantsforme;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitServices {
    @POST("/restauapi/auth")
    Call <String> login(@Body Login login);

    @GET("/restauapi/categoriesfood")
    Call <List<CategorieFood>> getCategoriesFood();

    @GET("/restauapi/restaurants")
    Call<List<Restaurant>> getRestaurants();
}


