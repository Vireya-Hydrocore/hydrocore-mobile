package com.vireya.hydrocore.ui.configuracoes.model;

import java.util.Date;

public class Funcionario {

    private int id;
    private String nome;
    private String email;
    private Date dataAdmissao;
    private Date dataNascimento;
    private String eta;
    private String cargo;

    // getters e setters


    public Funcionario(int id, String nome, String email, Date dataAdmissao, Date dataNascimento, String eta, String cargo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.dataAdmissao = dataAdmissao;
        this.dataNascimento = dataNascimento;
        this.eta = eta;
        this.cargo = cargo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(Date dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

}

