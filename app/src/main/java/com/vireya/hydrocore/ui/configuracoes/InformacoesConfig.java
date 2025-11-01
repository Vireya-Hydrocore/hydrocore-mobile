package com.vireya.hydrocore.ui.configuracoes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.vireya.hydrocore.R;
import com.vireya.hydrocore.entrada.EsqueceuSenha;
import com.vireya.hydrocore.core.network.RetrofitClient;
import com.vireya.hydrocore.funcionario.model.Funcionario;
import com.vireya.hydrocore.ui.configuracoes.api.ApiService;

import com.vireya.hydrocore.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        senha.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EsqueceuSenha.class);
            startActivity(intent);
        });

        setaEsquerda.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_configuracoes, null,
                    new androidx.navigation.NavOptions.Builder()
                            .setPopUpTo(R.id.navigation_home, true)
                            .build()
            );
        });

        carregarInfosFuncionarioLogado();
        return view;
    }

    private void carregarInfosFuncionarioLogado() {
        SessionManager session = new SessionManager(requireContext());
        String email = session.getEmail();

        if (email == null || email.isEmpty()) {
            Toast.makeText(getContext(), "Usuário não logado", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getRetrofit(getContext()).create(ApiService.class);
        apiService.getFuncionarioByEmail(email).enqueue(new Callback<Funcionario>() {
            @Override
            public void onResponse(Call<Funcionario> call, Response<Funcionario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Funcionario funcionario = response.body();

                    tvNome.setText(funcionario.getNome());
                    tvEmail.setText(email);
                    tvCargo.setText(funcionario.getCargo());

                } else {
                    Toast.makeText(getContext(), "Funcionário não encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Funcionario> call, Throwable t) {
                Toast.makeText(getContext(), "Erro ao carregar informações", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
