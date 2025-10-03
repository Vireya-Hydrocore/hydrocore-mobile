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
import android.widget.Toast;

import com.vireya.hydrocore.R;
import com.vireya.hydrocore.core.network.RetrofitClient;
import com.vireya.hydrocore.entrada.EsqueceuSenha;
import com.vireya.hydrocore.ui.configuracoes.api.ApiService;
import com.vireya.hydrocore.ui.configuracoes.model.Funcionario;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InformacoesConfig extends Fragment {
    TextView senha, tvNome, tvEmail, tvCargo;
    ImageView setaEsquerda;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_design_info, container, false);

        senha = view.findViewById(R.id.esqueceuSenha);
        setaEsquerda = view.findViewById(R.id.seta_esquerda);

        tvNome = view.findViewById(R.id.textView7);
        tvEmail = view.findViewById(R.id.textView9);
        tvCargo = view.findViewById(R.id.textView11);

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

        carregarInfosApi();
        return view;
    }

    public void carregarInfosApi(){
        RetrofitClient retrofit = new RetrofitClient();
        ApiService apiService = retrofit.getRetrofit().create(ApiService.class);

        apiService.getFuncionarios().enqueue(new Callback<List<Funcionario>>() {
            @Override
            public void onResponse(Call<List<Funcionario>> call, Response<List<Funcionario>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Funcionario funcionario = response.body().get(0);

                    tvNome.setText(funcionario.getNome());
                    tvEmail.setText(funcionario.getEmail());
                    tvCargo.setText(String.valueOf(funcionario.getCargo()));

                }
            }

            @Override
            public void onFailure(Call<List<Funcionario>> call, Throwable t) {
                Toast.makeText(getContext(), "Erro ao carregar informações", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
