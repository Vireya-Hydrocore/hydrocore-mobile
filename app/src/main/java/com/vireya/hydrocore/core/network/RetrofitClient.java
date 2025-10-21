package com.vireya.hydrocore.core.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.vireya.hydrocore.tarefas.api.TarefasApi;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class RetrofitClient {

    private static final String BASE_URL = "https://hydrocore-api-prod.onrender.com/";
    private static Retrofit retrofit;

    // 🔐 Suas credenciais fixas (depois você pode pegar dinamicamente, se quiser)
    private static final String USER_EMAIL = "teste@email.com";
    private static final String BEARER_TOKEN = "teste233";

    // === GSON customizado para datas ===
    private static Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    private final SimpleDateFormat sdf =
                            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault());

                    @Override
                    public Date deserialize(JsonElement json, Type typeOfT,
                                            JsonDeserializationContext context) throws JsonParseException {
                        try {
                            return sdf.parse(json.getAsString());
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                })
                .create();
    }

    // === Cliente HTTP com interceptador para cabeçalhos ===
    private static OkHttpClient buildClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        Request.Builder builder = original.newBuilder()
                                .header("X-User-Email", USER_EMAIL)
                                .header("Authorization", "Bearer " + BEARER_TOKEN)
                                .method(original.method(), original.body());

                        Request request = builder.build();
                        return chain.proceed(request);
                    }
                })
                .build();
    }

    // === Instância Retrofit com Gson e cliente customizado ===
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(buildGson()))
                    .client(buildClient())
                    .build();
        }
        return retrofit;
    }

    // === Exemplo de API específica ===
    public static TarefasApi getTarefasApi() {
        return getRetrofit().create(TarefasApi.class);
    }
}
