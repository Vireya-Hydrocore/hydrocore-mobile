package com.vireya.hydrocore.calculadora.api;

import com.vireya.hydrocore.calculadora.model.CalculoCoagulacaoRequest;
import com.vireya.hydrocore.calculadora.model.CalculoFloculacaoRequest;
import com.vireya.hydrocore.calculadora.model.CalculoResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CalculadoraApi {

    @Headers({
            "Authorization: Bearer teste233",
            "X-User-Email: teste@email.com"
    })
    @POST("v1/calculadora/coagulacao")
    Call<CalculoResponse> calcularCoagulacao(@Body CalculoCoagulacaoRequest request);

    @Headers({
            "Authorization: Bearer teste233",
            "X-User-Email: teste@email.com"
    })
    @POST("v1/calculadora/floculacao")
    Call<CalculoResponse> calcularFloculacao(@Body CalculoFloculacaoRequest request);
}
