package com.vireya.hydrocore.entrada;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vireya.hydrocore.R;
import com.vireya.hydrocore.core.network.RetrofitClientMongo;
import com.vireya.hydrocore.entrada.api.ApiService;
import com.vireya.hydrocore.entrada.model.Login;
import com.vireya.hydrocore.entrada.model.LoginResponse;
import com.vireya.hydrocore.ui.Home;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginService extends AppCompatActivity {

    private EditText emailEditText, senhaEditText, codigoEmpresaEditText;
    private Button entrarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Referências aos campos
        emailEditText = findViewById(R.id.emailInput);
        senhaEditText = findViewById(R.id.senhaInput);
        codigoEmpresaEditText = findViewById(R.id.codigoInput);
        entrarButton = findViewById(R.id.entrar);

        // Clique do botão
        entrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String senha = senhaEditText.getText().toString().trim();
                String codigoEmpresa = codigoEmpresaEditText.getText().toString().trim();

                if (email.isEmpty() || senha.isEmpty() || codigoEmpresa.isEmpty()) {
                    Toast.makeText(LoginService.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                realizarLogin(email, senha, codigoEmpresa);
            }
        });
    }

    private void realizarLogin(String email, String senha, String codigoEmpresa) {
        ApiService apiService = RetrofitClientMongo.createApi(ApiService.class);
        Login request = new Login(email, senha, codigoEmpresa);

        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String jwt = response.body().getToken();
                    Log.d("LOGIN", "Token JWT: " + jwt);

                    // Salva o token JWT no cliente Retrofit
                    RetrofitClientMongo.setTokens(jwt, null);

                    Toast.makeText(LoginService.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();

                    // Vai para a Home
                    Intent intent = new Intent(LoginService.this, Home.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginService.this, "Usuário, senha ou código incorretos", Toast.LENGTH_SHORT).show();
                    Log.e("LOGIN", "Erro no login: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginService.this, "Falha ao conectar com o servidor", Toast.LENGTH_SHORT).show();
                Log.e("LOGIN", "Erro: " + t.getMessage());
            }
        });
    }
}
