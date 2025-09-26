package com.vireya.hydrocore.tarefas.model;

import java.util.Date;

public class Tarefa {
    private int idTarefa;

    private String descricao;

    private Date dataCriacao;

    private Date dataConclusao;

    private String status;

    private int idFuncionario;

    private String nivel;

    // Construtor vazio para o Gson
    public Tarefa() {}

    public int getIdTarefa() { return idTarefa; }
    public String getDescricao() { return descricao; }
    public Date getDataCriacao() { return dataCriacao; }
    public Date getDataConclusao() { return dataConclusao; }
    public String getStatus() { return status; }
    public int getIdFuncionario() { return idFuncionario; }
    public String getNivel() { return nivel; }

    public void setIdTarefa(int idTarefa) { this.idTarefa = idTarefa; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setDataCriacao(Date dataCriacao) { this.dataCriacao = dataCriacao; }
    public void setDataConclusao(Date dataConclusao) { this.dataConclusao = dataConclusao; }
    public void setStatus(String status) { this.status = status; }
    public void setIdFuncionario(int idFuncionario) { this.idFuncionario = idFuncionario; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    // Helper para saber se está concluída
    public boolean isConcluida() {
        return status != null && status.equalsIgnoreCase("concluído");
    }

    // Helper para alternar status
    public void alternarStatus() {
        if (isConcluida()) {
            this.status = "pendente";
        } else {
            this.status = "concluído";
        }
    }
}