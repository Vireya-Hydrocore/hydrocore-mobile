package com.vireya.hydrocore.relatorio;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.widget.Toast;

import com.vireya.hydrocore.relatorio.model.RelatorioDetalhado;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfGenerator {

    public void gerarPdf(Context context, RelatorioDetalhado relatorio) {
        PdfDocument document = new PdfDocument();
        Paint titlePaint = new Paint();
        Paint textPaint = new Paint();

        titlePaint.setTextSize(18);
        titlePaint.setColor(Color.rgb(0, 102, 153));
        titlePaint.setFakeBoldText(true);

        textPaint.setTextSize(12);
        textPaint.setColor(Color.BLACK);

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        int x = 50, y = 80;

        canvas.drawText("Relatório Mensal – Estação de Tratamento de Água (ETA)", x, y, titlePaint);
        y += 40;
        canvas.drawText("Empresa: HydroCore", x, y, textPaint);
        y += 20;
        canvas.drawText("Unidade: " + relatorio.getCidade() + " - " + relatorio.getEstado(), x, y, textPaint);
        y += 20;
        canvas.drawText("Responsável técnico: " + relatorio.getNomeAdmin(), x, y, textPaint);
        y += 30;

        titlePaint.setTextSize(16);
        canvas.drawText("Produção de Água Tratada", x, y, titlePaint);
        y += 25;
        canvas.drawText("Volume Total Tratado: " + relatorio.getVolumeTratado() + " m³", x, y, textPaint);
        y += 20;
        canvas.drawText("pH Mínimo: " + relatorio.getPhMin(), x, y, textPaint);
        y += 20;
        canvas.drawText("pH Máximo: " + relatorio.getPhMax(), x, y, textPaint);
        y += 30;

        titlePaint.setTextSize(16);
        canvas.drawText("Comentários do Gerente", x, y, titlePaint);
        y += 25;
        canvas.drawText(relatorio.getComentarioGerente(), x, y, textPaint);

        document.finishPage(page);

        File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(downloads, "Relatorio_" + relatorio.getNome() + "_" + relatorio.getId() + ".pdf");

        try {
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();
            Toast.makeText(context, "PDF salvo em: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Erro ao gerar PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
