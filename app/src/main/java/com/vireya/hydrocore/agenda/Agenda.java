package com.vireya.hydrocore.agenda;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.vireya.hydrocore.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Agenda extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private static final String TAG = "AgendaFragment";

    public Agenda() {}

    public static Agenda newInstance(String param1, String param2) {
        Agenda fragment = new Agenda();
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
        View view = inflater.inflate(R.layout.fragment_agenda2, container, false);

        // Inicializa o CompactCalendarView
        final CompactCalendarView compactCalendarView = view.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);

        // Define a data atual como a data inicial
        compactCalendarView.setCurrentDate(new Date());

        // Inicializa o TextView que mostra a data selecionada
        final TextView selectedDateText = view.findViewById(R.id.selectedDateText);


        // Mostra a data atual quando o fragment abre
        Date today = new Date();
        selectedDateText.setText(formatDate(today));

        // Cria um calendário com a data atual
        Calendar cal = Calendar.getInstance();
        //quero que o dia de hoje fique marcada


        // Evento para amanhã
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Event evAmanha = new Event(Color.GREEN, cal.getTimeInMillis(), "Evento de amanhã");
        compactCalendarView.addEvent(evAmanha);

        Event ev2 = new Event(Color.GREEN, 1433704251000L);
        compactCalendarView.addEvent(ev2);

        // Consulta eventos (exemplo)
        List<Event> events = compactCalendarView.getEvents(today);
        Log.d(TAG, "Events: " + events);

        // Listener de clique e scroll
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                // Atualiza o TextView com a data selecionada
                selectedDateText.setText(formatDate(dateClicked));

                List<Event> events = compactCalendarView.getEvents(dateClicked);
                Log.d(TAG, "Day was clicked: " + dateClicked + " with events " + events);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d(TAG, "Month was scrolled to: " + firstDayOfNewMonth);
            }
        });

        return view;
    }
    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd, MMMM", new Locale("pt", "BR"));
        return dateFormat.format(date);
    }
}
