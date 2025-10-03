package com.vireya.hydrocore.ui.configuracoes.model;

import java.util.Date;

public class Funcionario {

    private int idFuncionario;
    private String nome;
    private String email;
    private Date dataAdmissao;
    private Date dataNascimento;
    private int idEta;
    private String cargo;

    // getters e setters


    public Funcionario(int idFuncionario, String nome, String email, Date dataAdmissao, Date dataNascimento, int idEta, String cargo) {
        this.idFuncionario = idFuncionario;
        this.nome = nome;
        this.email = email;
        this.dataAdmissao = dataAdmissao;
        this.dataNascimento = dataNascimento;
        this.idEta = idEta;
        this.cargo = cargo;
    }

    public int getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
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

    public int getIdEta() {
        return idEta;
    }

    public void setIdEta(int idEta) {
        this.idEta = idEta;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

}

