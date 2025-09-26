package com.vireya.hydrocore.calculadora;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vireya.hydrocore.R;


public class Calculadora extends Fragment {

    //Varíáveis
    String etapaSelecionada;
    private AutoCompleteTextView cbEtapa;

    @Override
    public void onResume() {
        super.onResume();
        if (cbEtapa != null) {
            cbEtapa.setText("", false);
            cbEtapa.clearListSelection();

            String[] etapas = {"Coagulação", "Floculação"};
            ArrayAdapter<String> adapterEtapas = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    etapas
            );
            cbEtapa.setAdapter(adapterEtapas);
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cbEtapa = view.findViewById(R.id.cbEtapa);

        //ComboBox
        gerarComboEtapa(view, cbEtapa);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //region Variáveis
        View view = inflater.inflate(R.layout.fragment_calculadora, container, false);
        TextView txtProdutosQuimicos = view.findViewById(R.id.txtProdutosQuimicos);
        TextInputLayout inputProdutosQuimicos = view.findViewById(R.id.inputProdutosQuimicos);
        Button btnCalcularDosagem = view.findViewById(R.id.btnCalcularDosagem);

        AutoCompleteTextView cbProdutosQuimicos = view.findViewById(R.id.cbProdutosQuimicos);
        //endregion

        //region Cards
        CardView cardCoagulacao = view.findViewById(R.id.cardCoagulacao);
        CardView cardFloculacao = view.findViewById(R.id.cardFloculacao);
        //endregion

        //region Variáveis Coagulação
        TextInputLayout turbidezLayoutCoagulacao = cardCoagulacao.findViewById(R.id.turbidezLayout);
        TextInputEditText turbidezCoagulacao = cardCoagulacao.findViewById(R.id.inputTurbidez);

        TextInputLayout phLayoutCoagulacao = cardCoagulacao.findViewById(R.id.phLayout);
        TextInputEditText phCoagulacao = cardCoagulacao.findViewById(R.id.inputPh);

        TextInputLayout volumeLayout = cardCoagulacao.findViewById(R.id.volumeLayout);
        TextInputEditText volume = cardCoagulacao.findViewById(R.id.inputVolume);

        TextInputLayout aluminaResidualLayout = cardCoagulacao.findViewById(R.id.aluminaResidualLayout);
        TextInputEditText aluminaResidual = cardCoagulacao.findViewById(R.id.inputAluminaResidual);

        TextInputLayout alcalinidadeLayout = cardCoagulacao.findViewById(R.id.alcalinidadeLayout);
        TextInputEditText alcalinidade = cardCoagulacao.findViewById(R.id.inputAlcalinidade);

        AutoCompleteTextView cbCorCoagulacao = cardCoagulacao.findViewById(R.id.cbCor);
        //endregion

        //region Variáveis Floculação
        TextInputLayout turbidezLayoutFloculacao = cardFloculacao.findViewById(R.id.turbidezLayout);
        TextInputEditText turbidezFloculacao = cardFloculacao.findViewById(R.id.inputTurbidez);

        TextInputLayout phLayoutFloculacao = cardFloculacao.findViewById(R.id.phLayout);
        TextInputEditText phFloculacao = cardFloculacao.findViewById(R.id.inputPh);

        AutoCompleteTextView cbCorFloculacao = cardFloculacao.findViewById(R.id.cbCor);
        //endregion

        //region Cores
        String[] cores = {"Marrom", "Amarelada", "Esverdeada", "Amarelada", "Cinza"};
        ArrayAdapter<String> adapterCores = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                cores
        );

        cbCorCoagulacao.setAdapter(adapterCores);
        cbCorFloculacao.setAdapter(adapterCores);
        //endregion

