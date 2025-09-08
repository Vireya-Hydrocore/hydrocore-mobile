package com.vireya.hydrocore.ui;

import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.vireya.hydrocore.R;

public class Perfil extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        // Botão "Histórico de Tarefas"
        MaterialButton btnHistorico = view.findViewById(R.id.btnHistorico);

        btnHistorico.setOnClickListener(v -> {
            // Pega o Drawer da Activity
            DrawerLayout drawer = getActivity().findViewById(R.id.drawerLayout);
            if (drawer != null) {
                drawer.openDrawer(GravityCompat.START); // abre o menu lateral
            }
        });

        return view;
    }

}