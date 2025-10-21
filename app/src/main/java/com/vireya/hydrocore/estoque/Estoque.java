package com.vireya.hydrocore.estoque;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.vireya.hydrocore.estoque.adapter.ProdutoAdapter;
import com.vireya.hydrocore.estoque.api.ApiClient;
import com.vireya.hydrocore.estoque.api.ApiService;
import com.vireya.hydrocore.estoque.model.Produto;
import com.vireya.hydrocore.estoque.repository.ProdutoRepository;

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
        apiService = ApiClient.getClient().create(ApiService.class);

        produtoRepository = new ProdutoRepository(requireContext());

        carregarProdutos();

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

    private void carregarProdutos() {
        // Primeiro tenta carregar da API
        apiService.getProdutos().enqueue(new Callback<List<Produto>>() {
            @Override
            public void onResponse(Call<List<Produto>> call, Response<List<Produto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body());
                    adapter.updateList(productList);
                    updateButtonUI("Todos", productList.size());

                    //  Salva no banco local (modo offline)
                    Executors.newSingleThreadExecutor().execute(() -> {
                        produtoRepository.getProdutosOffline(); // apenas garante o acesso
                        produtoRepository.syncProdutos(null);   // já deleta e insere tudo
                    });

                } else {
                    Log.e("API_DEBUG", "Erro HTTP: " + response.code());
                    carregarOffline("Erro HTTP, exibindo dados locais");
                }
            }

            @Override
            public void onFailure(Call<List<Produto>> call, Throwable t) {
                Log.e("API_DEBUG", "Falha: " + t.getMessage());
                carregarOffline("Falha de rede, exibindo dados locais");
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
