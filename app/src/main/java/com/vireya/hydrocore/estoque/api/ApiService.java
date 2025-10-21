package com.vireya.hydrocore.estoque.api;

import com.vireya.hydrocore.estoque.model.Produto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("/v1/estoque/listar")
    Call<List<Produto>> getProdutos();
}
