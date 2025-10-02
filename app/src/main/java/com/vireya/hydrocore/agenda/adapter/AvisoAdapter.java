package com.vireya.hydrocore.agenda.adapter;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vireya.hydrocore.R;
import com.vireya.hydrocore.agenda.model.Aviso;

import java.util.List;

public class AvisoAdapter extends RecyclerView.Adapter<AvisoAdapter.AvisoViewHolder> {

    private List<Aviso> listaAvisos;

    public AvisoAdapter(List<Aviso> listaAvisos) {
        this.listaAvisos = listaAvisos;
    }

    @NonNull
    @Override
    public AvisoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule, parent, false);
        return new AvisoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AvisoViewHolder holder, int position) {
        Aviso aviso = listaAvisos.get(position);
        holder.txtDescricao.setText(aviso.getDescricao());

        switch (aviso.getIdPrioridade()) {
            case 1:
                holder.cardAviso.setCardBackgroundColor(Color.parseColor("#FFCDD2"));
                holder.txtPrioridade.setText("Prioridade Alta");
                break;
            case 2:
                holder.cardAviso.setCardBackgroundColor(Color.parseColor("#FFF9C4"));
                holder.txtPrioridade.setText("Prioridade Média");
                break;
            default:
                holder.cardAviso.setCardBackgroundColor(Color.parseColor("#C8E6C9"));
                holder.txtPrioridade.setText("Prioridade Baixa");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return listaAvisos != null ? listaAvisos.size() : 0;
    }

    public void updateList(List<Aviso> novosAvisos) {
        this.listaAvisos = novosAvisos;
        notifyDataSetChanged();
    }

    static class AvisoViewHolder extends RecyclerView.ViewHolder {
        TextView txtDescricao, txtPrioridade;
        CardView cardAviso;

        AvisoViewHolder(View itemView) {
            super(itemView);
            txtDescricao = itemView.findViewById(R.id.txtDescricao);
            txtPrioridade = itemView.findViewById(R.id.txtPrioridade);
            cardAviso = itemView.findViewById(R.id.cardAviso);
        }
    }
}
