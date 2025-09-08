package com.vireya.hydrocore.relatorio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vireya.hydrocore.R;

import java.util.List;

public class RelatorioAdapter extends RecyclerView.Adapter<RelatorioAdapter.RelatorioViewHolder> {

    private List<String> relatorios;
    private OnRelatorioClickListener listener;


    // Interface para lidar com os cliques no botão de download
    public interface OnRelatorioClickListener {
        void onRelatorioClick(String relatorio);
    }


    public RelatorioAdapter(List<String> relatorios, OnRelatorioClickListener listener) {
        this.relatorios = relatorios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RelatorioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Aqui ele "cola" o item_relatorio dentro da lista
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_relatorio, parent, false);
        return new RelatorioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatorioViewHolder holder, int position) {
        String relatorio = relatorios.get(position);
        holder.textTitulo.setText(relatorio);

        holder.btnDownload.setOnClickListener(v -> listener.onRelatorioClick(relatorio));
    }

    @Override
    public int getItemCount() {
        return relatorios.size();
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