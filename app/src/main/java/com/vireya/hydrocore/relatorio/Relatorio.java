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
import com.vireya.hydrocore.core.network.RetrofitClient;
import com.vireya.hydrocore.funcionario.model.Funcionario;
import com.vireya.hydrocore.relatorio.adapter.RelatorioAdapter;
import com.vireya.hydrocore.relatorio.api.RelatorioApi;
import com.vireya.hydrocore.relatorio.model.RelatorioResumo;
import com.vireya.hydrocore.ui.configuracoes.api.ApiService;
import com.vireya.hydrocore.ui.perfil.api.ApiFuncionario;
import com.vireya.hydrocore.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Relatorio extends Fragment {

    private RecyclerView recyclerView;
    private RelatorioAdapter adapter;
    private RelatorioApi relatorioApi;
    private ApiFuncionario funcionarioApi;
    private ApiService apiService;
    private int idFuncionario;
    private int idEta;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_relatorio, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewRelatorios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        relatorioApi = RetrofitClient.getRetrofit(getContext()).create(RelatorioApi.class);
        funcionarioApi = RetrofitClient.getRetrofit(getContext()).create(ApiFuncionario.class);
        apiService = RetrofitClient.getRetrofit(getContext()).create(ApiService.class);


        SessionManager sessionManager = new SessionManager(requireContext());
        String email = sessionManager.getEmail();

        if (email == null || email.isEmpty()) {
            Toast.makeText(getContext(), "Erro: usuário não logado.", Toast.LENGTH_SHORT).show();
        } else {
            buscarFuncionarioPorEmail(email);
        }

        return view;
    }

    private void buscarFuncionarioPorEmail(String email) {
        apiService.getFuncionarioByEmail(email).enqueue(new Callback<Funcionario>() {
            @Override
            public void onResponse(Call<Funcionario> call, Response<Funcionario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    idFuncionario = response.body().getId();
                    buscarIdEtaDoFuncionario();
                } else {
                    Toast.makeText(getContext(), "Funcionário não encontrado para este e-mail.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Funcionario> call, Throwable t) {
                Toast.makeText(getContext(), "Erro ao buscar funcionário: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buscarIdEtaDoFuncionario() {
        funcionarioApi.getFuncionarioById(idFuncionario).enqueue(new Callback<Funcionario>() {
            @Override
            public void onResponse(Call<Funcionario> call, Response<Funcionario> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getEta() != null) {
                    idEta = response.body().getIdEta();
                    carregarRelatorios();
                } else {
                    Toast.makeText(getContext(), "Não foi possível obter o ID da ETA.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Funcionario> call, Throwable t) {
                Toast.makeText(getContext(), "Erro ao buscar ID da ETA: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void carregarRelatorios() {
        relatorioApi.listarRelatoriosPorEta(idEta).enqueue(new Callback<List<RelatorioResumo>>() {
            @Override
            public void onResponse(Call<List<RelatorioResumo>> call, Response<List<RelatorioResumo>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<RelatorioResumo> lista = response.body();
                    adapter = new RelatorioAdapter(lista, relatorioApi);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Nenhum relatório encontrado para esta ETA.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RelatorioResumo>> call, Throwable t) {
                Toast.makeText(getContext(), "Erro ao carregar relatórios: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
