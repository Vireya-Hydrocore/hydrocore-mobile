package com.vireya.hydrocore.entrada.model;

public class Login {
    private String email;
    private String password;
    private String codigoEmpresa;
//    private String token;

    public Login(String email, String password, String codigoEmpresa) {
        this.email = email;
        this.password = password;
        this.codigoEmpresa = codigoEmpresa;
    }

    

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(String codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }
}
