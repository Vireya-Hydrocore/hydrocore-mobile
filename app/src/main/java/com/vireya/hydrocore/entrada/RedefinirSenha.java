package com.vireya.hydrocore.entrada;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.vireya.hydrocore.MainActivity;
import com.vireya.hydrocore.R;
import com.vireya.hydrocore.entrada.api.ApiService;
import com.vireya.hydrocore.core.network.RetrofitClientMongo;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RedefinirSenha extends AppCompatActivity {

    private EditText novaSenhaInput, confirmaSenhaInput;
    private Button redefinirButton;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_redefinir_senha);

        email = getIntent().getStringExtra("email_usuario");

        novaSenhaInput = findViewById(R.id.senhaInput);
        confirmaSenhaInput = findViewById(R.id.senhaInput2);
        redefinirButton = findViewById(R.id.redefinir);


        redefinirButton.setOnClickListener(v -> redefinirSenha());
    }

    private void redefinirSenha() {
        String novaSenha = novaSenhaInput.getText().toString().trim();
        String confirmaSenha = confirmaSenhaInput.getText().toString().trim();

        if (novaSenha.isEmpty() || confirmaSenha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!novaSenha.equals(confirmaSenha)) {
            Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClientMongo.createApi(ApiService.class);

        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("novaSenha", novaSenha);
        body.put("confirmaSenha", confirmaSenha);

        apiService.resetPassword(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RedefinirSenha.this, "Senha redefinida com sucesso!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RedefinirSenha.this, LoginService.class));
                    finish();
                } else {
                    Toast.makeText(RedefinirSenha.this, "Erro ao redefinir senha: " + response.code(), Toast.LENGTH_LONG).show();
                    Log.e("RESET_SENHA", "Erro: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RedefinirSenha.this, "Falha de conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("RESET_SENHA", "Falha: " + t.getMessage());
            }
        });
    }
}
