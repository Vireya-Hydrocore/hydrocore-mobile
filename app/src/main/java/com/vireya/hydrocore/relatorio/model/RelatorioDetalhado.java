package com.vireya.hydrocore.relatorio.model;

public class RelatorioDetalhado {
    private int id;
    private double volumeTratado;
    private String nome;
    private String nomeAdmin;
    private String cidade;
    private String estado;
    private String bairro;
    private double phMin;
    private double phMax;
    private String comentarioGerente;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getVolumeTratado() { return volumeTratado; }
    public void setVolumeTratado(double volumeTratado) { this.volumeTratado = volumeTratado; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getNomeAdmin() { return nomeAdmin; }
    public void setNomeAdmin(String nomeAdmin) { this.nomeAdmin = nomeAdmin; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public double getPhMin() { return phMin; }
    public void setPhMin(double phMin) { this.phMin = phMin; }

    public double getPhMax() { return phMax; }
    public void setPhMax(double phMax) { this.phMax = phMax; }

    public String getComentarioGerente() { return comentarioGerente; }
    public void setComentarioGerente(String comentarioGerente) { this.comentarioGerente = comentarioGerente; }
}

