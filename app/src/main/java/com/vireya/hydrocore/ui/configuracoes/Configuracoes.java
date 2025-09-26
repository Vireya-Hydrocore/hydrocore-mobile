package com.vireya.hydrocore.ui.configuracoes;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.vireya.hydrocore.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Configuracoes extends Fragment {

    private boolean wifiAtivo;
    private boolean notificacaoAtivo;
    private boolean offlineAtivo;

    private static final int REQUEST_CAMERA = 100;
    private static final int REQUEST_GALLERY = 101;

    private ImageView imgProfile;
    private ImageView seta;

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

        imgProfile = view.findViewById(R.id.imgProfile);
        seta = view.findViewById(R.id.arrow_info);

        checkPermissions();
        loadImageFromInternalStorage();

        imgProfile.setOnClickListener(v -> showImageOptions());
        seta.setOnClickListener(v -> showInformations());

        // 🔹 Carrega estados salvos
        wifiAtivo = getSavedState("wifiAtivo", false);
        notificacaoAtivo = getSavedState("notificacaoAtivo", true);
        offlineAtivo = getSavedState("offlineAtivo", false);

        // 🔹 Inicializa os toggles já na posição correta
        setupToggle(view, R.id.toggleWifi, R.id.thumbWifi,
                () -> {
                    wifiAtivo = !wifiAtivo;
                    saveState("wifiAtivo", wifiAtivo);
                },
                () -> wifiAtivo);

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
    }

    private void showInformations(){
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.informacoesConfig, null,
                new androidx.navigation.NavOptions.Builder()
                        .setPopUpTo(R.id.navigation_home, true)
                        .build()
        );
    }

    private void showImageOptions() {
        String[] options = {"Visualizar foto", "Tirar foto", "Escolher da galeria"};

        new AlertDialog.Builder(requireContext())
                .setTitle("Foto de Perfil")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) visualizarFoto();
                    else if (which == 1) openCamera();
                    else openGallery();
                }).show();
    }

    private void visualizarFoto() {
        imgProfile.setDrawingCacheEnabled(true);
        Bitmap bitmap = imgProfile.getDrawingCache();

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        ImageView imageView = new ImageView(requireContext());
        imageView.setImageBitmap(bitmap);
        builder.setView(imageView);
        builder.setPositiveButton("Fechar", null);
        builder.show();
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == requireActivity().RESULT_OK && data != null) {
            if (requestCode == REQUEST_CAMERA) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imgProfile.setImageBitmap(imageBitmap);
                saveImageToInternalStorage(imageBitmap);

            } else if (requestCode == REQUEST_GALLERY) {
                Uri selectedImage = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImage);
                    imgProfile.setImageBitmap(bitmap);
                    saveImageToInternalStorage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 🔹 Configura os botões de toggle
    private void setupToggle(@NonNull View parentView, int toggleId, int thumbId,
                             Runnable toggleStateCallback, StateCallback isActiveCallback) {

        LinearLayout toggle = parentView.findViewById(toggleId);
        View thumb = parentView.findViewById(thumbId);

        toggle.post(() -> {
            final int deslocamento = toggle.getWidth() - thumb.getWidth() - 12;

            // 🔹 Posição inicial
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

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    200
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void saveImageToInternalStorage(Bitmap bitmap) {
        try {
            String filename = "profile_image.png";
            FileOutputStream fos = requireContext().openFileOutput(filename, android.content.Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            requireContext()
                    .getSharedPreferences("configuracoes", android.content.Context.MODE_PRIVATE)
                    .edit()
                    .putString("profileImage", filename)
                    .apply();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Erro ao salvar imagem", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadImageFromInternalStorage() {
        String filename = requireContext()
                .getSharedPreferences("configuracoes", android.content.Context.MODE_PRIVATE)
                .getString("profileImage", null);

        if (filename != null) {
            try {
                FileInputStream fis = requireContext().openFileInput(filename);
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                imgProfile.setImageBitmap(bitmap);
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 🔹 Helpers para salvar/ler estados dos toggles
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
