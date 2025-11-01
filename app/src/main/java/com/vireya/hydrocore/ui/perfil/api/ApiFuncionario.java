package com.vireya.hydrocore.ui.perfil.api;

import com.vireya.hydrocore.funcionario.model.Funcionario;
import com.vireya.hydrocore.ui.perfil.model.Estatistica;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ApiFuncionario {
    @GET("v1/funcionario/resumo-tarefas")
    Call<Estatistica> getResumoTarefas(@Header("id") int idFuncionario);

    @GET("v1/funcionario/{id}")
    Call<Funcionario> getFuncionarioById(@Path("id") int idFuncionario) ;


}
