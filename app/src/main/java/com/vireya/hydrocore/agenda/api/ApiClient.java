package com.vireya.hydrocore.agenda.api;

import com.vireya.hydrocore.agenda.model.Aviso;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiClient {
    @GET("v1/avisos/listar")
    Call<List<Aviso>> getAvisos();

    @GET("v1/avisos/ultimos-avisos")
    Call<List<Aviso>> getUltimosAvisos(@Header("dataReferencia") String dataReferencia);

}
