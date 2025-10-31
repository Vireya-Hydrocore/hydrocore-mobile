package com.vireya.hydrocore.tarefas;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vireya.hydrocore.R;
import com.vireya.hydrocore.databinding.FragmentTarefasBinding;
import com.vireya.hydrocore.estoque.api.ApiClient;
import com.vireya.hydrocore.funcionario.model.Funcionario;
import com.vireya.hydrocore.tarefas.adapter.TarefasAdapter;
import com.vireya.hydrocore.tarefas.api.TarefasApi;
import com.vireya.hydrocore.tarefas.api.TarefasApiClient;
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

        Button btnAtualizarTarefa = view.findViewById(R.id.btnAtualizarStatus);

        btnAtualizarTarefa.setOnClickListener(v -> {
            List<Tarefa> selecionadas = tarefasAdapter.getTarefasSelecionadas();
            if (selecionadas.isEmpty()) return;

            final Dialog dialog = new Dialog(requireContext());
            dialog.setContentView(R.layout.card_concluir_tarefa);
            dialog.setCancelable(true);
            Button btnSim = dialog.findViewById(R.id.btnSim);
            Button btnNao = dialog.findViewById(R.id.btnNao);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            btnNao.setOnClickListener(v1 -> dialog.dismiss());

            SessionManager session = new SessionManager(requireContext());
            String token = "Bearer " + session.getToken();
            String email = session.getEmail();


            btnSim.setOnClickListener(v1 -> {

                for (Tarefa t : selecionadas) {
                    t.setStatus("concluida");


                    if (NetworkUtils.temConexao(requireContext())) {
                        tarefasApi.atualizarStatus(t.getId(), "concluida")
                                .enqueue(new Callback<Tarefa>() {
                                    @Override
                                    public void onResponse(Call<Tarefa> call, Response<Tarefa> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            Log.d("API", "ID: " + t.getId());
                                            Log.d("API", "Status atualizado para: " + response.body().getStatus());
                                            carregarTarefas();
                                        } else {
                                            Log.e("API", "Erro ao atualizar status: " + response.code());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Tarefa> call, Throwable t) {
                                        Log.e("API", "Falha ao atualizar status: " + t.getMessage());
                                    }
                                });

                    }
                }
                dialog.dismiss();
            });


            dialog.show();
        });


    }


    private void carregarTarefas() {
        SessionManager session = new SessionManager(requireContext());
        String emailFuncionario = session.getEmail();

        binding.progressBar.setVisibility(View.VISIBLE); // Mostra o círculo

        TarefasApi tarefasApi = TarefasApiClient.getTarefasClient(requireContext())
                .create(TarefasApi.class);

        tarefasApi.getFuncionarioPorEmail(emailFuncionario).enqueue(new Callback<Funcionario>() {
            @Override
            public void onResponse(Call<Funcionario> call, Response<Funcionario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Funcionario funcionario = response.body();
                    String nomeFuncionario = funcionario.getNome();

                    tarefasApi.listarTarefasPorNome(nomeFuncionario, false)
                            .enqueue(new Callback<List<Tarefa>>() {
                                @Override
                                public void onResponse(Call<List<Tarefa>> call, Response<List<Tarefa>> response) {
                                    binding.progressBar.setVisibility(View.GONE); // Esconde o círculo

                                    if (response.isSuccessful() && response.body() != null) {
                                        List<Tarefa> tarefasRecebidas = response.body();
                                        tarefas.clear();
                                        tarefas.addAll(tarefasRecebidas);
                                        tarefasAdapter.notifyDataSetChanged();

                                        Executors.newSingleThreadExecutor().execute(() -> {
                                            tarefaRepository.getTarefaDao().deleteAll();
                                            tarefaRepository.getTarefaDao().insertAll(tarefasRecebidas);
                                        });
                                    } else {
                                        Log.e("API", "Erro ao buscar tarefas: " + response.code());
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<Tarefa>> call, Throwable t) {
                                    binding.progressBar.setVisibility(View.GONE);
                                    Log.e("API", "Falha ao buscar tarefas: " + t.getMessage());
                                }
                            });
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    Log.e("API", "Erro ao buscar funcionário por email: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Funcionario> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Log.e("API", "Falha ao buscar funcionário: " + t.getMessage());
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
