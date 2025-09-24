package com.vireya.hydrocore.ui.configuracoes;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vireya.hydrocore.R;
import com.vireya.hydrocore.entrada.EsqueceuSenha;

public class InformacoesConfig extends Fragment {
    TextView senha;
    ImageView setaEsquerda;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_design_info, container, false);

        senha = view.findViewById(R.id.esqueceuSenha);
        setaEsquerda = view.findViewById(R.id.seta_esquerda);

        senha.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EsqueceuSenha.class);
                startActivity(intent);
            }
        });

        setaEsquerda.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.navigation_configuracoes, null,
                        new androidx.navigation.NavOptions.Builder()
                                .setPopUpTo(R.id.navigation_home, true)
                                .build()
                );
            }


        });

        return view;
    }
}
