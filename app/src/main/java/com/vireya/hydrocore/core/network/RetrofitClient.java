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

    public static final String BASE_URL = "https://hydrocore-api-prod.onrender.com/";
    private static Retrofit retrofit;

    // 🔐 Credenciais fixas (você pode mudar pra pegar do login depois)
    private static final String USER_EMAIL = "teste@email.com";
    private static final String BEARER_TOKEN = "teste233";

    // === GSON customizado para lidar com múltiplos formatos de data ===
    private static Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    private final SimpleDateFormat[] formats = new SimpleDateFormat[]{
                            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault()),
                            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.getDefault()),
                            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                    };

                    @Override
                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        String dateStr = json.getAsString();
                        for (SimpleDateFormat format : formats) {
                            try {
                                return format.parse(dateStr);
                            } catch (Exception ignored) {
                            }
                        }
                        System.err.println("⚠️ Não foi possível converter data: " + dateStr);
                        return null;
                    }
                })
                .create();
    }

    // === Cliente HTTP com interceptador para autenticação ===
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

    // === Instância Retrofit com Gson e client customizado ===
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
