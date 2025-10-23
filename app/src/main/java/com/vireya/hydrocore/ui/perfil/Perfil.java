package com.vireya.hydrocore.ui.perfil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.vireya.hydrocore.R;
import com.vireya.hydrocore.core.network.RetrofitClient;
import com.vireya.hydrocore.tarefas.api.TarefasApi;
import com.vireya.hydrocore.tarefas.model.Tarefa;
import com.vireya.hydrocore.ui.configuracoes.api.ApiService;
import com.vireya.hydrocore.ui.configuracoes.model.Funcionario;
import com.vireya.hydrocore.ui.perfil.api.ApiFuncionario;
import com.vireya.hydrocore.ui.perfil.model.Estatistica;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Perfil extends Fragment {

    private TextView tarDiariaValor, tarNaoFeitasValor, tarTotaisValor, txtNome, txtCargo;
    private ShapeableImageView imgPerfil;
    private LineChart graficoProdutividade;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Usamos ScrollView para permitir rolagem caso o gráfico ocupe mais espaço
        ScrollView scrollView = (ScrollView) inflater.inflate(R.layout.fragment_perfil, container, false);
        return scrollView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgPerfil = view.findViewById(R.id.imgPerfil);
        tarDiariaValor = view.findViewById(R.id.tarDiariaValor);
        tarNaoFeitasValor = view.findViewById(R.id.tarNaoFeitasValor);
        tarTotaisValor = view.findViewById(R.id.tarTotaisValor);
        txtNome = view.findViewById(R.id.txtNome);
        txtCargo = view.findViewById(R.id.txtCargo);
        graficoProdutividade = view.findViewById(R.id.graficoProdutividade);

        LinearLayout layoutNaoFeitas = view.findViewById(R.id.layoutNaoFeitas);
        layoutNaoFeitas.setOnClickListener(v -> {
            BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.nav_view);
            bottomNavigationView.setSelectedItemId(R.id.navigation_tarefas);
        });

        loadProfileImage();
        loadFuncionarioInfo();
        loadTarefasStats();
        loadGraficoProdutividade(1);
    }

    // --- Imagem do perfil local ---
    private void loadProfileImage() {
        String filename = requireContext()
                .getSharedPreferences("configuracoes", android.content.Context.MODE_PRIVATE)
                .getString("profileImage", null);

        if (filename != null) {
            try (FileInputStream fis = requireContext().openFileInput(filename)) {
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                imgPerfil.setImageBitmap(bitmap);
            } catch (Exception e) {
                Log.e("Perfil", "Erro ao carregar imagem do perfil", e);
            }
        }
    }

    // --- Estatísticas básicas de tarefas ---
    private void loadTarefasStats() {
        TarefasApi api = RetrofitClient.getTarefasApi();
        api.listarTarefasPorNome("Lucas Pereira").enqueue(new Callback<List<Tarefa>>() {
            @Override
            public void onResponse(Call<List<Tarefa>> call, Response<List<Tarefa>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int feitas = 0, naoFeitas = 0;
                    for (Tarefa t : response.body()) {
                        if ("CONCLUIDA".equalsIgnoreCase(t.getStatus())) feitas++;
                        else naoFeitas++;
                    }

                    int totais = response.body().size();
                    tarDiariaValor.setText(String.valueOf(feitas));
                    tarNaoFeitasValor.setText(String.valueOf(naoFeitas));
                    tarTotaisValor.setText(String.valueOf(totais));
                }
            }

            @Override
            public void onFailure(Call<List<Tarefa>> call, Throwable t) {
                Log.e("Perfil", "Erro ao carregar tarefas", t);
            }
        });
    }

    // --- Nome e cargo ---
    public void loadFuncionarioInfo() {
        ApiService apiService = RetrofitClient.getRetrofit().create(ApiService.class);

        apiService.getFuncionarios().enqueue(new Callback<List<Funcionario>>() {
            @Override
            public void onResponse(Call<List<Funcionario>> call, Response<List<Funcionario>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Funcionario funcionario = response.body().get(0);
                    txtNome.setText(funcionario.getNome());
                    txtCargo.setText(String.valueOf(funcionario.getCargo()));
                }
            }

            @Override
            public void onFailure(Call<List<Funcionario>> call, Throwable t) {
                Log.e("Perfil", "Erro ao carregar funcionário", t);
            }
        });
    }

    // --- Gráfico de produtividade ---
    private void loadGraficoProdutividade(int funcionarioId) {
        ApiFuncionario apiService = RetrofitClient.getRetrofit().create(ApiFuncionario.class);

        apiService.getResumoTarefas(funcionarioId).enqueue(new Callback<Estatistica>() {
            @Override
            public void onResponse(Call<Estatistica> call, Response<Estatistica> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Estatistica dados = response.body();
                    Log.d("GRAFICO_API", "Resumo recebido: " + new Gson().toJson(dados));

                    if (dados.getTarefasFeitas() == 0 && dados.getTarefasTotais() == 0) {
                        Log.w("GRAFICO_API", "Nenhum dado real. Usando valores simulados.");
                        dados = new Estatistica(4, 8, 2, 10);
                    }

                    atualizarGrafico(dados);
                } else {
                    Log.e("GRAFICO_API", "Erro: " + response.code());
                    atualizarGrafico(new Estatistica(4, 8, 2, 10)); // fallback
                }
            }

            @Override
            public void onFailure(Call<Estatistica> call, Throwable t) {
                Log.e("GRAFICO_API", "Falha na API: " + t.getMessage());
                atualizarGrafico(new Estatistica(4, 8, 2, 10)); // fallback
            }
        });
    }

    private void atualizarGrafico(Estatistica dados) {
        graficoProdutividade.setVisibility(View.VISIBLE);

        float produtividadeDiaria = 0f;
        float eficienciaTotal = 0f;
        float taxaFalha = 0f;

        if (dados.getTarefasParaHoje() > 0)
            produtividadeDiaria = (float) dados.getTarefasFeitas() / dados.getTarefasParaHoje() * 100f;

        if (dados.getTarefasTotais() > 0) {
            eficienciaTotal = (float) dados.getTarefasFeitas() / dados.getTarefasTotais() * 100f;
            taxaFalha = (float) dados.getTarefasNaoRealizadas() / dados.getTarefasTotais() * 100f;
        }

        if (produtividadeDiaria == 0 && eficienciaTotal == 0 && taxaFalha == 0) {
            produtividadeDiaria = 50f;
            eficienciaTotal = 70f;
            taxaFalha = 15f;
        }

        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, produtividadeDiaria));
        entries.add(new Entry(1, eficienciaTotal));
        entries.add(new Entry(2, taxaFalha));

        LineDataSet dataSet = new LineDataSet(entries, "Produtividade (%)");
        dataSet.setLineWidth(3f);
        dataSet.setCircleRadius(5f);
        dataSet.setValueTextSize(12f);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setColor(Color.parseColor("#2E7D32"));
        dataSet.setCircleColor(Color.parseColor("#388E3C"));
        dataSet.setDrawFilled(true);
        dataSet.setFillAlpha(150);
        dataSet.setFillColor(Color.parseColor("#A5D6A7"));
        dataSet.setDrawValues(true);
        dataSet.setDrawHighlightIndicators(true);
        dataSet.setHighLightColor(Color.DKGRAY);

        LineData lineData = new LineData(dataSet);
        graficoProdutividade.setData(lineData);

        // Eixo X com rótulos
        String[] labels = {"Hoje", "Eficiência", "Falhas"};
        XAxis xAxis = graficoProdutividade.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        graficoProdutividade.setDrawBorders(true);
        graficoProdutividade.setBorderColor(Color.LTGRAY);
        graficoProdutividade.setBorderWidth(2f);
        graficoProdutividade.getAxisRight().setEnabled(false);

        graficoProdutividade.getDescription().setText("Índice de produtividade (%)");
        graficoProdutividade.getDescription().setTextSize(10f);

        Legend legend = graficoProdutividade.getLegend();
        legend.setTextSize(12f);
        legend.setTextColor(Color.DKGRAY);

        graficoProdutividade.animateY(1200);
        graficoProdutividade.invalidate();
    }
}
