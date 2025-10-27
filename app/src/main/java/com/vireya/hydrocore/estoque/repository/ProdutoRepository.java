package com.vireya.hydrocore.estoque.repository;

import android.content.Context;
import android.util.Log;

import com.vireya.hydrocore.estoque.api.ApiClient;
import com.vireya.hydrocore.estoque.api.ApiService;
import com.vireya.hydrocore.estoque.dao.ProdutoDao;
import com.vireya.hydrocore.estoque.db.AppDatabase;
import com.vireya.hydrocore.estoque.model.Produto;
import com.vireya.hydrocore.estoque.model.ProdutoResponse;
import com.vireya.hydrocore.funcionario.model.Funcionario;
import com.vireya.hydrocore.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdutoRepository {
    private ProdutoDao produtoDao;
    private ApiService apiService;

    private Context context;



    public ProdutoRepository(Context context) {
        this.context = context;
        AppDatabase database = AppDatabase.getInstance(context);
        produtoDao = database.produtoDao();
        apiService = ApiClient.getClient(context).create(ApiService.class);
    }

    //busca offline
    public List<Produto> getProdutosOffline() {
        return produtoDao.getAll();

    }
    //sincroniza com API e salva no banco
    public void syncProdutos(Runnable onFinish) {
        SessionManager session = new SessionManager(context);
        String email = session.getEmail();

        apiService.getFuncionarioPorEmail(email).enqueue(new Callback<Funcionario>() {
            @Override
            public void onResponse(Call<Funcionario> call, Response<Funcionario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int funcionarioId = response.body().getId();

                    apiService.getFuncionarioPorId(funcionarioId).enqueue(new Callback<Funcionario>() {
                        @Override
                        public void onResponse(Call<Funcionario> call, Response<Funcionario> responseId) {
                            if (responseId.isSuccessful() && responseId.body() != null) {
                                String eta = responseId.body().getEta();

                                apiService.getProdutosPorEta(eta).enqueue(new Callback<List<ProdutoResponse>>() {
                                    @Override
                                    public void onResponse(Call<List<ProdutoResponse>> call, Response<List<ProdutoResponse>> responseProdutos) {
                                        if (responseProdutos.isSuccessful() && responseProdutos.body() != null) {
                                            Executors.newSingleThreadExecutor().execute(() -> {
                                                try {
                                                    produtoDao.deleteAll();

                                                    // Converter ProdutoResponse -> Produto
                                                    List<Produto> produtosParaBanco = new ArrayList<>();
                                                    for (ProdutoResponse pr : responseProdutos.body()) {
                                                        for (String nomeProduto : pr.getProdutos()) {
                                                            produtosParaBanco.add(new Produto(nomeProduto, 0, "Suficiente"));
                                                        }
                                                    }

                                                    produtoDao.insert(produtosParaBanco);

                                                } catch (Exception e) {
                                                    Log.e("ROOM_ERROR", "Erro ao salvar no banco: " + e.getMessage());
                                                } finally {
                                                    if (onFinish != null) onFinish.run();
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<ProdutoResponse>> call, Throwable t) {
                                        Log.e("API_DEBUG", "Falha ao buscar produtos: " + t.getMessage());
                                    }
                                });

                            }
                        }

                        @Override
                        public void onFailure(Call<Funcionario> call, Throwable t) {}
                    });

                }
            }

            @Override
            public void onFailure(Call<Funcionario> call, Throwable t) {}
        });
    }



}
