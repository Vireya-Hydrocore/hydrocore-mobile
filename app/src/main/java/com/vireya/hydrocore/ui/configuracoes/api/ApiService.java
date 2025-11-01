package com.vireya.hydrocore.ui.configuracoes.api;


import com.vireya.hydrocore.funcionario.model.Funcionario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("v1/funcionario/listar")
    Call<List<Funcionario>> getFuncionarios();

    @GET("v1/funcionario/email")
    Call<Funcionario> getFuncionarioByEmail(@Header("email") String email);

}
