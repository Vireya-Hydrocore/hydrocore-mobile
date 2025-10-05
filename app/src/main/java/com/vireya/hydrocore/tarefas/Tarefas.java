package com.vireya.hydrocore.tarefas;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vireya.hydrocore.databinding.FragmentTarefasBinding;
import com.vireya.hydrocore.tarefas.adapter.TarefasAdapter;
import com.vireya.hydrocore.tarefas.api.TarefasApi;
import com.vireya.hydrocore.tarefas.db.AppDatabase;
import com.vireya.hydrocore.tarefas.model.Tarefa;
import com.vireya.hydrocore.tarefas.repository.TarefaRepository;
import com.vireya.hydrocore.core.network.RetrofitClient;
import com.vireya.hydrocore.utils.NetworkUtils;
import com.vireya.hydrocore.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Tarefas extends Fragment {

    private FragmentTarefasBinding binding;
    private final List<Tarefa> tarefas = new ArrayList<>();
    private TarefasAdapter tarefasAdapter;

    private TarefaRepository tarefaRepository;
    private TarefasApi tarefasApi;

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

        AppDatabase db = AppDatabase.getInstance(requireContext());
        tarefasApi = RetrofitClient.getTarefasApi();
        tarefaRepository = new TarefaRepository(db.getTarefaDao(), tarefasApi);

        carregarTarefas();
    }

    private void carregarTarefas() {
        SessionManager session = new SessionManager(requireContext());
        String nomeFuncionario = session.getUsuarioNome();

        if (NetworkUtils.temConexao(requireContext())) {
            // Online: pega da API
            tarefasApi.listarTarefasPorNome(nomeFuncionario).enqueue(new Callback<List<Tarefa>>() {
                @Override
                public void onResponse(Call<List<Tarefa>> call, Response<List<Tarefa>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        tarefas.clear();
                        tarefas.addAll(response.body());
                        tarefasAdapter.setTarefas(tarefas);

                        Executors.newSingleThreadExecutor().execute(() -> {
                            tarefaRepository.getTarefaDao().deleteAll();
                            tarefaRepository.getTarefaDao().insertAll(tarefas);
                        });
                    }
                }

                @Override
                public void onFailure(Call<List<Tarefa>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            // Offline: pega do banco local
            Executors.newSingleThreadExecutor().execute(() -> {
                List<Tarefa> tarefasLocais = tarefaRepository.getAllTarefas();
                requireActivity().runOnUiThread(() -> {
                    tarefas.clear();
                    tarefas.addAll(tarefasLocais);
                    tarefasAdapter.setTarefas(tarefas);
                });
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
