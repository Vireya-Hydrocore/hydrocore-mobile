package com.vireya.hydrocore.calculadora;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vireya.hydrocore.R;


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

        //Variáveis Coagulação
        TextInputLayout turbidezLayout = cardCoagulacao.findViewById(R.id.turbidezLayout);
        TextInputEditText turbidez = cardCoagulacao.findViewById(R.id.inputTurbidez);

        TextInputLayout phLayout = cardCoagulacao.findViewById(R.id.phLayout);
        TextInputEditText ph = cardCoagulacao.findViewById(R.id.inputPh);

        TextInputLayout volumeLayout = cardCoagulacao.findViewById(R.id.volumeLayout);
        TextInputEditText volume = cardCoagulacao.findViewById(R.id.inputVolume);

        TextInputLayout aluminaResidualLayout = cardCoagulacao.findViewById(R.id.aluminaResidualLayout);
        TextInputEditText aluminaResidual = cardCoagulacao.findViewById(R.id.inputAluminaResidual);

        TextInputLayout alcalinidadeLayout = cardCoagulacao.findViewById(R.id.alcalinidadeLayout);
        TextInputEditText alcalinidade = cardCoagulacao.findViewById(R.id.inputAlcalinidade);

        //Configurar Botões do Topo da tela
        ConfigurarBotõesTopo(view);


        //ComboBox
        gerarComboEtapa(view, cbEtapa);
        cbEtapa.setOnItemClickListener((parent, view1, position, id) -> {
            DefineVisibilidadeCombo(parent, position, cardCoagulacao, cardFloculacao, txtProdutosQuimicos, inputProdutosQuimicos, btnCalcular);
        });

        //Campos Coagulação

        //Turbidez
        turbidez.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    turbidezLayout.setError(null);
                    return;
                }

                int valor = Integer.parseInt(s.toString());
                if(valor > 500){
                    turbidezLayout.setError("Turbidez máxima: 500 NTU.");
                } else if(valor <= 0){
                    turbidezLayout.setError("Turbidez mínima 0 NTU.");
                }
                else{
                    turbidezLayout.setError(null);
                }
            }
        });

        //pH
        ph.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    phLayout.setError(null);
                    return;
                }

                int valor = Integer.parseInt(s.toString());
                if(valor > 14){
                    phLayout.setError("pH máximo: 14 pH.");
                } else if(valor <= 0){
                    phLayout.setError("pH mínimo: 1 pH.");
                }
                else{
                    phLayout.setError(null);
                }
            }

        });

        //Volume
        volume.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    volumeLayout.setError(null);
                    return;
                }

                int valor = Integer.parseInt(s.toString());
                if(valor > 500000){
                    volumeLayout.setError("Volume Máximo: 500.000m³.");
                } else if(valor <= 0){
                    volumeLayout.setError("Volume Mínimo: 0m³.");
                }
                else{
                    volumeLayout.setError(null);
                }
            }

        });

        //Alumina Residual
        aluminaResidual.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    aluminaResidualLayout.setError(null);
                    return;
                }

                double valor = Double.parseDouble(s.toString());
                if(valor > 2){
                    aluminaResidualLayout.setError("Alumina Residual Máximo: 2mg/L");
                } else if(valor <= 0){
                    aluminaResidualLayout.setError("Alumina Residual Mínimo: 0mg/L.");
                }
                else{
                    aluminaResidualLayout.setError(null);
                }
            }

        });

        //Alcalinidade
        alcalinidade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    alcalinidadeLayout.setError(null);
                    return;
                }

                int valor = Integer.parseInt(s.toString());
                if(valor > 300){
                    alcalinidadeLayout.setError("Alcalinidade Máxima: 300mg/L CaCO₃.");
                } else if(valor <= 0){
                    alcalinidadeLayout.setError("Alcalinidade Mínima: 0mg/L CaCO₃.");
                }
                else{
                    alcalinidadeLayout.setError(null);
                }
            }

        });








        return view;
    }

    private static void DefineVisibilidadeCombo(AdapterView<?> parent, int position, CardView cardCoagulacao, CardView cardFloculacao, TextView txtProdutosQuimicos, TextInputLayout inputProdutosQuimicos, Button btnCalcular) {
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
    }

    private void ConfigurarBotõesTopo(View view) {
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