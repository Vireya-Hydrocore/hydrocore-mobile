package com.vireya.hydrocore.core.network;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.vireya.hydrocore.relatorio.api.RelatorioApi;
import com.vireya.hydrocore.tarefas.api.TarefasApi;
import com.vireya.hydrocore.utils.SessionManager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static final String BASE_URL = "https://hydrocore-api-prod.onrender.com/";
    private static Retrofit retrofit;
    private static final String TAG = "RETROFIT_DEBUG";

    private static Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    private final SimpleDateFormat[] formats = new SimpleDateFormat[]{
                            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault()),
                            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.getDefault()),
                            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()),
                            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) //

                    };

                    @Override
                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        String dateStr = json.getAsString();
                        for (SimpleDateFormat format : formats) {
                            try {
                                return format.parse(dateStr);
                            } catch (Exception ignored) {}
                        }
                        System.err.println("Não foi possível converter data: " + dateStr);
                        return null;
                    }
                })
                .create();
    }

    private static String limparAcentos(String texto) {
        if (texto == null) return null;
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }

    private static OkHttpClient buildClient(Context context) {
        SessionManager
                session = new SessionManager(context);

        return new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        String eta = session.getEta();
                        if (eta != null) {
                            eta = limparAcentos(eta);
                        }

                        Log.d(TAG, "ETA enviada no header: " + eta);

                        Request.Builder builder = original.newBuilder()
                                .header("X-User-Email", session.getEmail())
                                .header("Authorization", "Bearer " + session.getToken());

                        if (eta != null && !eta.isEmpty()) {
                            builder.header("nome", eta);
                        }

                        Request request = builder
                                .method(original.method(), original.body())
                                .build();

                        return chain.proceed(request);
                    }
                })
                .build();
    }

    public static Retrofit getRetrofit(Context context) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(buildGson()))
                    .client(buildClient(context))
                    .build();
        }
        return retrofit;
    }

    public static RelatorioApi getRelatorioApi(Context context) {
        return getRetrofit(context).create(RelatorioApi.class);
    }

    public static TarefasApi getTarefasApi(Context context) {
        return getRetrofit(context).create(TarefasApi.class);
    }
}
