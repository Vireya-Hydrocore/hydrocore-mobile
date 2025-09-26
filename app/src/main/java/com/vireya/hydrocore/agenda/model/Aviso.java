package com.vireya.hydrocore.agenda.model;

public class Aviso {
    private int idAvisos;
    private String descricao;
    private String dataOcorrencia;
    private int idEta;
    private int idPrioridade;

    // getters e setters

    public int getIdAvisos() {
        return idAvisos;
    }

    public void setIdAvisos(int idAvisos) {
        this.idAvisos = idAvisos;
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

    public int getIdEta() {
        return idEta;
    }

    public void setIdEta(int idEta) {
        this.idEta = idEta;
    }

    public int getIdPrioridade() {
        return idPrioridade;
    }

    public void setIdPrioridade(int idPrioridade) {
        this.idPrioridade = idPrioridade;
    }
}

