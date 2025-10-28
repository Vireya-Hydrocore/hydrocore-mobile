package com.vireya.hydrocore.entrada;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.vireya.hydrocore.MainActivity;
import com.vireya.hydrocore.R;
import com.vireya.hydrocore.core.network.RetrofitClientMongo;
import com.vireya.hydrocore.entrada.api.ApiService;
import com.vireya.hydrocore.entrada.model.Login;
import com.vireya.hydrocore.entrada.model.LoginResponse;
import com.vireya.hydrocore.utils.SessionManager;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginService extends AppCompatActivity {

    private TextInputLayout emailLayout, senhaLayout, codigoLayout;
    private EditText emailEditText, senhaEditText, codigoEmpresaEditText;
    private TextView esqueceuSenhaEditText;
    private Button entrarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // TextInputLayouts
        emailLayout = findViewById(R.id.emailLayout);
        senhaLayout = findViewById(R.id.senhaLayout);
        codigoLayout = findViewById(R.id.codigoLayout);

        // Campos
        emailEditText = findViewById(R.id.emailInput);
        senhaEditText = findViewById(R.id.senhaInput);
        codigoEmpresaEditText = findViewById(R.id.codigoInput);
        entrarButton = findViewById(R.id.entrar);

        // TextView
        esqueceuSenhaEditText = findViewById(R.id.txtEsqueceuSenha);

        entrarButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String senha = senhaEditText.getText().toString().trim();
            String codigoEmpresa = codigoEmpresaEditText.getText().toString().trim();

            if(!validarCampos(email, senha, codigoEmpresa)) return;

            realizarLogin(email, senha, codigoEmpresa);
        });

        esqueceuSenhaEditText.setOnClickListener(v ->
                startActivity(new Intent(LoginService.this, EsqueceuSenha.class))
        );
    }

    private boolean validarCampos(String email, String senha, String codigoEmpresa) {
        boolean valido = true;

        if (email.isEmpty()) {
            emailLayout.setError("O email não pode ficar vazio");
            valido = false;
        } else if (!email.contains("@")) {
            emailLayout.setError("Email inválido");
            valido = false;
        } else emailLayout.setError(null);

        if (senha.isEmpty()) {
            senhaLayout.setError("Senha não pode ficar vazia");
            valido = false;
        } else senhaLayout.setError(null);

        if (codigoEmpresa.isEmpty()) {
            codigoLayout.setError("Código da empresa não pode ficar vazio");
            valido = false;
        } else codigoLayout.setError(null);

        return valido;
    }

    private void realizarLogin(String email, String password, String codigoEmpresa) {
        ApiService apiService = RetrofitClientMongo.createApi(ApiService.class);
        Login request = new Login(email, password, codigoEmpresa);

        apiService.login(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonString = response.body().string();
                        JSONObject json = new JSONObject(jsonString);
                        String token = json.getString("token");
                        String chaveApi = json.getString("chaveApi");

                        // Cria LoginResponse
                        LoginResponse loginResponse = new LoginResponse(token, chaveApi);

                        // Salva na sessão
                        SessionManager sessionManager = new SessionManager(LoginService.this);
                        sessionManager.saveSession(token, chaveApi, email);

                        Toast.makeText(LoginService.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();

                        // Vai pra próxima tela
                        startActivity(new Intent(LoginService.this, MainActivity.class));
                        finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(LoginService.this, "Erro ao processar resposta do servidor", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginService.this, "Usuário, senha ou código incorretos", Toast.LENGTH_SHORT).show();
                    Log.e("LOGIN", "Erro no login: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LoginService.this, "Falha ao conectar com o servidor", Toast.LENGTH_SHORT).show();
                Log.e("LOGIN", "Erro: " + t.getMessage());
            }
        });
    }
}
