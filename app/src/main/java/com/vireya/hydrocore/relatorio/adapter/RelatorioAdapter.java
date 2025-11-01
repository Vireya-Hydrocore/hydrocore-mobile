package com.vireya.hydrocore.relatorio.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vireya.hydrocore.R;
import com.vireya.hydrocore.relatorio.PdfGenerator;
import com.vireya.hydrocore.relatorio.api.RelatorioApi;
import com.vireya.hydrocore.relatorio.model.RelatorioDetalhado;
import com.vireya.hydrocore.relatorio.model.RelatorioResumo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RelatorioAdapter extends RecyclerView.Adapter<RelatorioAdapter.RelatorioViewHolder> {

    private static final String TAG = "RelatorioAdapter";

    private final List<RelatorioResumo> lista;
    private final RelatorioApi api;

    public RelatorioAdapter(List<RelatorioResumo> lista, RelatorioApi api) {
        this.lista = lista;
        this.api = api;
    }

    @NonNull
    @Override
    public RelatorioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_relatorio, parent, false);
        return new RelatorioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatorioViewHolder holder, int position) {
        RelatorioResumo relatorio = lista.get(position);
        holder.textTitulo.setText(relatorio.getData() != null ? relatorio.getData() : "Relatório");

        holder.btnDownload.setOnClickListener(v -> {
            String data = relatorio.getData();
            if (data == null || data.trim().isEmpty()) {
                Toast.makeText(v.getContext(), "Data do relatório indisponível.", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] partes = data.trim().split("\\s+");
            if (partes.length < 2) {
                Toast.makeText(v.getContext(), "Formato de data inválido. Esperado: 'Mês Ano'.", Toast.LENGTH_SHORT).show();
                return;
            }

            String anoStr = partes[partes.length - 1];
            StringBuilder mesBuilder = new StringBuilder();
            for (int i = 0; i < partes.length - 1; i++) {
                if (i > 0) mesBuilder.append(" ");
                mesBuilder.append(partes[i]);
            }
            String mesNome = mesBuilder.toString().trim();

            int ano;
            try {
                ano = Integer.parseInt(anoStr);
            } catch (NumberFormatException e) {
                Log.e(TAG, "Ano inválido na string de data: " + anoStr, e);
                Toast.makeText(v.getContext(), "Ano inválido no formato de data.", Toast.LENGTH_SHORT).show();
                return;
            }

            int mesNumero = converterMesParaNumero(mesNome);
            if (mesNumero == -1) {
                Toast.makeText(v.getContext(), "Mês inválido: " + mesNome, Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d(TAG, "Preparando chamada detalhado — mesNome: '" + mesNome + "', mesNumero: " + mesNumero + ", ano: " + ano);

            Call<List<RelatorioDetalhado>> call = api.buscarRelatorioPorMesAno(mesNumero, ano);
            try {
                if (call != null && call.request() != null && call.request().url() != null) {
                    Log.d(TAG, "URL chamada: " + call.request().url().toString());
                }
            } catch (Exception ex) {
                Log.w(TAG, "Não foi possível obter URL da call: " + ex.getMessage());
            }

            call.enqueue(new Callback<List<RelatorioDetalhado>>() {
                @Override
                public void onResponse(Call<List<RelatorioDetalhado>> call, Response<List<RelatorioDetalhado>> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                        List<RelatorioDetalhado> listaDetalhes = response.body();
                        RelatorioDetalhado detalhe = listaDetalhes.get(0); // usa o primeiro

                        try {
                            PdfGenerator pdfGenerator = new PdfGenerator();
                            pdfGenerator.gerarPdf(v.getContext(), detalhe);
                            Toast.makeText(v.getContext(), "PDF gerado com sucesso!", Toast.LENGTH_SHORT).show();
                        } catch (Exception ex) {
                            Log.e(TAG, "Erro ao gerar PDF", ex);
                            Toast.makeText(v.getContext(), "Erro ao gerar PDF: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        int code = response.code();
                        Log.e(TAG, "Resposta inválida ao buscar detalhado: code=" + code);

                        if (response.errorBody() != null) {
                            try {
                                String errorBody = response.errorBody().string();
                                Log.e(TAG, "errorBody: " + errorBody);
                            } catch (IOException ioe) {
                                Log.e(TAG, "Não foi possível ler errorBody", ioe);
                            }
                        } else {
                            Log.e(TAG, "errorBody é null");
                        }

                        Toast.makeText(v.getContext(),
                                "Erro ao carregar detalhes do relatório (código " + code + "). Veja logs.",
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<List<RelatorioDetalhado>> call, Throwable t) {
                    Log.e(TAG, "Falha na chamada detalhado", t);
                    Toast.makeText(v.getContext(),
                            "Falha ao buscar detalhes: " + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    /**
     * Converte o nome do mês em português (maiúsculo ou minúsculo) para número (1-12).
     */
    private int converterMesParaNumero(String mesNome) {
        if (mesNome == null) return -1;

        Map<String, Integer> meses = new HashMap<>();
        meses.put("janeiro", 1);
        meses.put("fevereiro", 2);
        meses.put("março", 3);
        meses.put("marco", 3);
        meses.put("abril", 4);
        meses.put("maio", 5);
        meses.put("junho", 6);
        meses.put("julho", 7);
        meses.put("agosto", 8);
        meses.put("setembro", 9);
        meses.put("outubro", 10);
        meses.put("novembro", 11);
        meses.put("dezembro", 12);

        String mesFormatado = mesNome.toLowerCase(Locale.ROOT).trim();
        return meses.getOrDefault(mesFormatado, -1);
    }

    static class RelatorioViewHolder extends RecyclerView.ViewHolder {
        TextView textTitulo;
        ImageButton btnDownload;

        public RelatorioViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitulo = itemView.findViewById(R.id.textTitulo);
            btnDownload = itemView.findViewById(R.id.btnDownload);
        }
    }
}
