package com.vireya.hydrocore.tarefas.api;



import com.vireya.hydrocore.tarefas.model.Tarefa;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TarefasApi {
    @GET("v1/tarefas/listar-nome/{nome}")
    Call<List<Tarefa>> listarTarefasPorNome(@Path(value = "nome", encoded = true)String nome);

}
