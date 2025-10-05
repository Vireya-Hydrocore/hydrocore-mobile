package com.vireya.hydrocore.estoque.repository;

import android.content.Context;
import android.util.Log;

import com.vireya.hydrocore.estoque.api.ApiClient;
import com.vireya.hydrocore.estoque.api.ApiService;
import com.vireya.hydrocore.estoque.dao.ProdutoDao;
import com.vireya.hydrocore.estoque.db.AppDatabase;
import com.vireya.hydrocore.estoque.model.Produto;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdutoRepository {
    private ProdutoDao produtoDao;
    private ApiService apiService;

    public ProdutoRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        produtoDao = database.produtoDao();
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    //busca offline
    public List<Produto> getProdutosOffline() {
        return produtoDao.getAll();

    }
    //sincroniza com API e salva no banco
    public void syncProdutos(Runnable onFinish) {
        apiService.getProdutos().enqueue(new Callback<List<Produto>>() {

            @Override
            public void onResponse(Call<List<Produto>> call, Response<List<Produto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        try {
                            produtoDao.deleteAll();
                            produtoDao.insert(response.body());
                        } catch (Exception e) {
                            Log.e("ROOM_ERROR", "Erro ao salvar no banco: " + e.getMessage());
                        } finally {
                            if (onFinish != null) onFinish.run();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Produto>> call, Throwable t) {
                Log.e("API_DEBUG", "Falha na requisição: " + t.getMessage());
            }

        });
    }


}
