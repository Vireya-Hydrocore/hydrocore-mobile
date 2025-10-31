package com.vireya.hydrocore.estoque;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.vireya.hydrocore.R;
import com.vireya.hydrocore.estoque.adapter.ProdutoAdapter;
import com.vireya.hydrocore.estoque.api.ApiClient;
import com.vireya.hydrocore.estoque.api.ApiService;
import com.vireya.hydrocore.estoque.model.Produto;
import com.vireya.hydrocore.estoque.model.ProdutoResponse;
import com.vireya.hydrocore.estoque.repository.ProdutoRepository;
import com.vireya.hydrocore.funcionario.model.Funcionario;
import com.vireya.hydrocore.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Estoque extends Fragment {

    private RecyclerView recyclerView;
    private ProdutoAdapter adapter;
    private List<Produto> productList = new ArrayList<>();
    private ApiService apiService;
    private ProdutoRepository produtoRepository;
    private Button button;
    private View progressBar;

    public Estoque() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_estoque, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ProdutoAdapter(productList);
        recyclerView.setAdapter(adapter);

        button = view.findViewById(R.id.button);
        progressBar = view.findViewById(R.id.progressBarEstoque);

        apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        produtoRepository = new ProdutoRepository(requireContext());

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterList(tab.getPosition());
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        carregarProdutos();
    }

    private void carregarProdutos() {
        SessionManager session = new SessionManager(requireContext());
        String emailFuncionario = session.getEmail();

        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

        apiService.getFuncionarioPorEmail(emailFuncionario).enqueue(new Callback<Funcionario>() {
            @Override
            public void onResponse(Call<Funcionario> call, Response<Funcionario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Funcionario funcionarioParcial = response.body();
                    int funcionarioId = funcionarioParcial.getId();

                    apiService.getFuncionarioPorId(funcionarioId).enqueue(new Callback<Funcionario>() {
                        @Override
                        public void onResponse(Call<Funcionario> call, Response<Funcionario> responseFull) {
                            if (responseFull.isSuccessful() && responseFull.body() != null) {
                                Funcionario funcionarioCompleto = responseFull.body();
                                String eta = funcionarioCompleto.getEta();

                                carregarProdutosPorEta(eta);
                            } else {
                                esconderProgress();
                                carregarOffline("Funcionário não encontrado pelo ID");
                            }
                        }

                        @Override
                        public void onFailure(Call<Funcionario> call, Throwable t) {
                            esconderProgress();
                            carregarOffline("Falha ao buscar funcionário pelo ID: " + t.getMessage());
                        }
                    });
                } else {
                    esconderProgress();
                    carregarOffline("Funcionário não encontrado pelo email");
                }
            }

            @Override
            public void onFailure(Call<Funcionario> call, Throwable t) {
                esconderProgress();
                carregarOffline("Falha ao buscar funcionário: " + t.getMessage());
            }
        });
    }

    private void carregarProdutosPorEta(String eta) {
        apiService.getProdutosPorEta(eta).enqueue(new Callback<List<ProdutoResponse>>() {
            @Override
            public void onResponse(Call<List<ProdutoResponse>> call, Response<List<ProdutoResponse>> response) {
                esconderProgress();

                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    for (ProdutoResponse pr : response.body()) {
                        for (String nomeProduto : pr.getProdutos()) {
                            productList.add(new Produto(nomeProduto, 0, "Suficiente"));
                        }
                    }
                    adapter.updateList(productList);
                    updateButtonUI("Todos", productList.size());
                } else {
                    carregarOffline("Erro ao buscar produtos");
                }
            }

            @Override
            public void onFailure(Call<List<ProdutoResponse>> call, Throwable t) {
                esconderProgress();
                carregarOffline("Falha ao buscar produtos: " + t.getMessage());
            }
        });
    }

    private void esconderProgress() {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }

    private void carregarOffline(String mensagem) {
        Toast.makeText(requireContext(), mensagem, Toast.LENGTH_SHORT).show();

        Executors.newSingleThreadExecutor().execute(() -> {
            List<Produto> produtosOffline = produtoRepository.getProdutosOffline();
            requireActivity().runOnUiThread(() -> {
                productList.clear();
                productList.addAll(produtosOffline);
                adapter.updateList(productList);
                updateButtonUI("Offline", productList.size());
            });
        });
    }

    private void filterList(int position) {
        List<Produto> filteredList = new ArrayList<>();
        String status;

        switch (position) {
            case 1:
                for (Produto p : productList)
                    if ("Suficiente".equals(p.getStatus())) filteredList.add(p);
                adapter.updateList(filteredList);
                status = "Suficiente";
                break;
            case 2:
                for (Produto p : productList)
                    if ("Próximo ao fim".equals(p.getStatus())) filteredList.add(p);
                adapter.updateList(filteredList);
                status = "Próximo ao fim";
                break;
            case 3:
                for (Produto p : productList)
                    if ("Insuficiente".equals(p.getStatus())) filteredList.add(p);
                adapter.updateList(filteredList);
                status = "Insuficiente";
                break;
            default:
                adapter.updateList(productList);
                status = "Todos";
        }

        updateButtonUI(status, adapter.getItemCount());
    }

    private void updateButtonUI(String status, int count) {
        if (button == null) return;

        button.setText(count + " produtos");

        switch (status) {
            case "Suficiente":
                button.setBackgroundColor(0xFF00796B);
                button.setTextColor(Color.WHITE);
                break;
            case "Próximo ao fim":
                button.setBackgroundColor(0xFFFBC02D);
                button.setTextColor(Color.BLACK);
                break;
            case "Insuficiente":
                button.setBackgroundColor(0xFFD32F2F);
                button.setTextColor(Color.WHITE);
                break;
            default:
                button.setBackgroundColor(0xFF9E9E9E);
                button.setTextColor(Color.WHITE);
                break;
        }
    }
}
