package com.vireya.hydrocore.entrada.model;

public class LoginResponse {

    private String token;
    private String chaveApi;

    public LoginResponse() {}

    public LoginResponse(String token, String chaveApi) {
        this.token = token;
        this.chaveApi = chaveApi;
    }

    public String getToken() {
        return token;
    }

    public String getChaveApi() {
        return chaveApi;
    }
}
