package com.vireya.hydrocore.calculadora;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vireya.hydrocore.R;
import com.vireya.hydrocore.calculadora.api.CalculadoraApi;
import com.vireya.hydrocore.calculadora.model.CalculoCoagulacaoRequest;
import com.vireya.hydrocore.calculadora.model.CalculoFloculacaoRequest;
import com.vireya.hydrocore.calculadora.model.CalculoResponse;
import com.vireya.hydrocore.calculadora.model.ProdutoResponse;


import com.vireya.hydrocore.core.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

        CalculadoraApi api = RetrofitClient.getRetrofit().create(CalculadoraApi.class);

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
        Call<List<ProdutoResponse>> callListaProduto = api.listarProdutos("ETA Rio Claro");

        callListaProduto.enqueue(new Callback<List<ProdutoResponse>>() {
            @Override
            public void onResponse(Call<List<ProdutoResponse>> call, Response<List<ProdutoResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ProdutoResponse> lista = response.body();
                    if (!lista.isEmpty()) {
                        List<String> produtos = lista.get(0).getProdutos();

                        ArrayAdapter<String> adapterProdutos = new ArrayAdapter<>(
                                requireContext(),
                                android.R.layout.simple_dropdown_item_1line,
                                produtos
                        );
                        cbProdutosQuimicos.setAdapter(adapterProdutos);
                        cbProdutosQuimicos.showDropDown();
                    } else {
                        Toast.makeText(requireContext(), "Nenhum produto encontrado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Erro: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProdutoResponse>> call, Throwable t) {
                Toast.makeText(requireContext(), "Falha na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });






        //Impedir teclado de abrir no produto químico
        cbProdutosQuimicos.setInputType(0);
        cbProdutosQuimicos.setKeyListener(null);
        cbProdutosQuimicos.setFocusable(false);

        cbProdutosQuimicos.setFocusableInTouchMode(true);

        cbProdutosQuimicos.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                cbProdutosQuimicos.showDropDown();
            }
        });



        //endregion

        //region ComboBox
        cbEtapa = view.findViewById(R.id.cbEtapa);

        cbEtapa.setOnClickListener(v -> {
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
            Call<CalculoResponse> call = null;

            if (etapaSelecionada.equals("Coagulação")) {

                // Validação
                valido = ValidarCamposCoagulacao(
                        turbidezCoagulacao, turbidezLayoutCoagulacao,
                        phCoagulacao, phLayoutCoagulacao,
                        volume, volumeLayout,
                        aluminaResidual, aluminaResidualLayout,
                        alcalinidade, alcalinidadeLayout,
                        cbProdutosQuimicos, inputProdutosQuimicos
                );

                if (!valido) {
                    Toast.makeText(requireContext(), "Preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Captura dos dados
                double turbidez = Double.parseDouble(turbidezCoagulacao.getText().toString());
                double ph = Double.parseDouble(phCoagulacao.getText().toString());
                String cor = cbCorCoagulacao.getText().toString();
                double vol = Double.parseDouble(volume.getText().toString());
                double alumina = Double.parseDouble(aluminaResidual.getText().toString());
                double alcalinidadeVal = Double.parseDouble(alcalinidade.getText().toString());
                String produto = cbProdutosQuimicos.getText().toString();

                CalculoCoagulacaoRequest request = new CalculoCoagulacaoRequest(
                        turbidez, ph, cor, vol, alumina, alcalinidadeVal, produto
                );

                // Chamada da API
                call = api.calcularCoagulacao(request);

                call.enqueue(new Callback<CalculoResponse>() {
                    @Override
                    public void onResponse(Call<CalculoResponse> call, Response<CalculoResponse> response) {
                        executarCalculo(response);
                    }

                    @Override
                    public void onFailure(Call<CalculoResponse> call, Throwable t) {
                        Toast.makeText(requireContext(), "Falha na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            } else if (etapaSelecionada.equals("Floculação")) {
                valido = validarCamposFloculacao(
                        turbidezFloculacao, turbidezLayoutFloculacao,
                        phFloculacao, phLayoutFloculacao,
                        cbProdutosQuimicos, inputProdutosQuimicos
                );

                if (valido) {
                    // Monta request para floculação (você deve criar classe FloculacaoRequest)
                    double turbidez = Double.parseDouble(turbidezFloculacao.getText().toString());
                    double ph = Double.parseDouble(phFloculacao.getText().toString());
                    String produto = cbProdutosQuimicos.getText().toString();

                    CalculoFloculacaoRequest request = new CalculoFloculacaoRequest(
                            Double.parseDouble(turbidezFloculacao.getText().toString()),
                            Double.parseDouble(phFloculacao.getText().toString()),
                            cbCorFloculacao.getText().toString(),
                            cbProdutosQuimicos.getText().toString()
                    );

                    call = api.calcularFloculacao(request);
                    call.enqueue(new Callback<CalculoResponse>() {
                        @Override
                        public void onResponse(Call<CalculoResponse> call, Response<CalculoResponse> response) {
                            executarCalculo(response);
                        }

                        @Override
                        public void onFailure(Call<CalculoResponse> call, Throwable t) {
                            Toast.makeText(requireContext(), "Falha na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

        });



        return view;
    }

    //Validação Coagulação
    private static void ValidarCalculadora(TextInputEditText turbidez, TextInputLayout turbidezLayout,
                                           TextInputEditText ph, TextInputLayout phLayout,
                                           TextInputEditText volume, TextInputLayout volumeLayout,
                                           TextInputEditText aluminaResidual, TextInputLayout aluminaResidualLayout,
                                           TextInputEditText alcalinidade, TextInputLayout alcalinidadeLayout) {

        // Turbidez
        turbidez.addTextChangedListener(new SimpleTextWatcher(s -> {
            if(s.isEmpty()) return null;
            try {
                double valor = Double.parseDouble(s);
                if(valor > 500) return "Turbidez máxima: 500 NTU.";
                if(valor <= 0) return "Turbidez mínima: 0 NTU.";
            } catch(NumberFormatException e){
                return "Número inválido";
            }
            return null;
        }, turbidezLayout));

        // pH
        ph.addTextChangedListener(new SimpleTextWatcher(s -> {
            if(s.isEmpty()) return null;
            try {
                double valor = Double.parseDouble(s);
                if(valor > 14) return "pH máximo: 14";
                if(valor <= 0) return "pH mínimo: 1";
            } catch(NumberFormatException e){
                return "Número inválido";
            }
            return null;
        }, phLayout));

        // Volume
        volume.addTextChangedListener(new SimpleTextWatcher(s -> {
            if(s.isEmpty()) return null;
            try {
                double valor = Double.parseDouble(s);
                if(valor > 500000) return "Volume Máximo: 500.000 m³";
                if(valor <= 0) return "Volume Mínimo: 0 m³";
            } catch(NumberFormatException e){
                return "Número inválido";
            }
            return null;
        }, volumeLayout));

        // Alumina Residual
        aluminaResidual.addTextChangedListener(new SimpleTextWatcher(s -> {
            if(s.isEmpty()) return null;
            try {
                double valor = Double.parseDouble(s);
                if(valor > 2) return "Alumina Residual Máx: 2 mg/L";
                if(valor <= 0) return "Alumina Residual Mín: 0 mg/L";
            } catch(NumberFormatException e){
                return "Número inválido";
            }
            return null;
        }, aluminaResidualLayout));

        // Alcalinidade
        alcalinidade.addTextChangedListener(new SimpleTextWatcher(s -> {
            if(s.isEmpty()) return null;
            try {
                double valor = Double.parseDouble(s);
                if(valor > 300) return "Alcalinidade Máx: 300 mg/L";
                if(valor <= 0) return "Alcalinidade Mín: 0 mg/L";
            } catch(NumberFormatException e){
                return "Número inválido";
            }
            return null;
        }, alcalinidadeLayout));

        InputFilter filterDecimal = (source, start, end, dest, dstart, dend) -> {
            String newText = dest.toString().substring(0, dstart)
                    + source.subSequence(start, end)
                    + dest.toString().substring(dend);

            // permite apenas dígitos e um único ponto decimal
            if (!newText.matches("\\d*\\.?\\d*")) {
                return "";
            }

            return null;
        };

        turbidez.setFilters(new InputFilter[]{filterDecimal});
        ph.setFilters(new InputFilter[]{filterDecimal});
        volume.setFilters(new InputFilter[]{filterDecimal});
        aluminaResidual.setFilters(new InputFilter[]{filterDecimal});
        alcalinidade.setFilters(new InputFilter[]{filterDecimal});

    }

    private static class SimpleTextWatcher implements TextWatcher {
        interface Validator { String validate(String s); }
        private final Validator validator;
        private final TextInputLayout layout;

        SimpleTextWatcher(Validator validator, TextInputLayout layout) {
            this.validator = validator;
            this.layout = layout;
        }

        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            String error = validator.validate(s.toString().trim());
            layout.setError(error);
        }
    }


    //Validação Floculação
    private static void ValidarCalculadora(TextInputEditText turbidezFloc, TextInputLayout turbidezLayoutFloc,
                                           TextInputEditText phFloc, TextInputLayout phLayoutFloc) {

        // Turbidez
        turbidezFloc.addTextChangedListener(new SimpleTextWatcher(s -> {
            if (s.isEmpty()) return null;
            try {
                int valor = Integer.parseInt(s);
                if (valor > 500) return "Turbidez máxima: 500 NTU.";
                if (valor <= 0) return "Turbidez mínima: 0 NTU.";
            } catch (NumberFormatException e) {
                return "Número inválido";
            }
            return null;
        }, turbidezLayoutFloc));

        // pH
        phFloc.addTextChangedListener(new SimpleTextWatcher(s -> {
            if (s.isEmpty()) return null;
            try {
                int valor = Integer.parseInt(s);
                if (valor > 14) return "pH máximo: 14";
                if (valor <= 0) return "pH mínimo: 1";
            } catch (NumberFormatException e) {
                return "Número inválido";
            }
            return null;
        }, phLayoutFloc));

        InputFilter filterDecimal = (source, start, end, dest, dstart, dend) -> {
            String newText = dest.toString().substring(0, dstart)
                    + source.subSequence(start, end)
                    + dest.toString().substring(dend);

            // permite apenas dígitos e um único ponto decimal
            if (!newText.matches("\\d*\\.?\\d*")) {
                return "";
            }

            return null;
        };


        turbidezFloc.setFilters(new InputFilter[]{filterDecimal});
        phFloc.setFilters(new InputFilter[]{filterDecimal});

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

    private void executarCalculo(Response<CalculoResponse> response) {
        if (response.isSuccessful() && response.body() != null) {
            CalculoResponse resultado = response.body();

            TextView txtProduto = requireView().findViewById(R.id.txtProduto);
            TextView txtQuantidade = requireView().findViewById(R.id.txtQuantidade);

            txtProduto.setText("Produto: " + resultado.getProduto());
            txtQuantidade.setText("Quantidade: " + resultado.getQuantidade());

            txtProduto.setVisibility(View.VISIBLE);
            txtQuantidade.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(requireContext(), "Erro ao calcular. Verifique os dados.", Toast.LENGTH_SHORT).show();
        }
    }





}