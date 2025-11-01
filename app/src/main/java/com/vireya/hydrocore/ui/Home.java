package com.vireya.hydrocore.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vireya.hydrocore.R;

public class Home extends Fragment {

    private WebView webGrafico1;
    private LinearLayout avisosGerais;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // infla o layout correto
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // inicializa os elementos da view
        webGrafico1 = view.findViewById(R.id.webGrafico1);
        avisosGerais = view.findViewById(R.id.avisosGerais);

        // configuração do WebView
        if (webGrafico1 != null) {
            WebSettings webSettings = webGrafico1.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webGrafico1.setWebViewClient(new WebViewClient());
            // aqui você pode carregar sua URL ou gráfico
            webGrafico1.loadUrl("https://app.powerbi.com/view?r=eyJrIjoiODlhOTZmMzUtY2VjMi00OTI4LWFiY2YtMzVjNmFhYTY3NDRhIiwidCI6ImIxNDhmMTRjLTIzOTctNDAyYy1hYjZhLTFiNDcxMTE3N2FjMCJ9"); // substitua pelo gráfico real
        }

        // exemplo: clique no card de avisos
        if (avisosGerais != null) {
            avisosGerais.setOnClickListener(v ->
                    // exemplo de ação: abrir outra tela ou mostrar detalhes
                    System.out.println("Avisos gerais clicado")
            );
        }

        return view;
    }
}
