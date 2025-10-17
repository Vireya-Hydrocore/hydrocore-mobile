package com.vireya.hydrocore.calculadora.model;

public class CalculoResponse {

    private String produto;
    private double quantidade;

    public String getProduto() {
        return produto;
    }

    public double getQuantidade() {
        return quantidade;
    }

    @Override
    public String toString() {
        return "CalculoResponse{" +
                "produto='" + produto + '\'' +
                ", quantidade=" + quantidade +
                '}';
    }
}
