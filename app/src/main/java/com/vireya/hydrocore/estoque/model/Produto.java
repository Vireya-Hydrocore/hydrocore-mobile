package com.vireya.hydrocore.estoque.model;

import com.google.gson.annotations.SerializedName;

public class Produto {
    @SerializedName("nomeProduto")
    private String nome;
    private int quantidade;
    private String status;

    public Produto(String nome, int quantidade, String status) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.status = status;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
