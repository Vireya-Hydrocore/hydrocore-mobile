package com.vireya.hydrocore.relatorio.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vireya.hydrocore.R;
import com.vireya.hydrocore.relatorio.api.RelatorioApi;
import com.vireya.hydrocore.relatorio.model.RelatorioDetalhado;
import com.vireya.hydrocore.relatorio.model.RelatorioResumo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RelatorioAdapter extends RecyclerView.Adapter<RelatorioAdapter.RelatorioViewHolder> {

    private List<RelatorioResumo> lista;
    private RelatorioApi api;

    public RelatorioAdapter(List<RelatorioResumo> lista, RelatorioApi api) {
        this.lista = lista;
        this.api = api;
    }

    @NonNull
    @Override
    public RelatorioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_relatorio, parent, false);
        return new RelatorioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatorioViewHolder holder, int position) {
        RelatorioResumo relatorio = lista.get(position);
        holder.textTitulo.setText(relatorio.getData());

        holder.btnDownload.setOnClickListener(v -> {
            try {
                String[] partes = relatorio.getData().split(" ");
                String mes = partes[0];
                int ano = Integer.parseInt(partes[1]);

                api.buscarRelatorioPorMesAno(mes, ano).enqueue(new Callback<RelatorioDetalhado>() {
                    @Override
                    public void onResponse(Call<RelatorioDetalhado> call, Response<RelatorioDetalhado> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            RelatorioDetalhado detalhe = response.body();
                            Toast.makeText(v.getContext(),
                                    "Relatório: " + detalhe.getNome() + "\nCidade: " + detalhe.getCidade(),
                                    Toast.LENGTH_LONG).show();
                            // aqui você pode chamar o PdfGenerator ou abrir uma nova tela
                        } else {
                            Toast.makeText(v.getContext(), "Erro ao carregar detalhes", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RelatorioDetalhado> call, Throwable t) {
                        Toast.makeText(v.getContext(), "Falha: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Toast.makeText(v.getContext(), "Erro ao processar data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
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
