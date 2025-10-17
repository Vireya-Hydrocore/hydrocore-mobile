package com.vireya.hydrocore.calculadora.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.vireya.hydrocore.calculadora.api.CalculadoraApi;
import com.vireya.hydrocore.calculadora.model.CalculoCoagulacaoRequest;
import com.vireya.hydrocore.calculadora.model.CalculoResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalculadoraRepository {

    private final CalculadoraApi calculadoraApi;

    public CalculadoraRepository(CalculadoraApi calculadoraApi) {
        this.calculadoraApi = calculadoraApi;
    }

    public void calcularCoagulacao(CalculoCoagulacaoRequest request, CalculadoraCallback callback) {
        calculadoraApi.calcularCoagulacao(request).enqueue(new Callback<CalculoResponse>() {
            @Override
            public void onResponse(@NonNull Call<CalculoResponse> call, @NonNull Response<CalculoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSucesso(response.body());
                } else {
                    callback.onErro("Erro no cálculo: código " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CalculoResponse> call, @NonNull Throwable t) {
                callback.onErro("Falha na requisição: " + t.getMessage());
                Log.e("CalculadoraRepository", "Erro API", t);
            }
        });
    }

    public interface CalculadoraCallback {
        void onSucesso(CalculoResponse resposta);
        void onErro(String mensagem);
    }
}
