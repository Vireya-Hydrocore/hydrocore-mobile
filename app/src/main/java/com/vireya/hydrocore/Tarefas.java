package com.vireya.hydrocore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vireya.hydrocore.databinding.FragmentTarefasBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Tarefas extends Fragment {

    //Variáveis
    private FragmentTarefasBinding binding;
    List<Tarefa> tarefas = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTarefasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //configurar o RecycleView
        binding.rvTarefas.setLayoutManager(new LinearLayoutManager(getContext()));

        // Preencher a lista com exemplos
        tarefas.add(new Tarefa("Tarefa 1", "Descrição 1", new Date(2025, 8, 23)));
        tarefas.add(new Tarefa("Tarefa 2", "Descrição 2", new Date(2025, 8, 23)));
        tarefas.add(new Tarefa("Tarefa 3", "Descrição 3", new Date(2025, 8, 23)));
        tarefas.add(new Tarefa("Tarefa 4", "Descrição 4", new Date(2025, 8, 23)));
        tarefas.add(new Tarefa("Tarefa 5", "Descrição 5", new Date(2025, 8, 23)));

        // Configurar a Adapter
        TarefasAdapter tarefasAdapter = new TarefasAdapter(tarefas);
        binding.rvTarefas.setAdapter(tarefasAdapter);


    }


}