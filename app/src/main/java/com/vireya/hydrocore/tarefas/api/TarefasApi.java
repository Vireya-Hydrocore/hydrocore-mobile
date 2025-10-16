package com.vireya.hydrocore.tarefas.api;



import com.vireya.hydrocore.tarefas.model.Tarefa;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TarefasApi {

    @Headers({
            "Authorization: Bearer teste233",
            "X-User-Email: teste@email.com"
    })
    @GET("v1/tarefas/listar-nome/{nome}")
    Call<List<Tarefa>> listarTarefasPorNome(@Path(value = "nome", encoded = true)String nome);

    @Headers({
            "Authorization: Bearer teste233",
            "X-User-Email: teste@email.com"
    })
    @PATCH("v1/tarefas/atualizar-status")
    Call<Void> atualizarStatus(@Body AtualizarStatusRequest request);

    class AtualizarStatusRequest {
        private int idTarefa;
        private String status;

        public AtualizarStatusRequest(int idTarefa, String status) {
            this.idTarefa = idTarefa;
            this.status = status;
        }

        public int getIdTarefa() { return idTarefa; }
        public String getStatus() { return status; }
    }

}
