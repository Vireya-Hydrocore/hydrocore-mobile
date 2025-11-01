package com.vireya.hydrocore.relatorio.api;

import com.vireya.hydrocore.relatorio.model.RelatorioDetalhado;
import com.vireya.hydrocore.relatorio.model.RelatorioResumo;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RelatorioApi {

    @GET("v1/eta/listar-relatorios/{idEta}")
    Call<List<RelatorioResumo>> listarRelatoriosPorEta(@Path("idEta") long idEta);

    @GET("v1/eta/relatorio")
    Call<List<RelatorioDetalhado>> buscarRelatorioPorMesAno(
            @Query("mes") int mes,
            @Query("ano") int ano
    );

    @GET("v1/funcionario/{id}")
    Call<List<RelatorioDetalhado>> listarRelatoriosPorEtaFuncionario(@Path("id") long id);
}
