package com.vireya.hydrocore.entrada;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.vireya.hydrocore.MainActivity;
import com.vireya.hydrocore.R;
import com.vireya.hydrocore.core.network.RetrofitClientMongo;
import com.vireya.hydrocore.entrada.api.ApiClient;
import com.vireya.hydrocore.entrada.model.Login;
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
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLayout = findViewById(R.id.emailLayout);
        senhaLayout = findViewById(R.id.senhaLayout);
        codigoLayout = findViewById(R.id.codigoLayout);

        emailEditText = findViewById(R.id.emailInput);
        senhaEditText = findViewById(R.id.senhaInput);
        codigoEmpresaEditText = findViewById(R.id.codigoInput);
        entrarButton = findViewById(R.id.entrar);
        esqueceuSenhaEditText = findViewById(R.id.txtEsqueceuSenha);
        progressBar = findViewById(R.id.progressBar);

        entrarButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String senha = senhaEditText.getText().toString().trim();
            String codigoEmpresa = codigoEmpresaEditText.getText().toString().trim();

            if (!validarCampos(email, senha, codigoEmpresa)) return;
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
        ApiClient apiClient = RetrofitClientMongo.createApi(ApiClient.class);
        Login request = new Login(email, password, codigoEmpresa);

        entrarButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        apiClient.login(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                entrarButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonString = response.body().string();
                        JSONObject json = new JSONObject(jsonString);

                        String token = json.getString("token");
                        String chaveApi = json.getString("chaveApi");

                        // 🔹 Verifica se o servidor também envia o idFuncionario e a ETA
                        int idFuncionario = json.optInt("idFuncionario", -1);
                        String eta = json.optString("eta", null);

                        SessionManager sessionManager = new SessionManager(LoginService.this);
                        sessionManager.saveSession(token, chaveApi, email);

                        if (idFuncionario != -1) {
                            sessionManager.setIdFuncionario(idFuncionario);
                            Log.d("LOGIN", "ID do funcionário salvo: " + idFuncionario);
                        }

                        if (eta != null) {
                            sessionManager.setEta(eta);
                            Log.d("LOGIN", "ETA salva: " + eta);
                        }

                        Toast.makeText(LoginService.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginService.this, MainActivity.class));
                        finish();

                    } catch (Exception e) {
                        Log.e("LOGIN", "Erro ao processar resposta: " + e.getMessage());
                        Toast.makeText(LoginService.this, "Erro ao processar resposta do servidor", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginService.this, "Usuário, senha ou código incorretos", Toast.LENGTH_SHORT).show();
                    Log.e("LOGIN", "Erro no login: código " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                entrarButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginService.this, "Falha ao conectar com o servidor", Toast.LENGTH_SHORT).show();
                Log.e("LOGIN", "Erro: " + t.getMessage());
            }
        });
    }
}
