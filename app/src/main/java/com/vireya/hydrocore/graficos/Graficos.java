package com.vireya.hydrocore.graficos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.vireya.hydrocore.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Graficos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Graficos extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Graficos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Graficos.
     */
    // TODO: Rename and change types and number of parameters
    public static Graficos newInstance(String param1, String param2) {
        Graficos fragment = new Graficos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_graficos, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WebView webGrafico1 = view.findViewById(R.id.webGrafico1);
        WebView webGrafico2 = view.findViewById(R.id.webGrafico2);
        WebView webGrafico3 = view.findViewById(R.id.webGrafico3);

        // Configura o WebView 1
        webGrafico1.getSettings().setJavaScriptEnabled(true);
        webGrafico1.setWebViewClient(new WebViewClient()); // garante abertura dentro do app
        webGrafico1.loadUrl("https://app.powerbi.com/view?r=eyJrIjoiODlhOTZmMzUtY2VjMi00OTI4LWFiY2YtMzVjNmFhYTY3NDRhIiwidCI6ImIxNDhmMTRjLTIzOTctNDAyYy1hYjZhLTFiNDcxMTE3N2FjMCJ9");

        // Configura o WebView 2
        webGrafico2.getSettings().setJavaScriptEnabled(true);
        webGrafico2.setWebViewClient(new WebViewClient());
        webGrafico2.loadUrl("https://app.powerbi.com/view?r=eyJrIjoiODlhOTZmMzUtY2VjMi00OTI4LWFiY2YtMzVjNmFhYTY3NDRhIiwidCI6ImIxNDhmMTRjLTIzOTctNDAyYy1hYjZhLTFiNDcxMTE3N2FjMCJ9");

        webGrafico3.getSettings().setJavaScriptEnabled(true);
        webGrafico3.setWebViewClient(new WebViewClient());
        webGrafico3.loadUrl("https://app.powerbi.com/view?r=eyJrIjoiODlhOTZmMzUtY2VjMi00OTI4LWFiY2YtMzVjNmFhYTY3NDRhIiwidCI6ImIxNDhmMTRjLTIzOTctNDAyYy1hYjZhLTFiNDcxMTE3N2FjMCJ9");
    }
}
