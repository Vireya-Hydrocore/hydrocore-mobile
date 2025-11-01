package com.vireya.hydrocore.calculadora.model;

public class CalculoFloculacaoRequest {

    private double turbidez;
    private double ph;
    private String cor;
    private String produtoQuimico;

    public CalculoFloculacaoRequest(double turbidez, double ph, String cor, String produtoQuimico) {
        this.turbidez = turbidez;
        this.ph = ph;
        this.cor = cor;
        this.produtoQuimico = produtoQuimico;
    }

    public double getTurbidez() {
        return turbidez;
    }

    public void setTurbidez(double turbidez) {
        this.turbidez = turbidez;
    }

    public double getPh() {
        return ph;
    }

    public void setPh(double ph) {
        this.ph = ph;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getProdutoQuimico() {
        return produtoQuimico;
    }

    public void setProdutoQuimico(String produtoQuimico) {
        this.produtoQuimico = produtoQuimico;
    }
}
