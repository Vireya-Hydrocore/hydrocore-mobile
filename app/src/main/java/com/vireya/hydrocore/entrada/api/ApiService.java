package com.vireya.hydrocore.entrada.api;

import com.vireya.hydrocore.entrada.model.Login;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("auth/login")
    Call<ResponseBody> login(@Body Login login);

    @POST("auth/forgot-password")
    Call<ResponseBody> forgotPassword(@Body Map<String, String> body);

    @POST("auth/reset-password")
    Call<ResponseBody> resetPassword(@Body Map<String, String> body);


}
