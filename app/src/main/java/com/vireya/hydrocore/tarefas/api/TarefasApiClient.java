package com.vireya.hydrocore.tarefas.api;

import static com.vireya.hydrocore.core.network.RetrofitClient.BASE_URL;

import android.content.Context;

import com.vireya.hydrocore.utils.SessionManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TarefasApiClient {
    private static Retrofit retrofitTarefas; // variável própria

    public static Retrofit getTarefasClient(Context context) {
        if (retrofitTarefas == null) {
            SessionManager session = new SessionManager(context);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request request = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer teste233")
                                .addHeader("X-User-Email", "teste@email.com")
                                .build();
                        return chain.proceed(request);
                    })
                    .build();

            retrofitTarefas = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofitTarefas;
    }
}

