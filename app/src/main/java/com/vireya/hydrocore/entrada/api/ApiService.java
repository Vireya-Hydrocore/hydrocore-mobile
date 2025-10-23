package com.vireya.hydrocore.entrada.api;

import com.vireya.hydrocore.entrada.model.Login;
import com.vireya.hydrocore.entrada.model.Login;
import com.vireya.hydrocore.entrada.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    // Endpoint de login
    @POST("auth/login")
    Call<LoginResponse> login(@Body Login request);
}
