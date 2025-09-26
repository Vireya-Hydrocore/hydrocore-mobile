package com.vireya.hydrocore.agenda.api;

import com.vireya.hydrocore.agenda.model.Aviso;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiClient {
    @GET("avisos/listar")
    Call<List<Aviso>> getAvisos();
}
