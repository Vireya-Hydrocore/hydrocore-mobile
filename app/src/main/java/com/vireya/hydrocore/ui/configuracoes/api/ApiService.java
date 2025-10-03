package com.vireya.hydrocore.ui.configuracoes.api;

import com.vireya.hydrocore.ui.configuracoes.model.Funcionario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("v1/funcionario/listar")
    Call<List<Funcionario>> getFuncionarios();
}
