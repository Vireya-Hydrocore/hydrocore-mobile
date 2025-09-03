package com.vireya.hydrocore;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class RedefinirSenha extends AppCompatActivity {
    FirebaseAuth objAuth = FirebaseAuth.getInstance();
    private String oobCode; // código que vem do link do e-mail

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_redefinir_senha);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.redefinir), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // pega o código que o Firebase envia no link
        oobCode = getIntent().getStringExtra("oobCode");

        EditText edtSenha = findViewById(R.id.senhaInput2);
        EditText edtConfirmaSenha = findViewById(R.id.senhaInput);
        Button btnRedefinir = findViewById(R.id.redefinir);

        btnRedefinir.setOnClickListener(v -> {
            String senha = edtSenha.getText().toString().trim();
            String confirma = edtConfirmaSenha.getText().toString().trim();

            if (senha.isEmpty() || confirma.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!senha.equals(confirma)) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                return;
            }

            if (oobCode != null) {
                objAuth.confirmPasswordReset(oobCode, senha)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Senha alterada com sucesso", Toast.LENGTH_SHORT).show();
                                createNotification("Senha redefinida", "Sua senha foi alterada com sucesso!");
                                finish();
                            } else {
                                Toast.makeText(this, "Erro ao redefinir senha", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void createNotification(String title, String content) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "reset_password_channel",
                    "Reset Password",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Notificações de redefinição de senha");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "reset_password_channel")
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        managerCompat.notify(1, builder.build());
    }
}