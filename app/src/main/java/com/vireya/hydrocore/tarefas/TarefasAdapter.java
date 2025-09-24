package com.vireya.hydrocore.tarefas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.widget.TextView;

import com.vireya.hydrocore.R;


public class TarefasAdapter extends RecyclerView.Adapter<TarefasAdapter.ViewHolder> {
    private List<Tarefa> tarefas;

    public TarefasAdapter(List<Tarefa> listaTarefas) {
        this.tarefas = listaTarefas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_tarefa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tarefa tarefa = tarefas.get(position);
        holder.titulo.setText(tarefa.getTitulo());
        holder.descricao.setText(tarefa.getDescricao());
        holder.data.setText(tarefa.getPrazo().toString());
    }

    @Override
    public int getItemCount() {
        return tarefas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, descricao, data;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.txtTitulo);
            descricao = itemView.findViewById(R.id.txtDescricao);
            data = itemView.findViewById(R.id.txtPrazoData);
        }
    }

}

