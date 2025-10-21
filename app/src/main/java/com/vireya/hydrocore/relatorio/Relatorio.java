package com.vireya.hydrocore.relatorio;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vireya.hydrocore.R;
import com.vireya.hydrocore.relatorio.adapter.RelatorioAdapter;

import java.util.ArrayList;
import java.util.List;

public class Relatorio extends Fragment {

    private RecyclerView recyclerView;
    private RelatorioAdapter adapter;
    private List<String> listaRelatorios;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_relatorio, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewRelatorios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listaRelatorios = new ArrayList<>();
        listaRelatorios.add("Junho 2025");
        listaRelatorios.add("Maio 2025");
        listaRelatorios.add("Abril 2025");
        listaRelatorios.add("Março 2025");
        listaRelatorios.add("Fevereiro 2025");
        listaRelatorios.add("Janeiro 2025");

        adapter = new RelatorioAdapter(listaRelatorios, relatorio -> {
            // Quando clicar no ícone de download
            String[] partes = relatorio.split(" ");
            String mesNome = partes[0];
            int ano = Integer.parseInt(partes[1]);
            int mes = RelatorioDownloader.converterMesParaNumero(mesNome);

            Toast.makeText(getContext(), "Gerando relatório de " + relatorio, Toast.LENGTH_SHORT).show();
            RelatorioDownloader.baixarRelatorioPdf(getContext(), mes, ano);
        });

        recyclerView.setAdapter(adapter);

        return view;
    }
}
