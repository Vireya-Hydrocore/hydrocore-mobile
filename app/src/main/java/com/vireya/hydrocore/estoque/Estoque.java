package com.vireya.hydrocore.estoque;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.vireya.hydrocore.R;
import com.vireya.hydrocore.core.network.RetrofitClient;
import com.vireya.hydrocore.estoque.adapter.ProdutoAdapter;
import com.vireya.hydrocore.estoque.api.ApiService;
import com.vireya.hydrocore.estoque.model.Produto;
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

    public Estoque() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_estoque, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProdutoAdapter(productList);
        recyclerView.setAdapter(adapter);

        button = view.findViewById(R.id.button);
        apiService = RetrofitClient.getRetrofit(requireContext()).create(ApiService.class);
        produtoRepository = new ProdutoRepository(requireContext());

        // Use a view inflada para o progressBar
        View progressBar = view.findViewById(R.id.progressBarEstoque);

        carregarProdutos(progressBar);

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterList(tab.getPosition());
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        return view;
    }


    private void carregarProdutos(View progressBar) {
        SessionManager session = new SessionManager(requireContext());
        String emailFuncionario = session.getEmail();

        progressBar.setVisibility(View.VISIBLE);

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

                                apiService.getProdutosPorEta(eta).enqueue(new Callback<List<Produto>>() {
                                    @Override
                                    public void onResponse(Call<List<Produto>> call, Response<List<Produto>> responseProdutos) {
                                        progressBar.setVisibility(View.GONE);

                                        if (responseProdutos.isSuccessful() && responseProdutos.body() != null) {
                                            productList.clear();
                                            productList.addAll(responseProdutos.body());
                                            adapter.updateList(productList);
                                            updateButtonUI("Todos", productList.size());
                                        } else {
                                            carregarOffline("Erro ao buscar produtos");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<Produto>> call, Throwable t) {
                                        progressBar.setVisibility(View.GONE);
                                        carregarOffline("Falha ao buscar produtos: " + t.getMessage());
                                    }
                                });

                            } else {
                                progressBar.setVisibility(View.GONE);
                                carregarOffline("Funcionário não encontrado pelo ID");
                            }
                        }

                        @Override
                        public void onFailure(Call<Funcionario> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            carregarOffline("Falha ao buscar funcionário pelo ID: " + t.getMessage());
                        }
                    });
                } else {
                    progressBar.setVisibility(View.GONE);
                    carregarOffline("Funcionário não encontrado pelo email");
                }
            }

            @Override
            public void onFailure(Call<Funcionario> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                carregarOffline("Falha ao buscar funcionário: " + t.getMessage());
            }
        });
    }





    private void carregarOffline(String mensagem) {
        Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();

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
        String status = "";

        switch (position) {
            case 0:
                adapter.updateList(productList);
                status = "Todos";
                break;
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
