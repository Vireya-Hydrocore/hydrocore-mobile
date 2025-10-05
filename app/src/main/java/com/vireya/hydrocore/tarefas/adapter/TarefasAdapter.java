package com.vireya.hydrocore.tarefas.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vireya.hydrocore.R;
import com.vireya.hydrocore.tarefas.model.Tarefa;

import java.util.ArrayList;
import java.util.List;

public class TarefasAdapter extends RecyclerView.Adapter<TarefasAdapter.ViewHolder> {

    private List<Tarefa> tarefas;

    public TarefasAdapter(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
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

        holder.titulo.setText(tarefa.getNivel());
        holder.descricao.setText(tarefa.getDescricao());

        boolean concluido = tarefa.getStatus() != null &&
                tarefa.getStatus().equalsIgnoreCase("concluída");

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(concluido);

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tarefa.setStatus("concluída");
            } else {
                tarefa.setStatus("pendente");
            }

        });

    }

    @Override
    public int getItemCount() {
        return tarefas.size();
    }

    public void setTarefas(List<Tarefa> listaTarefas) {
        this.tarefas = listaTarefas;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, descricao;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.txtTitulo);
            descricao = itemView.findViewById(R.id.txtDescricao);
            checkBox = itemView.findViewById(R.id.tarefaCheck);
        }
    }

    public List<Tarefa> getTarefasSelecionadas() {
        List<Tarefa> selecionadas = new ArrayList<>();
        for (Tarefa t : tarefas) {
            if (t.getStatus() != null && t.getStatus().equalsIgnoreCase("concluída")) {
                selecionadas.add(t);
            }
        }
        return selecionadas;
    }



}