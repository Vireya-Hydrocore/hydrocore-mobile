package com.vireya.hydrocore.ui.perfil.api;

import com.vireya.hydrocore.ui.perfil.model.Estatistica;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiFuncionario {
    @GET("v1/funcionario/{id}/resumo-tarefas")
    Call<Estatistica> getResumoTarefas(@Path("id") int idFuncionario);

}
