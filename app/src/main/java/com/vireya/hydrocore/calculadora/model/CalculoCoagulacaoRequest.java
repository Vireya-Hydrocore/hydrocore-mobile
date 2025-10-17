package com.vireya.hydrocore.calculadora.model;

public class CalculoCoagulacaoRequest {
    private double turbidez;
    private double ph;
    private String cor;
    private double volume;
    private double alumina;
    private double alcalinidade;
    private String produtoQuimico;

    public CalculoCoagulacaoRequest(double turbidez, double ph, String cor,
                                    double volume, double alumina, double alcalinidade,
                                    String produtoQuimico) {
        this.turbidez = turbidez;
        this.ph = ph;
        this.cor = cor;
        this.volume = volume;
        this.alumina = alumina;
        this.alcalinidade = alcalinidade;
        this.produtoQuimico = produtoQuimico;
    }

    // Getters
    public double getTurbidez() { return turbidez; }
    public double getPh() { return ph; }
    public String getCor() { return cor; }
    public double getVolume() { return volume; }
    public double getAlumina() { return alumina; }
    public double getAlcalinidade() { return alcalinidade; }
    public String getProdutoQuimico() { return produtoQuimico; }
}
