package com.vireya.hydrocore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
            Toast.makeText(getContext(), "Abrir PDF: " + relatorio, Toast.LENGTH_SHORT).show();
            // Aqui você abre o PdfViewerActivity
        });

        recyclerView.setAdapter(adapter);

        return view;
    }
}