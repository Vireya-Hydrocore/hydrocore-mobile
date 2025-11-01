package com.vireya.hydrocore.ui.configuracoes;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.imageview.ShapeableImageView;
import com.vireya.hydrocore.R;
import com.vireya.hydrocore.core.network.RetrofitClient;
import com.vireya.hydrocore.funcionario.model.Funcionario;
import com.vireya.hydrocore.ui.configuracoes.api.ApiService;

import com.vireya.hydrocore.utils.SessionManager;

import java.io.FileInputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Configuracoes extends Fragment {

    private boolean notificacaoAtivo;
    private boolean offlineAtivo;

    private TextView nomeFuncionario;
    private TextView cargoFuncionario;
    private ShapeableImageView imgProfile;

    public Configuracoes() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_configuracoes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nomeFuncionario = view.findViewById(R.id.txtFuncionario);
        cargoFuncionario = view.findViewById(R.id.txtCargo);
        imgProfile = view.findViewById(R.id.imgProfile);

        carregarDadosFuncionario();
        carregarImagemPerfil(); // 👈 NOVO

        notificacaoAtivo = getSavedState("notificacaoAtivo", true);
        offlineAtivo = getSavedState("offlineAtivo", false);

        // TOGGLE DO MODO ESCURO
        setupToggle(view, R.id.toggleWifi, R.id.thumbWifi,
                () -> {
                    boolean modoEscuroAtivo = getSavedState("modoEscuro", false);
                    boolean novoEstado = !modoEscuroAtivo;
                    saveState("modoEscuro", novoEstado);
                    AppCompatDelegate.setDefaultNightMode(
                            novoEstado ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
                    );
                    requireActivity().recreate();
                },
                () -> getSavedState("modoEscuro", false)
        );

        // TOGGLE NOTIFICAÇÃO
        setupToggle(view, R.id.toggleNotificacao, R.id.thumbBluetooth,
                () -> {
                    notificacaoAtivo = !notificacaoAtivo;
                    saveState("notificacaoAtivo", notificacaoAtivo);
                },
                () -> notificacaoAtivo);

        // TOGGLE OFFLINE
        setupToggle(view, R.id.toggleOffline, R.id.thumbOffline,
                () -> {
                    offlineAtivo = !offlineAtivo;
                    saveState("offlineAtivo", offlineAtivo);
                },
                () -> offlineAtivo);

        // Clique no layout da foto → vai para o Perfil
        LinearLayout layoutFotoPerfil = view.findViewById(R.id.layoutFotoPerfilConfig);
        layoutFotoPerfil.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_perfil);
        });

        // Navegação para informações
        LinearLayout layoutInfo = view.findViewById(R.id.layoutInfo);
        layoutInfo.setOnClickListener(v -> showInformations());
    }

    private void carregarImagemPerfil() {
        String filename = requireContext()
                .getSharedPreferences("configuracoes", Context.MODE_PRIVATE)
                .getString("profileImage", null);

        if (filename != null) {
            try (FileInputStream fis = requireContext().openFileInput(filename)) {
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                imgProfile.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                imgProfile.setImageResource(R.drawable.perfil_default);
            }
        } else {
            imgProfile.setImageResource(R.drawable.perfil_default);
        }
    }

    private void carregarDadosFuncionario() {
        SessionManager sessionManager = new SessionManager(requireContext());
        String email = sessionManager.getEmail();

        if (email == null || email.isEmpty()) {
            nomeFuncionario.setText("Usuário desconhecido");
            cargoFuncionario.setText("Sem cargo");
            return;
        }

        ApiService apiService = RetrofitClient.getRetrofit(getContext()).create(ApiService.class);
        Call<Funcionario> call = apiService.getFuncionarioByEmail(email);

        call.enqueue(new Callback<Funcionario>() {
            @Override
            public void onResponse(Call<Funcionario> call, Response<Funcionario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Funcionario funcionario = response.body();
                    nomeFuncionario.setText(funcionario.getNome());
                    cargoFuncionario.setText(funcionario.getCargo());
                } else {
                    nomeFuncionario.setText("Erro ao carregar");
                    cargoFuncionario.setText("Tente novamente");
                }
            }

            @Override
            public void onFailure(Call<Funcionario> call, Throwable t) {
                nomeFuncionario.setText("Falha de conexão");
                cargoFuncionario.setText("Offline");
            }
        });
    }

    private void showInformations() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.informacoesConfig, null,
                new androidx.navigation.NavOptions.Builder()
                        .setPopUpTo(R.id.navigation_home, true)
                        .build()
        );
    }

    // ======== TOGGLES ================
    private void setupToggle(@NonNull View parentView, int toggleId, int thumbId,
                             Runnable toggleStateCallback, StateCallback isActiveCallback) {
        LinearLayout toggle = parentView.findViewById(toggleId);
        View thumb = parentView.findViewById(thumbId);

        toggle.post(() -> {
            final int deslocamento = toggle.getWidth() - thumb.getWidth() - 12;
            boolean ativo = isActiveCallback.isActive();
            thumb.setTranslationX(ativo ? deslocamento : 0f);
            toggle.setBackgroundResource(
                    ativo ? R.drawable.toggle_background : R.drawable.toggle_background_off
            );

            toggle.setOnClickListener(v -> {
                toggleStateCallback.run();
                boolean novoEstado = isActiveCallback.isActive();
                moverThumb(thumb, novoEstado, deslocamento);
                toggle.setBackgroundResource(
                        novoEstado ? R.drawable.toggle_background : R.drawable.toggle_background_off
                );
            });
        });
    }

    private void moverThumb(View thumb, boolean ativo, int deslocamento) {
        float destino = ativo ? deslocamento : 0f;
        ObjectAnimator animator = ObjectAnimator.ofFloat(thumb, "translationX", destino);
        animator.setDuration(200);
        animator.start();
    }

    private void saveState(String key, boolean value) {
        requireContext()
                .getSharedPreferences("configuracoes", Context.MODE_PRIVATE)
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    private boolean getSavedState(String key, boolean defaultValue) {
        return requireContext()
                .getSharedPreferences("configuracoes", Context.MODE_PRIVATE)
                .getBoolean(key, defaultValue);
    }

    private interface StateCallback {
        boolean isActive();
    }
}
