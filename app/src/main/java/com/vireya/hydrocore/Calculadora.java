package com.vireya.hydrocore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;


public class Calculadora extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calculadora, container, false);

        gerarComboBox(view);

        return view;
    }

    //Métodos GerarComboBox
    private void gerarComboBox(View view) {
        gerarComboCor(view);
        gerarComboOdor(view);
        gerarComboProdutosQuimicos(view);
        gerarComboEtapa(view);
    }

    private void gerarComboEtapa(View view) {
        AutoCompleteTextView cbEtapa = view.findViewById(R.id.cbEtapa);
        String [] etapas = {"Coagulação", "Floculação", "Desinfecção", "Ajustes do pH"};

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

    private void gerarComboProdutosQuimicos(View view) {
        AutoCompleteTextView cbProdutosQuimicos = view.findViewById(R.id.cbProdutosQuimicos);
        String [] produtosQuimicos = {"Polímero", "Sulfato de Alumínio", "Cal Hidratada"};

        ArrayAdapter<String> adapterProdutosQuimicos = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                produtosQuimicos
        );

        cbProdutosQuimicos.setAdapter(adapterProdutosQuimicos);

        cbProdutosQuimicos.setOnClickListener(v -> {
            cbProdutosQuimicos.showDropDown();
        });
    }

    private void gerarComboOdor(View view) {
        AutoCompleteTextView cbOdor = view.findViewById(R.id.cbOdor);
        String [] odores = {"Sem odor", "Terroso", "Mofo", "Lodo", "Algás", "Metálico", "Sulfuroso"};

        ArrayAdapter<String> adapterOdor = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                odores
        );

        cbOdor.setAdapter(adapterOdor);

        cbOdor.setOnClickListener(v -> {
            cbOdor.showDropDown();
        });
    }

    private void gerarComboCor(View view) {
        AutoCompleteTextView cbCor = view.findViewById(R.id.cbCor);
        String [] cores = {"Transparente", "Turva", "Amarela", "Esverdeada", "Marrom", "Acinzentada", "Preta"};

        ArrayAdapter<String> adapterCor = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                cores
        );

        cbCor.setAdapter(adapterCor);

        cbCor.setOnClickListener(v -> {
            cbCor.showDropDown();
        });
    }
}