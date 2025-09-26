package com.vireya.hydrocore.agenda.model;

public class Aviso {
    private int id_avisos;
    private String descricao;
    private String data_ocorrencia;
    private int id_eta;
    private int id_prioridade;

    // getters e setters
    public int getId_avisos() { return id_avisos; }
    public void setId_avisos(int id_avisos) { this.id_avisos = id_avisos; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getData_ocorrencia() { return data_ocorrencia; }
    public void setData_ocorrencia(String data_ocorrencia) { this.data_ocorrencia = data_ocorrencia; }

    public int getId_eta() { return id_eta; }
    public void setId_eta(int id_eta) { this.id_eta = id_eta; }

    public int getId_prioridade() { return id_prioridade; }
    public void setId_prioridade(int id_prioridade) { this.id_prioridade = id_prioridade; }
}

