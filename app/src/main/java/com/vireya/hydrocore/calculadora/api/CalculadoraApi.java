package com.vireya.hydrocore.calculadora.api;

import com.vireya.hydrocore.calculadora.model.CalculoCoagulacaoRequest;
import com.vireya.hydrocore.calculadora.model.CalculoFloculacaoRequest;
import com.vireya.hydrocore.calculadora.model.CalculoResponse;
import com.vireya.hydrocore.calculadora.model.ProdutoResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

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

    @Headers({
            "Authorization: Bearer teste233",
            "X-User-Email: teste@email.com"
    })
    @GET("v1/estoque/listar/produtos")
    Call<List<ProdutoResponse>> listarProdutos(@Header("nome") String etaNome);




}
