package com.vireya.hydrocore.potabilidade;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.vireya.hydrocore.R;

public class Potabilidade extends Fragment {

    private TextView txtResultadoPotavel;
    private MaterialButton btnCalcularPotabilidade;

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

        btnCalcularPotabilidade.setOnClickListener(v -> {
            txtResultadoPotavel.setText("1");
            txtResultadoPotavel.setVisibility(View.VISIBLE);
        });

        return view;
    }
}
