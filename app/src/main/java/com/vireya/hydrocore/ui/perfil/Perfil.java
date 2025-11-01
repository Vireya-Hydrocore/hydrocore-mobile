package com.vireya.hydrocore.ui.perfil;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
import com.vireya.hydrocore.funcionario.model.Funcionario;
import com.vireya.hydrocore.tarefas.api.TarefasApi;
import com.vireya.hydrocore.tarefas.model.Tarefa;
import com.vireya.hydrocore.ui.configuracoes.api.ApiService;
import com.vireya.hydrocore.ui.perfil.api.ApiFuncionario;
import com.vireya.hydrocore.ui.perfil.model.Estatistica;
import com.vireya.hydrocore.utils.SessionManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Perfil extends Fragment {

    private static final int REQUEST_CAMERA = 100;
    private static final int REQUEST_GALLERY = 101;

    private ShapeableImageView imgPerfil;
    private ImageView btnEditarFoto;
    private TextView tarDiariaValor, tarNaoFeitasValor, tarTotaisValor, txtNome, txtCargo;
    private LineChart graficoProdutividade;
    private ApiFuncionario funcionario;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgPerfil = view.findViewById(R.id.imgPerfil);
        btnEditarFoto = view.findViewById(R.id.btnEditarFoto);
        tarDiariaValor = view.findViewById(R.id.tarDiariaValor);
        tarNaoFeitasValor = view.findViewById(R.id.tarNaoFeitasValor);
        tarTotaisValor = view.findViewById(R.id.tarTotaisValor);
        txtNome = view.findViewById(R.id.txtNome);
        txtCargo = view.findViewById(R.id.txtCargo);
        graficoProdutividade = view.findViewById(R.id.graficoProdutividade);

        checkPermissions();
        loadProfileImage();
        loadFuncionarioInfo();


        btnEditarFoto.setOnClickListener(v -> showImageOptions());

        LinearLayout layoutNaoFeitas = view.findViewById(R.id.layoutNaoFeitas);
        layoutNaoFeitas.setOnClickListener(v -> {
            BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.nav_view);
            bottomNavigationView.setSelectedItemId(R.id.navigation_tarefas);
        });
    }

    private void showImageOptions() {
        String[] options = {"Visualizar foto", "Tirar foto", "Escolher da galeria"};

        new AlertDialog.Builder(requireContext())
                .setTitle("Foto de Perfil")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) visualizarFoto();
                    else if (which == 1) openCamera();
                    else openGallery();
                }).show();
    }

    private void visualizarFoto() {
        imgPerfil.setDrawingCacheEnabled(true);
        Bitmap bitmap = imgPerfil.getDrawingCache();

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        ImageView imageView = new ImageView(requireContext());
        imageView.setImageBitmap(bitmap);
        builder.setView(imageView);
        builder.setPositiveButton("Fechar", null);
        builder.show();
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == requireActivity().RESULT_OK && data != null) {
            if (requestCode == REQUEST_CAMERA) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imgPerfil.setImageBitmap(imageBitmap);
                saveImageToInternalStorage(imageBitmap);

            } else if (requestCode == REQUEST_GALLERY) {
                Uri selectedImage = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImage);
                    imgPerfil.setImageBitmap(bitmap);
                    saveImageToInternalStorage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    200
            );
        }
    }

    private void saveImageToInternalStorage(Bitmap bitmap) {
        try {
            String filename = "profile_image.png";
            FileOutputStream fos = requireContext().openFileOutput(filename, android.content.Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            requireContext()
                    .getSharedPreferences("configuracoes", android.content.Context.MODE_PRIVATE)
                    .edit()
                    .putString("profileImage", filename)
                    .apply();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Erro ao salvar imagem", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadProfileImage() {
        String filename = requireContext()
                .getSharedPreferences("configuracoes", android.content.Context.MODE_PRIVATE)
                .getString("profileImage", null);

        if (filename != null) {
            try (FileInputStream fis = requireContext().openFileInput(filename)) {
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                imgPerfil.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                imgPerfil.setImageResource(R.drawable.perfil_default);
            }
        } else {
            imgPerfil.setImageResource(R.drawable.perfil_default);
        }
    }


    private void loadTarefasStats(String nomeFuncionario) {
        TarefasApi api = RetrofitClient.getTarefasApi(getContext());

        api.listarTarefasPorNome(nomeFuncionario, true).enqueue(new Callback<List<Tarefa>>() {
            @Override
            public void onResponse(Call<List<Tarefa>> call, Response<List<Tarefa>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Tarefa> tarefas = response.body();

                    int feitas = 0;
                    int naoFeitas = 0;

                    for (Tarefa t : tarefas) {
                        Log.d("STATUS", t.getStatus());
                        if ("concluída".equalsIgnoreCase(t.getStatus())) feitas++;
                        else naoFeitas++;
                    }

                    tarDiariaValor.setText(String.valueOf(feitas));
                    tarNaoFeitasValor.setText(String.valueOf(naoFeitas));
                    tarTotaisValor.setText(String.valueOf(tarefas.size()));
                } else {
                    Log.e("TAREFAS", "Erro na resposta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Tarefa>> call, Throwable t) {
                Log.e("TAREFAS", "Falha na API", t);
            }
        });
    }




    private void loadFuncionarioInfo() {
        ApiService apiService = RetrofitClient.getRetrofit(getContext()).create(ApiService.class);

        // Pega o e-mail da sessão
        SessionManager session = new SessionManager(requireContext());
        String email = session.getEmail();

        apiService.getFuncionarioByEmail(email).enqueue(new Callback<Funcionario>() {
            @Override
            public void onResponse(Call<Funcionario> call, Response<Funcionario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Funcionario funcionario = response.body();

                    Log.d("FUNCIONARIO", "Nome: " + funcionario.getNome());
                    Log.d("FUNCIONARIO", "Cargo: " + funcionario.getCargo());
                    Log.d("FUNCIONARIO", "ID: " + funcionario.getId());

                    txtNome.setText(funcionario.getNome());
                    txtCargo.setText(funcionario.getCargo());

                    // Atualiza os cards de tarefas
                    loadTarefasStats(funcionario.getNome());

                    // 👉 Chama o gráfico passando o ID certo
                    if (funcionario.getId() != 0) {
                        loadGraficoProdutividade(funcionario.getId());
                    } else {
                        Log.e("Perfil", "ID do funcionário retornou 0 — verifique o backend");
                    }

                } else {
                    Log.e("Perfil", "Funcionário não encontrado. Código: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Funcionario> call, Throwable t) {
                Log.e("Perfil", "Erro ao carregar funcionário", t);
            }
        });
    }


    private void loadGraficoProdutividade(int funcionarioId) {
        ApiFuncionario apiService = RetrofitClient.getRetrofit(getContext()).create(ApiFuncionario.class);

        apiService.getResumoTarefas(funcionarioId).enqueue(new Callback<Estatistica>() {
            @Override
            public void onResponse(Call<Estatistica> call, Response<Estatistica> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Estatistica dados = response.body();
                    atualizarGrafico(dados);
                } else {
                    Log.e("GRAFICO_API", "Erro: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Estatistica> call, Throwable t) {
                Log.e("GRAFICO_API", "Falha na API: " + t.getMessage());
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
        System.out.println(dados.getTarefasFeitas());
        System.out.println(dados.getTarefasParaHoje());
        System.out.println("produtividade diaria " +produtividadeDiaria);

        if (dados.getTarefasTotais() > 0) {
            eficienciaTotal = (float) dados.getTarefasFeitas() / dados.getTarefasTotais() * 100f;
            taxaFalha = (float) dados.getTarefasNaoRealizadas() / dados.getTarefasTotais() * 100f;
        System.out.println("eficiencia total "+eficienciaTotal);
        System.out.println("taxa" + taxaFalha);

        }

        if (produtividadeDiaria == 0 && eficienciaTotal == 0 && taxaFalha == 0) {
            graficoProdutividade.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Sem dados disponíveis para gerar o gráfico.", Toast.LENGTH_SHORT).show();
            return;
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
