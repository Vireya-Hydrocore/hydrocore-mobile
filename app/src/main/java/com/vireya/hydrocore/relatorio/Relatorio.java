package com.vireya.hydrocore.relatorio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vireya.hydrocore.R;
import com.vireya.hydrocore.relatorio.adapter.RelatorioAdapter;
import com.vireya.hydrocore.relatorio.api.RelatorioApi;
import com.vireya.hydrocore.relatorio.model.RelatorioResumo;
import com.vireya.hydrocore.core.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Relatorio extends Fragment {

    private RecyclerView recyclerView;
    private RelatorioAdapter adapter;
    private RelatorioApi api;
    private long idEta = 1L;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_relatorio, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewRelatorios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        api = RetrofitClient.getRetrofit(getContext()).create(RelatorioApi.class);

        carregarRelatorios();

        return view;
    }

    private void carregarRelatorios() {
        api.listarRelatoriosPorEta(idEta).enqueue(new Callback<List<RelatorioResumo>>() {
            @Override
            public void onResponse(Call<List<RelatorioResumo>> call, Response<List<RelatorioResumo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<RelatorioResumo> lista = response.body();
                    adapter = new RelatorioAdapter(lista, api);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Nenhum relatório encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RelatorioResumo>> call, Throwable t) {
                Toast.makeText(getContext(), "Erro ao carregar relatórios", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
