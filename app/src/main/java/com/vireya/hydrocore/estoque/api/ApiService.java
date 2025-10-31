package com.vireya.hydrocore.estoque.api;

import com.vireya.hydrocore.estoque.model.Produto;
import com.vireya.hydrocore.estoque.model.ProdutoResponse;
import com.vireya.hydrocore.funcionario.model.Funcionario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ApiService {

    @GET("/v1/funcionario/email")
    Call<Funcionario> getFuncionarioPorEmail(@Header("email") String email);

    @GET("/v1/funcionario/{id}")
    Call<Funcionario> getFuncionarioPorId(@Path("id") int id);

    @GET("/v1/estoque/listar")
    Call<List<Produto>> getProdutosPorEta(@Header("nome") String eta);

}
