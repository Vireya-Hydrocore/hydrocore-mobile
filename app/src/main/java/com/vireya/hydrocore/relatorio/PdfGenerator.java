package com.vireya.hydrocore.relatorio;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.core.app.NotificationCompat;

import com.vireya.hydrocore.R;
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
        canvas.drawText("Unidade: " + safe(relatorio.getCidade()) + " - " + safe(relatorio.getEstado()), x, y, textPaint);
        y += 20;
        canvas.drawText("Responsável técnico: " + safe(relatorio.getNomeAdmin()), x, y, textPaint);
        y += 30;

        titlePaint.setTextSize(16);
        canvas.drawText("Produção de Água Tratada", x, y, titlePaint);
        y += 25;
        canvas.drawText("Volume Total Tratado: " + safe(relatorio.getVolumeTratado()) + " m³", x, y, textPaint);
        y += 20;
        canvas.drawText("pH Mínimo: " + safe(relatorio.getPhMin()), x, y, textPaint);
        y += 20;
        canvas.drawText("pH Máximo: " + safe(relatorio.getPhMax()), x, y, textPaint);
        y += 30;

        titlePaint.setTextSize(16);
        canvas.drawText("Comentários do Gerente", x, y, titlePaint);
        y += 25;
        canvas.drawText(safe(relatorio.getComentarioGerente()), x, y, textPaint);

        document.finishPage(page);

        File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!downloads.exists()) downloads.mkdirs();
        File file = new File(downloads, "Relatorio_" + safe(relatorio.getNome()) + "_" + relatorio.getId() + ".pdf");

        try {
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();

            Toast.makeText(context, "PDF salvo em Downloads", Toast.LENGTH_SHORT).show();

            Uri uri = FileProvider.getUriForFile(context,
                    context.getPackageName() + ".provider", file);

            Intent openIntent = new Intent(Intent.ACTION_VIEW);
            openIntent.setDataAndType(uri, "application/pdf");
            openIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            context.startActivity(openIntent);

            showPdfNotification(context, uri, file.getName());

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Erro ao gerar PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showPdfNotification(Context context, Uri uri, String fileName) {
        String channelId = "pdf_channel";
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "PDFs Gerados", NotificationManager.IMPORTANCE_DEFAULT);
            nm.createNotificationChannel(channel);
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setContentTitle("PDF gerado com sucesso")
                .setContentText("Toque para abrir " + fileName)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        nm.notify(1, builder.build());
    }

    private String safe(Object text) {
        return text == null ? "—" : text.toString();
    }
}
