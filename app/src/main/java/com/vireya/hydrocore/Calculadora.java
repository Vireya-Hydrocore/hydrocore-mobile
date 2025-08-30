package com.vireya.hydrocore;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
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


}