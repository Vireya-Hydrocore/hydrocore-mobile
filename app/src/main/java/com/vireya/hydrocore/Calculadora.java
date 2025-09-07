package com.vireya.hydrocore;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;


public class Calculadora extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Variáveis
        View view = inflater.inflate(R.layout.fragment_calculadora, container, false);
        AutoCompleteTextView cbEtapa = view.findViewById(R.id.cbEtapa);
        TextView txtProdutosQuimicos = view.findViewById(R.id.txtProdutosQuimicos);
        TextInputLayout inputProdutosQuimicos = view.findViewById(R.id.inputProdutosQuimicos);
        Button btnCalcular = view.findViewById(R.id.btnCalcular);

        //Cards
        CardView cardCoagulacao = view.findViewById(R.id.cardCoagulacao);
        CardView cardFloculacao = view.findViewById(R.id.cardFloculacao);



        gerarComboEtapa(view, cbEtapa);

        cbEtapa.setOnItemClickListener((parent, view1, position, id) -> {
            String selecionado = (String) parent.getItemAtPosition(position);

            if (selecionado.equals("Coagulação")) {
                cardCoagulacao.setVisibility(View.VISIBLE);
                cardFloculacao.setVisibility(View.GONE);
            } else {
                cardCoagulacao.setVisibility(View.GONE);
                cardFloculacao.setVisibility(View.VISIBLE);
            }

            txtProdutosQuimicos.setVisibility(View.VISIBLE);
            inputProdutosQuimicos.setVisibility(View.VISIBLE);
            btnCalcular.setVisibility(View.VISIBLE);
        });


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
                            .setPopUpTo(R.id.navigation_calculadora, true) // limpa até a Home
                            .build()
            );

            DesmarcarCalculadora();
        });

        btnConfig.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_configuracoes, null,
                    new androidx.navigation.NavOptions.Builder()
                            .setPopUpTo(R.id.navigation_calculadora, true)
                            .build()
            );

            DesmarcarCalculadora();
        });

        return view;
    }

    //Métodos GerarComboBox
    private void gerarComboEtapa(View view, AutoCompleteTextView cbEtapa) {
        String [] etapas = {"Coagulação", "Floculação"};

        ArrayAdapter<String> adapterEtapas = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                etapas
        );

        cbEtapa.setAdapter(adapterEtapas);

        cbEtapa.setOnClickListener(v -> {
            cbEtapa.showDropDown();
        });
    }

    private void DesmarcarCalculadora() {
        BottomNavigationView bottomNav = getActivity().findViewById(R.id.nav_view);
        if (bottomNav != null) {
            bottomNav.getMenu().setGroupCheckable(0, true, false);
            bottomNav.getMenu().findItem(R.id.navigation_calculadora).setChecked(false);
            bottomNav.getMenu().setGroupCheckable(0, true, true);
        }
    }


}