package com.vireya.hydrocore.relatorio;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.vireya.hydrocore.R;

import java.util.ArrayList;
import java.util.List;

public class Relatorio extends Fragment {

    private RecyclerView recyclerView;
    private RelatorioAdapter adapter;
    private List<String> listaRelatorios;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_relatorio, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewRelatorios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listaRelatorios = new ArrayList<>();
        listaRelatorios.add("Junho 2025");
        listaRelatorios.add("Maio 2025");
        listaRelatorios.add("Abril 2025");
        listaRelatorios.add("Março 2025");
        listaRelatorios.add("Fevereiro 2025");
        listaRelatorios.add("Janeiro 2025");

        adapter = new RelatorioAdapter(listaRelatorios, relatorio -> {
            Toast.makeText(getContext(), "Abrir PDF: " + relatorio, Toast.LENGTH_SHORT).show();
            // Aqui você abre o PdfViewerActivity
        });

        recyclerView.setAdapter(adapter);

        // Botões
        ImageView btnVireya = view.findViewById(R.id.imgHydrocore);
        ImageView btnAgenda = view.findViewById((R.id.imgAgenda));
        ImageView btnConfig = view.findViewById((R.id.imgConfig));

        //Ações Botão
        btnVireya.setOnClickListener(v -> {
            DrawerLayout drawer = getActivity().findViewById(R.id.drawerLayout);
            if (drawer != null) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        btnAgenda.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_agenda, null,
                    new androidx.navigation.NavOptions.Builder()
                            .setPopUpTo(R.id.navigation_relatorio, true) // limpa até a Home
                            .build()
            );

        });

        btnConfig.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_configuracoes, null,
                    new androidx.navigation.NavOptions.Builder()
                            .setPopUpTo(R.id.navigation_relatorio, true)
                            .build()
            );

        });

        return view;
    }

}