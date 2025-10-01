package com.vireya.hydrocore.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.vireya.hydrocore.R;
import com.vireya.hydrocore.core.network.RetrofitClient;
import com.vireya.hydrocore.tarefas.api.TarefasApi;
import com.vireya.hydrocore.tarefas.model.Tarefa;

import java.io.FileInputStream;
import java.util.List;

public class Perfil extends Fragment {

    private ShapeableImageView imgPerfil;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgPerfil = view.findViewById(R.id.imgPerfil);
        TextView tarDiariaValor = view.findViewById(R.id.tarDiariaValor);
        TextView tarNaoFeitasValor = view.findViewById(R.id.tarNaoFeitasValor);
        TextView tarTotaisValor = view.findViewById(R.id.tarTotaisValor);
        loadProfileImage();
        loadTarefasStats(tarDiariaValor, tarNaoFeitasValor, tarTotaisValor);

    }

    private void loadProfileImage() {
        String filename = requireContext()
                .getSharedPreferences("configuracoes", android.content.Context.MODE_PRIVATE)
                .getString("profileImage", null);

        if (filename != null) {
            try {
                FileInputStream fis = requireContext().openFileInput(filename);
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                imgPerfil.setImageBitmap(bitmap);
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadTarefasStats(TextView tarDiariaValor, TextView tarNaoFeitasValor, TextView tarTotaisValor) {
        TarefasApi api = RetrofitClient.getTarefasApi();
        api.listarTarefasPorNome("Lucas Pereira").enqueue(new retrofit2.Callback<List<Tarefa>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Tarefa>> call, retrofit2.Response<List<Tarefa>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Tarefa> tarefas = response.body();

                    int feitas = 0;
                    int naoFeitas = 0;

                    for (Tarefa t : tarefas) {
                        if ("CONCLUIDA".equalsIgnoreCase(t.getStatus())) {
                            feitas++;
                        } else {
                            naoFeitas++;
                        }
                    }

                    int totais = tarefas.size();

                    // Atualiza os TextView
                    tarDiariaValor.setText(String.valueOf(feitas));
                    tarNaoFeitasValor.setText(String.valueOf(naoFeitas));
                    tarTotaisValor.setText(String.valueOf(totais));
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Tarefa>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }



}
