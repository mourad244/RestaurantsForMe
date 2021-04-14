package com.hfad.restaurantsforme;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitServices {
    @POST("/restauapi/auth")
    Call <User> login(@Body Login login);
    }
