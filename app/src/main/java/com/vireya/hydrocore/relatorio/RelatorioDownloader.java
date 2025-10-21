package com.vireya.hydrocore.relatorio;

import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RelatorioDownloader {

    private static final String BASE_URL = "http://192.168.0.105:8080/relatorios/pdf";

    public static void baixarRelatorioPdf(Context context, int mes, int ano) {
        new Thread(() -> {
            try {
                String urlStr = BASE_URL + "?mes=" + mes + "&ano=" + ano;
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() == 200) {
                    InputStream input = conn.getInputStream();

                    File dir = new File(context.getExternalFilesDir(null), "relatorios");
                    if (!dir.exists()) dir.mkdirs();

                    File file = new File(dir, "relatorio_" + mes + "_" + ano + ".pdf");
                    FileOutputStream output = new FileOutputStream(file);

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = input.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }

                    output.close();
                    input.close();

                    ((FragmentActivity) context).runOnUiThread(() ->
                            Toast.makeText(context, "Relatório salvo em: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show());
                } else {
                    ((FragmentActivity) context).runOnUiThread(() ->
                            Toast.makeText(context, "Erro ao gerar relatório", Toast.LENGTH_SHORT).show());
                }

            } catch (Exception e) {
                ((FragmentActivity) context).runOnUiThread(() ->
                        Toast.makeText(context, "Falha: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    public static int converterMesParaNumero(String mes) {
        switch (mes.toLowerCase()) {
            case "janeiro": return 1;
            case "fevereiro": return 2;
            case "março": return 3;
            case "abril": return 4;
            case "maio": return 5;
            case "junho": return 6;
            case "julho": return 7;
            case "agosto": return 8;
            case "setembro": return 9;
            case "outubro": return 10;
            case "novembro": return 11;
            case "dezembro": return 12;
            default: return 1;
        }
    }
}
