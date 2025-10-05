package com.vireya.hydrocore.tarefas;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vireya.hydrocore.R;
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


            btnSim.setOnClickListener(v1 -> {


                for (Tarefa t : selecionadas) {
                    t.setStatus("concluída"); // atualiza localmente

                    // atualiza no adapter
                    tarefasAdapter.notifyItemChanged(tarefas.indexOf(t));

                    // atualiza na API
                    if (NetworkUtils.temConexao(requireContext())) {
                        tarefasApi.atualizarStatus(
                                new TarefasApi.AtualizarStatusRequest(t.getIdTarefa(), t.getStatus())
                        ).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (!response.isSuccessful()) {
                                    System.err.println("Erro API: " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                t.printStackTrace();
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
//        String nomeFuncionario = session.getUsuarioNome();
        String nomeFuncionario = "Lucas Pereira";
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
