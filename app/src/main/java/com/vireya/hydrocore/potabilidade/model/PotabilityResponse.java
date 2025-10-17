package com.vireya.hydrocore.potabilidade.model;

import org.json.JSONObject;

public class PotabilityResponse {
    public String potability;

    public static PotabilityResponse fromJson(String json) {
        PotabilityResponse response = new PotabilityResponse();
        try {
            JSONObject obj = new JSONObject(json);
            if (obj.has("Potability")) {
                response.potability = String.valueOf(obj.getInt("Potability"));

                if(response.potability.equals("0")){
                    response.potability = "Água não potável.";
                }
                else{
                    response.potability = "Água potável.";
                }
            } else {
                response.potability = "Desconhecido";
            }
        } catch (Exception e) {
            response.potability = "Erro parse JSON";
        }
        return response;
    }


    // Construtor vazio necessário para bibliotecas como Gson
    public PotabilityResponse() {}

    // Getter
    public String getPotability() {
        return potability;
    }

    // Setter (opcional, mas necessário para Gson ou Moshi)
    public void setPotability(String potability) {
        this.potability = potability;
    }
}

