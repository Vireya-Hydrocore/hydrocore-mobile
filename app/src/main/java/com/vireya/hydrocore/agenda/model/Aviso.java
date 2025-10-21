package com.vireya.hydrocore.agenda.model;

public class Aviso {
    private int id;
    private String descricao;
    private String dataOcorrencia;
    private String nomeEta;
    private int idPrioridade;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDataOcorrencia() {
        return dataOcorrencia;
    }

    public void setDataOcorrencia(String dataOcorrencia) {
        this.dataOcorrencia = dataOcorrencia;
    }

    public String getNomeEta() {
        return nomeEta;
    }

    public void setNomeEta(String nomeEta) {
        this.nomeEta = nomeEta;
    }

    public int getIdPrioridade() {
        return idPrioridade;
    }

    public void setIdPrioridade(int idPrioridade) {
        this.idPrioridade = idPrioridade;
    }
}

