package com.vireya.hydrocore.ui.configuracoes;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.vireya.hydrocore.R;

public class Configuracoes extends Fragment {

    private boolean notificacaoAtivo;
    private boolean offlineAtivo;

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


        notificacaoAtivo = getSavedState("notificacaoAtivo", true);
        offlineAtivo = getSavedState("offlineAtivo", false);

        setupToggle(view, R.id.toggleWifi, R.id.thumbWifi,
                () -> {
                    boolean modoEscuroAtivo = getSavedState("modoEscuro", false);
                    boolean novoEstado = !modoEscuroAtivo;
                    saveState("modoEscuro", novoEstado);

                    if (novoEstado) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }

                    requireActivity().recreate();
                },
                () -> getSavedState("modoEscuro", false)
        );

        setupToggle(view, R.id.toggleNotificacao, R.id.thumbBluetooth,
                () -> {
                    notificacaoAtivo = !notificacaoAtivo;
                    saveState("notificacaoAtivo", notificacaoAtivo);
                },
                () -> notificacaoAtivo);

        setupToggle(view, R.id.toggleOffline, R.id.thumbOffline,
                () -> {
                    offlineAtivo = !offlineAtivo;
                    saveState("offlineAtivo", offlineAtivo);
                },
                () -> offlineAtivo);

        LinearLayout layoutInfo = view.findViewById(R.id.layoutInfo);
        layoutInfo.setOnClickListener(v -> showInformations());
    }

    private void showInformations() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.informacoesConfig, null,
                new androidx.navigation.NavOptions.Builder()
                        .setPopUpTo(R.id.navigation_home, true)
                        .build()
        );
    }

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
        float destino = ativo ? (float) deslocamento : 0f;
        ObjectAnimator animator = ObjectAnimator.ofFloat(thumb, "translationX", destino);
        animator.setDuration(200);
        animator.start();
    }

    private void saveState(String key, boolean value) {
        requireContext()
                .getSharedPreferences("configuracoes", android.content.Context.MODE_PRIVATE)
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    private boolean getSavedState(String key, boolean defaultValue) {
        return requireContext()
                .getSharedPreferences("configuracoes", android.content.Context.MODE_PRIVATE)
                .getBoolean(key, defaultValue);
    }

    private interface StateCallback {
        boolean isActive();
    }
}
