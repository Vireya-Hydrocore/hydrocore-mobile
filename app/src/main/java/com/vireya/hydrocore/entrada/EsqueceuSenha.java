package com.vireya.hydrocore.entrada;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.vireya.hydrocore.R;
import com.vireya.hydrocore.core.network.RetrofitClientMongo;
import com.vireya.hydrocore.entrada.api.ApiService;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EsqueceuSenha extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_esqueceu_senha);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button recuperarButton = findViewById(R.id.button3);
        TextInputEditText emailInput = findViewById(R.id.emailInput2);

        recuperarButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Digite seu e-mail", Toast.LENGTH_SHORT).show();
                return;
            }

            enviarEmailRecuperacao(email);
        });
    }

    private void enviarEmailRecuperacao(String email) {
        ApiService apiService = RetrofitClientMongo.createApi(ApiService.class);

        Map<String, String> body = new HashMap<>();
        body.put("email", email);

        Call<ResponseBody> call = apiService.forgotPassword(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EsqueceuSenha.this, "E-mail de recuperação enviado!", Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    Toast.makeText(EsqueceuSenha.this, "Usuário não encontrado.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(EsqueceuSenha.this, "Erro ao enviar o e-mail. Tente novamente.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EsqueceuSenha.this, "Falha na conexão com o servidor.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
