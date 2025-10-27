package com.vireya.hydrocore.tarefas.api;

import com.vireya.hydrocore.tarefas.model.Tarefa;
import com.vireya.hydrocore.funcionario.model.Funcionario;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Header;

public interface TarefasApi {

    @GET("v1/funcionario/email")
    Call<Funcionario> getFuncionarioPorEmail(@Header("email") String email);

    @GET("v1/tarefas/listar-nome/{nome}")
    Call<List<Tarefa>> listarTarefasPorNome(
            @Path("nome") String nomeFuncionario,
            @Query("tarefasConcluidas") boolean tarefasConcluidas
    );

    @PATCH("v1/tarefas/atualizar-status")
    Call<Void> atualizarStatus(
            @Header("idTarefa") int idTarefa,
            @Header("status") String status
    );
}
