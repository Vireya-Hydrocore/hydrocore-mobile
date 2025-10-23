package com.vireya.hydrocore.entrada.model;

public class Login {
    private String email;
    private String senha;
    private String codigoEmpresa;
//    private String token;

    public Login(String email, String senha, String codigoEmpresa) {
        this.email = email;
        this.senha = senha;
        this.codigoEmpresa = codigoEmpresa;
    }

    

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(String codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }
}
