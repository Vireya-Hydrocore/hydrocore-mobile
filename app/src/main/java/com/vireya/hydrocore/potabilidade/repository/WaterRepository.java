package com.vireya.hydrocore.potabilidade.repository;

import com.vireya.hydrocore.potabilidade.model.PotabilityResponse;
import com.vireya.hydrocore.potabilidade.model.WaterData;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WaterRepository {

    private final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public interface PotabilityCallback {
        void onResult(PotabilityResponse response);
        void onError(String error);
    }

    public void enviarParaApi(WaterData data, PotabilityCallback callback) {
        try {
            JSONObject json = new JSONObject();
            json.put("ph", data.ph);
            json.put("Hardness", data.Hardness);
            json.put("Solids", data.Solids);
            json.put("Chloramines", data.Chloramines);
            json.put("Sulfate", data.Sulfate);
            json.put("Conductivity", data.Conductivity);
            json.put("Organic_carbon", data.Organic_carbon);
            json.put("Trihalomethanes", data.Trihalomethanes);
            json.put("Turbidity", data.Turbidity);

            RequestBody body = RequestBody.create(json.toString(), JSON);
            Request request = new Request.Builder()
                    .url("https://hydrocore-api-machine-learning.onrender.com/predict")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onError("Erro na requisição");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        callback.onResult(PotabilityResponse.fromJson(res));
                    } else {
                        callback.onError("Falha: " + response.code());
                    }
                }
            });

        } catch (Exception e) {
            callback.onError("Dados inválidos");
        }
    }
}

