package com.vireya.hydrocore.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.vireya.hydrocore.R;
import com.vireya.hydrocore.agenda.api.ApiClient;
import com.vireya.hydrocore.agenda.model.Aviso;
import com.vireya.hydrocore.core.network.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends Fragment {

    private WebView webGrafico1;
    private LinearLayout avisosGerais, layoutAviso;
    private TextView txtDescricaoAviso, txtTempoAviso;
    private ApiClient apiClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        webGrafico1 = view.findViewById(R.id.webGrafico1);
        avisosGerais = view.findViewById(R.id.avisosGerais);
        layoutAviso = view.findViewById(R.id.layoutAviso);
        txtDescricaoAviso = view.findViewById(R.id.txtDescricaoAviso);
        txtTempoAviso = view.findViewById(R.id.txtTempoAviso);

        apiClient = RetrofitClient.getRetrofit(requireContext()).create(ApiClient.class);

        if (webGrafico1 != null) {
            WebSettings webSettings = webGrafico1.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webGrafico1.setWebViewClient(new WebViewClient());
            webGrafico1.loadUrl("https://app.powerbi.com/view?r=eyJrIjoiODlhOTZmMzUtY2VjMi00OTI4LWFiY2YtMzVjNmFhYTY3NDRhIiwidCI6ImIxNDhmMTRjLTIzOTctNDAyYy1hYjZhLTFiNDcxMTE3N2FjMCJ9");
        }

        if (avisosGerais != null) {
            avisosGerais.setOnClickListener(v -> {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.navigation_agenda);
            });
        }

        carregarAvisos();
        return view;
    }

    private void carregarAvisos() {
        String dataHoje = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Call<List<Aviso>> call = apiClient.getUltimosAvisos(dataHoje);

        call.enqueue(new Callback<List<Aviso>>() {
            @Override
            public void onResponse(Call<List<Aviso>> call, Response<List<Aviso>> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Aviso avisoRecente = null;
                    for (Aviso a : response.body()) {
                        if (isMenosDeUmDia(a.getDataOcorrencia())) {
                            avisoRecente = a;
                            break;
                        }
                    }

                    if (avisoRecente != null) {
                        txtDescricaoAviso.setText(avisoRecente.getDescricao());
                        txtTempoAviso.setText(calcularTempo(avisoRecente.getDataOcorrencia()));
                        layoutAviso.setVisibility(View.VISIBLE);
                    } else {
                        layoutAviso.setVisibility(View.GONE);
                    }
                } else {
                    layoutAviso.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Aviso>> call, Throwable t) {
                if (isAdded()) layoutAviso.setVisibility(View.GONE);
            }
        });
    }

    private boolean isMenosDeUmDia(String dataOcorrencia) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date dataAviso = sdf.parse(dataOcorrencia);
            long diff = new Date().getTime() - dataAviso.getTime();
            return diff < 24 * 60 * 60 * 1000;
        } catch (Exception e) {
            return false;
        }
    }

    private String calcularTempo(String dataOcorrencia) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date dataAviso = sdf.parse(dataOcorrencia);
            long diffMillis = new Date().getTime() - dataAviso.getTime();
            long horas = diffMillis / (1000 * 60 * 60);
            if (horas < 1) return "Há poucos minutos";
            return "Há " + horas + "h";
        } catch (Exception e) {
            return "";
        }
    }
}
