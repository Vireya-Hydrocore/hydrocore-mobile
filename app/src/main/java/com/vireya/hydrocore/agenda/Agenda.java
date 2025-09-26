package com.vireya.hydrocore.agenda;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.vireya.hydrocore.R;
import com.vireya.hydrocore.agenda.adapter.AvisoAdapter;
import com.vireya.hydrocore.agenda.api.ApiClient;
import com.vireya.hydrocore.agenda.model.Aviso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Agenda extends Fragment {

    private TextView selectedDateText;
    private CompactCalendarView compactCalendarView;
    private RecyclerView recyclerView;
    private AvisoAdapter avisoAdapter;
    private List<Aviso> listaAvisos = new ArrayList<>();

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final SimpleDateFormat sdfHeader = new SimpleDateFormat("dd MMMM yyyy", new Locale("pt", "BR"));

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agenda2, container, false);

        selectedDateText = view.findViewById(R.id.selectedDateText);
        compactCalendarView = view.findViewById(R.id.compactcalendar_view);
        recyclerView = view.findViewById(R.id.blockContainer);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        avisoAdapter = new AvisoAdapter(new ArrayList<>());
        recyclerView.setAdapter(avisoAdapter);

        // Data inicial no cabeçalho
        selectedDateText.setText(sdfHeader.format(new Date()));

        // Configurações do calendário
        compactCalendarView.setUseThreeLetterAbbreviation(true);

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                selectedDateText.setText(sdfHeader.format(dateClicked));
                atualizarRecycler(dateClicked);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                selectedDateText.setText(sdfHeader.format(firstDayOfNewMonth));
            }
        });

        carregarAvisosDaApi();

        return view;
    }

    private void carregarAvisosDaApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://SEU_BACKEND_URL/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiClient apiService = retrofit.create(ApiClient.class);

        apiService.getAvisos().enqueue(new Callback<List<Aviso>>() {
            @Override
            public void onResponse(Call<List<Aviso>> call, Response<List<Aviso>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaAvisos = response.body();

                    for (Aviso aviso : listaAvisos) {
                        try {
                            Date data = sdf.parse(aviso.getData_ocorrencia());

                            int cor;
                            switch (aviso.getId_prioridade()) {
                                case 1: cor = Color.RED; break;     // Alta
                                case 2: cor = Color.YELLOW; break; // Média
                                default: cor = Color.GREEN; break; // Baixa
                            }

                            Event event = new Event(cor, data.getTime(), aviso);
                            compactCalendarView.addEvent(event);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<List<Aviso>> call, Throwable t) {
                Log.e("API_ERROR", "Erro ao carregar avisos", t);
            }
        });
    }

    private void atualizarRecycler(Date dataSelecionada) {
        List<Aviso> avisosDoDia = new ArrayList<>();

        String dataFormatada = sdf.format(dataSelecionada);

        for (Aviso aviso : listaAvisos) {
            if (aviso.getData_ocorrencia().equals(dataFormatada)) {
                avisosDoDia.add(aviso);
            }
        }

        avisoAdapter.updateList(avisosDoDia);
    }
}
