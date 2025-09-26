package com.vireya.hydrocore.tarefas;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vireya.hydrocore.R;
import com.vireya.hydrocore.databinding.FragmentTarefasBinding;
import com.vireya.hydrocore.tarefas.adapter.TarefasAdapter;
import com.vireya.hydrocore.core.network.RetrofitClient;
import com.vireya.hydrocore.tarefas.api.TarefasApi;
import com.vireya.hydrocore.tarefas.model.Tarefa;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Tarefas extends Fragment {

    private FragmentTarefasBinding binding;
    private final List<Tarefa> tarefas = new ArrayList<>();
    private TarefasAdapter tarefasAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTarefasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.rvTarefas.setLayoutManager(new LinearLayoutManager(requireContext()));
        tarefasAdapter = new TarefasAdapter(tarefas);
        binding.rvTarefas.setAdapter(tarefasAdapter);

        carregarTarefas("Lucas Pereira");
    }

    private void carregarTarefas(String nome) {
        TarefasApi api = RetrofitClient.getTarefasApi();
        api.listarTarefasPorNome(nome).enqueue(new Callback<List<Tarefa>>() {
            @Override
            public void onResponse(Call<List<Tarefa>> call, Response<List<Tarefa>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tarefas.clear();
                    tarefas.addAll(response.body());
                    tarefasAdapter.setTarefas(tarefas);
                }
            }

            @Override
            public void onFailure(Call<List<Tarefa>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