        //region Produtos
        String[] produtos = {"Produto 1", "Produto 2", "Produto 3"};
        ArrayAdapter<String> adapterProdutos = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                produtos
        );

        cbProdutosQuimicos.setAdapter(adapterProdutos);
        //endregion

        //region ComboBox
        cbEtapa = view.findViewById(R.id.cbEtapa);

        cbEtapa.setOnClickListener(v -> {
            // força resetar o filtro
            cbEtapa.setText("", false);
            cbEtapa.showDropDown();
        });


        cbEtapa.setOnItemClickListener((parent, view1, position, id) -> {
            DefineVisibilidadeCombo(parent, position, cardCoagulacao, cardFloculacao, txtProdutosQuimicos, inputProdutosQuimicos, btnCalcularDosagem);
            etapaSelecionada = parent.getItemAtPosition(position).toString();
        });
        //endregion



        //Validações
        ValidarCalculadora(turbidezCoagulacao, turbidezLayoutCoagulacao, phCoagulacao, phLayoutCoagulacao, volume, volumeLayout, aluminaResidual, aluminaResidualLayout, alcalinidade, alcalinidadeLayout);
        ValidarCalculadora(turbidezFloculacao, turbidezLayoutFloculacao, phFloculacao, phLayoutFloculacao);



        //Habilitar Botão
        btnCalcularDosagem.setOnClickListener(v -> {
            boolean valido = false;
            if (etapaSelecionada.equals("Coagulação")) {
                valido = ValidarCamposCoagulacao(
                        turbidezCoagulacao, turbidezLayoutCoagulacao,
                        phCoagulacao, phLayoutCoagulacao,
                        volume, volumeLayout,
                        aluminaResidual, aluminaResidualLayout,
                        alcalinidade, alcalinidadeLayout,
                        cbProdutosQuimicos, inputProdutosQuimicos
                );
            }

            else if (etapaSelecionada.equals("Floculação")) {
                valido = validarCamposFloculacao(
                        turbidezFloculacao, turbidezLayoutFloculacao,
                        phFloculacao, phLayoutFloculacao,
                        cbProdutosQuimicos, inputProdutosQuimicos
                );
            }

            if (valido) {
                Toast.makeText(requireContext(), "Cálculo realizado com sucesso!", Toast.LENGTH_SHORT).show();
                // COLOCAR LÓGICA DE CÁLCULO
            } else {
                Toast.makeText(requireContext(), "Preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    //Validação Coagulação
    private static void ValidarCalculadora(TextInputEditText turbidez, TextInputLayout turbidezLayout, TextInputEditText ph, TextInputLayout phLayout, TextInputEditText volume, TextInputLayout volumeLayout, TextInputEditText aluminaResidual, TextInputLayout aluminaResidualLayout, TextInputEditText alcalinidade, TextInputLayout alcalinidadeLayout) {
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
    }

    //Validar Floculação
    private static void ValidarCalculadora(TextInputEditText turbidezFloculacao, TextInputLayout turbidezLayoutFloculacao, TextInputEditText phFloculacao, TextInputLayout phLayoutFloculacao) {
        //Turbidez
        turbidezFloculacao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    turbidezLayoutFloculacao.setError(null);
                    return;
                }

                int valor = Integer.parseInt(s.toString());
                if(valor > 500){
                    turbidezLayoutFloculacao.setError("Turbidez máxima: 500 NTU.");
                } else if(valor <= 0){
                    turbidezLayoutFloculacao.setError("Turbidez mínima 0 NTU.");
                }
                else{
                    turbidezLayoutFloculacao.setError(null);
                }
            }
        });

        //pH
        phFloculacao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    phLayoutFloculacao.setError(null);
                    return;
                }

                int valor = Integer.parseInt(s.toString());
                if(valor > 14){
                    phLayoutFloculacao.setError("pH máximo: 14 pH.");
                } else if(valor <= 0){
                    phLayoutFloculacao.setError("pH mínimo: 1 pH.");
                }
                else{
                    phLayoutFloculacao.setError(null);
                }
            }

        });
    }

    //Validar Campos em Geral
    private boolean ValidarCampo(TextInputEditText editText, TextInputLayout layout) {
        String texto = editText.getText() != null ? editText.getText().toString().trim() : "";
        return !texto.isEmpty() && layout.getError() == null;
    }

    private boolean ValidarCampo(AutoCompleteTextView autoText, TextInputLayout layout) {
        String texto = autoText.getText() != null ? autoText.getText().toString().trim() : "";
        if (texto.isEmpty()) {
            layout.setError("Selecione um produto");
            return false;
        }
        layout.setError(null);
        return true;
    }

    private boolean ValidarCamposCoagulacao(
            TextInputEditText turbidez, TextInputLayout turbidezLayout,
            TextInputEditText ph, TextInputLayout phLayout,
            TextInputEditText volume, TextInputLayout volumeLayout,
            TextInputEditText aluminaResidual, TextInputLayout aluminaResidualLayout,
            TextInputEditText alcalinidade, TextInputLayout alcalinidadeLayout,
            AutoCompleteTextView produtos, TextInputLayout produtosLayout
    ) {
        return ValidarCampo(turbidez, turbidezLayout)
                && ValidarCampo(ph, phLayout)
                && ValidarCampo(volume, volumeLayout)
                && ValidarCampo(aluminaResidual, aluminaResidualLayout)
                && ValidarCampo(alcalinidade, alcalinidadeLayout)
                && ValidarCampo(produtos, produtosLayout);
    }

    private boolean validarCamposFloculacao(
            TextInputEditText turbidez, TextInputLayout turbidezLayout,
            TextInputEditText ph, TextInputLayout phLayout,
            AutoCompleteTextView produtos, TextInputLayout produtosLayout
    ) {
        return ValidarCampo(turbidez, turbidezLayout)
                && ValidarCampo(ph, phLayout)
                && ValidarCampo(produtos, produtosLayout);
    }


    private boolean ValidarCamposFloculacao(
            TextInputEditText turbidez, TextInputLayout turbidezLayout,
            TextInputEditText ph, TextInputLayout phLayout
    ) {
        return ValidarCampo(turbidez, turbidezLayout)
                && ValidarCampo(ph, phLayout);
    }
    //endregion


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