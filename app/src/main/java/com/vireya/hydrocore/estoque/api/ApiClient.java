package com.vireya.hydrocore.estoque.api;

import android.content.Context;

import com.vireya.hydrocore.utils.SessionManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://hydrocore-api-prod.onrender.com/";

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            SessionManager session = new SessionManager(context);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor((Interceptor.Chain chain) -> {
                        Request request = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer " + session.getToken())
                                .addHeader("X-User-Email", session.getEmail())
                                .build();
                        return chain.proceed(request);

                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
