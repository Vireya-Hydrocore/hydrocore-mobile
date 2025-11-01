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
import com.vireya.hydrocore.core.network.RetrofitClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        ApiClient api = RetrofitClient.getRetrofit(getContext()).create(ApiClient.class);

        api.getAvisos().enqueue(new Callback<List<Aviso>>() {
            @Override
            public void onResponse(@NonNull Call<List<Aviso>> call, @NonNull Response<List<Aviso>> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {
                    listaAvisos = response.body();
                    compactCalendarView.removeAllEvents();

                    for (Aviso aviso : listaAvisos) {
                        try {
                            Date data = sdf.parse(aviso.getDataOcorrencia());
                            if (data == null) continue;

                            int cor;
                            switch (aviso.getIdPrioridade()) {
                                case 1: cor = Color.RED; break;
                                case 2: cor = Color.YELLOW; break;
                                default: cor = Color.GREEN; break;
                            }

                            compactCalendarView.addEvent(new Event(cor, data.getTime(), aviso));


                        } catch (ParseException e) {
                            Log.e("AGENDA", "Erro ao converter data: " + aviso.getDataOcorrencia(), e);
                        }
                    }

                    atualizarRecycler(new Date());

                } else {
                    Log.e("AGENDA", "Erro HTTP: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Aviso>> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                Log.e("AGENDA", "Falha na API: " + t.getMessage(), t);
            }
        });
    }

    private void atualizarRecycler(Date dataSelecionada) {
        if (listaAvisos == null || listaAvisos.isEmpty()) return;

        List<Aviso> avisosDoDia = new ArrayList<>();
        String dataFormatada = sdf.format(dataSelecionada);

        for (Aviso aviso : listaAvisos) {
            if (aviso.getDataOcorrencia().equals(dataFormatada)) {
                avisosDoDia.add(aviso);
            }
        }

        avisoAdapter.updateList(avisosDoDia);
    }
}
