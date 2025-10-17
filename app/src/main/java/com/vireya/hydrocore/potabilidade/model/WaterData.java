package com.vireya.hydrocore.potabilidade.model;

public class WaterData {
    public double ph;
    public double Hardness;
    public double Solids;
    public double Chloramines;
    public double Sulfate;
    public double Conductivity;
    public double Organic_carbon;
    public double Trihalomethanes;
    public double Turbidity;

    public WaterData(double ph, double hardness, double solids, double chloramines,
                     double sulfate, double conductivity, double organic_carbon,
                     double trihalomethanes, double turbidity) {
        this.ph = ph;
        this.Hardness = hardness;
        this.Solids = solids;
        this.Chloramines = chloramines;
        this.Sulfate = sulfate;
        this.Conductivity = conductivity;
        this.Organic_carbon = organic_carbon;
        this.Trihalomethanes = trihalomethanes;
        this.Turbidity = turbidity;
    }
}

