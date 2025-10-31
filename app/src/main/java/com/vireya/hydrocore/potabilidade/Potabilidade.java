package com.vireya.hydrocore.potabilidade;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vireya.hydrocore.R;
import com.vireya.hydrocore.potabilidade.model.PotabilityResponse;
import com.vireya.hydrocore.potabilidade.model.WaterData;
import com.vireya.hydrocore.potabilidade.repository.WaterRepository;

public class Potabilidade extends Fragment {

    private TextView txtResultadoPotavel;
    private MaterialButton btnCalcularPotabilidade;

    private ProgressBar progressBar;


    private TextInputEditText inputPh, inputDureza, inputSolidos, inputCloraminas,
            inputSulfato, inputCondutividade, inputCarbono, inputTrihalometanos, inputTurbidez;

    public Potabilidade() {}

    public static Potabilidade newInstance(String param1, String param2) {
        Potabilidade fragment = new Potabilidade();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_potabilidade, container, false);

        txtResultadoPotavel = view.findViewById(R.id.txtResultadoPotavel);
        btnCalcularPotabilidade = view.findViewById(R.id.btnCalcularPotabilidade);

        txtResultadoPotavel.setVisibility(View.GONE);
        progressBar = view.findViewById(R.id.progressBarPotabilidade);


        configurarValidacoes(view);

        btnCalcularPotabilidade.setOnClickListener(v -> {
            try {
                progressBar.setVisibility(View.VISIBLE);
                txtResultadoPotavel.setVisibility(View.GONE);

                WaterData data = new WaterData(
                        Double.parseDouble(inputPh.getText().toString()),
                        Double.parseDouble(inputDureza.getText().toString()),
                        Double.parseDouble(inputSolidos.getText().toString()),
                        Double.parseDouble(inputCloraminas.getText().toString()),
                        Double.parseDouble(inputSulfato.getText().toString()),
                        Double.parseDouble(inputCondutividade.getText().toString()),
                        Double.parseDouble(inputCarbono.getText().toString()),
                        Double.parseDouble(inputTrihalometanos.getText().toString()),
                        Double.parseDouble(inputTurbidez.getText().toString())
                );

                WaterRepository repository = new WaterRepository();
                repository.enviarParaApi(data, new WaterRepository.PotabilityCallback() {
                    @Override
                    public void onResult(PotabilityResponse response) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                progressBar.setVisibility(View.GONE);
                                txtResultadoPotavel.setText(response.getPotability());
                                txtResultadoPotavel.setVisibility(View.VISIBLE);
                            });
                        }
                    }

                    @Override
                    public void onError(String error) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                progressBar.setVisibility(View.GONE);
                                txtResultadoPotavel.setText(error);
                                txtResultadoPotavel.setVisibility(View.VISIBLE);
                            });
                        }
                    }
                });

            } catch (Exception e) {
                progressBar.setVisibility(View.GONE);
                txtResultadoPotavel.setText("Campos inválidos");
                txtResultadoPotavel.setVisibility(View.VISIBLE);
            }
        });



        return view;
    }

    private void configurarValidacoes(View view) {
        // Inicializa inputs de classe
        inputPh = view.findViewById(R.id.inputPh);
        TextInputLayout phLayout = view.findViewById(R.id.PhLayout);

        inputDureza = view.findViewById(R.id.inputDureza);
        TextInputLayout durezaLayout = view.findViewById(R.id.durezaLayout);

        inputSolidos = view.findViewById(R.id.inputSolidos);
        TextInputLayout solidosLayout = view.findViewById(R.id.solidosLayout);

        inputCloraminas = view.findViewById(R.id.inputCloraminas);
        TextInputLayout cloraminasLayout = view.findViewById(R.id.cloraminasLayout);

        inputSulfato = view.findViewById(R.id.inputSulfato);
        TextInputLayout sulfatoLayout = view.findViewById(R.id.sulfatoLayout);

        inputCondutividade = view.findViewById(R.id.inputCondutividade);
        TextInputLayout condutividadeLayout = view.findViewById(R.id.condutividadeLayout);

        inputCarbono = view.findViewById(R.id.inputCarbonoOrganico);
        TextInputLayout carbonoLayout = view.findViewById(R.id.carbonoOrganicoLayout);

        inputTrihalometanos = view.findViewById(R.id.inputTrihalometanos);
        TextInputLayout trihaloLayout = view.findViewById(R.id.trihalometanosLayout);

        inputTurbidez = view.findViewById(R.id.inputTurbidez);
        TextInputLayout turbidezLayout = view.findViewById(R.id.turbidezLayout);

        //Filtro
        InputFilter filtroDecimal = (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if (!Character.isDigit(c) && c != '.') {
                    return "";
                }

                if (c == '.' && dest.toString().contains(".")) {
                    return "";
                }
            }
            return null;
        };

        inputPh.setFilters(new InputFilter[]{filtroDecimal});
        inputDureza.setFilters(new InputFilter[]{filtroDecimal});
        inputSolidos.setFilters(new InputFilter[]{filtroDecimal});
        inputCloraminas.setFilters(new InputFilter[]{filtroDecimal});
        inputSulfato.setFilters(new InputFilter[]{filtroDecimal});
        inputCondutividade.setFilters(new InputFilter[]{filtroDecimal});
        inputCarbono.setFilters(new InputFilter[]{filtroDecimal});
        inputTrihalometanos.setFilters(new InputFilter[]{filtroDecimal});
        inputTurbidez.setFilters(new InputFilter[]{filtroDecimal});

        // validação geral
        validarCampo(inputPh, phLayout, 1.0, 14.0, "pH");
        validarCampo(inputDureza, durezaLayout, 0.0, 500.0, "Dureza");
        validarCampo(inputSolidos, solidosLayout, 0.0, 50000.0, "Sólidos");
        validarCampo(inputCloraminas, cloraminasLayout, 0.0, 10.0, "Cloraminas");
        validarCampo(inputSulfato, sulfatoLayout, 0.0, 500.0, "Sulfato");
        validarCampo(inputCondutividade, condutividadeLayout, 0.0, 2000.0, "Condutividade");
        validarCampo(inputCarbono, carbonoLayout, 0.0, 20.0, "Carbono Orgânico");
        validarCampo(inputTrihalometanos, trihaloLayout, 0.0, 100.0, "Trihalometanos");
        validarCampo(inputTurbidez, turbidezLayout, 0.0, 100.0, "Turbidez");

    }

    private void validarCampo(TextInputEditText campo, TextInputLayout layout, double min, double max, String nomeCampo) {
        campo.addTextChangedListener(new SimpleTextWatcher(s -> {
            if (s.isEmpty()) return null;
            try {
                double valor = Double.parseDouble(s);
                if (valor < min) return nomeCampo + " mínimo: " + min;
                if (valor > max) return nomeCampo + " máximo: " + max;
            } catch (NumberFormatException e) {
                return "Número inválido";
            }
            return null;
        }, layout));
    }

}
