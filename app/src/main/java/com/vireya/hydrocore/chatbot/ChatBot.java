package com.vireya.hydrocore.chatbot;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vireya.hydrocore.R;
import com.vireya.hydrocore.chatbot.adapter.ChatAdapter;
import com.vireya.hydrocore.chatbot.model.Message;
import com.vireya.hydrocore.utils.SessionManager;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatBot extends Fragment {

    private RecyclerView recyclerView;
    private EditText etMessage;
    private Button btnSend;
    private ChatAdapter adapter;
    private ArrayList<Message> messages = new ArrayList<>();

    private static final String API_URL = "https://chatbotvireyafim.onrender.com/chat";

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_bot, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        etMessage = view.findViewById(R.id.etMessage);
        btnSend = view.findViewById(R.id.btnSend);

        adapter = new ChatAdapter(messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        btnSend.setOnClickListener(v -> {
            String text = etMessage.getText().toString().trim();
            if (!text.isEmpty()) {
                sendMessage(text);
                etMessage.setText("");
            }

            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(etMessage.getWindowToken(), 0);
            }
        });

        return view;
    }

    private void sendMessage(String text) {
        messages.add(new Message(text, true));
        adapter.notifyItemInserted(messages.size() - 1);
        recyclerView.scrollToPosition(messages.size() - 1);

        SessionManager session = new SessionManager(requireContext());
        String apiKey = session.getApiKey();
        String email = session.getEmail();

        JSONObject json = new JSONObject();
        try {
            json.put("user_message", text);
            json.put("api_key", apiKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        String urlWithEmail;
        try {
            urlWithEmail = API_URL + "?email=" + URLEncoder.encode(email, "UTF-8");
        } catch (Exception e) {
            urlWithEmail = API_URL + "?email=" + email;
        }

        Request request = new Request.Builder()
                .url(urlWithEmail)
                .addHeader("Authorization", "Bearer dofhaidfpdhfodhf39e93eur4fefo")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                addBotMessage("Erro ao conectar com o servidor. Tente novamente.");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        String botReply = jsonResponse.optString("resposta", "Desculpe, não entendi.");
                        addBotMessage(botReply);
                    } catch (Exception e) {
                        e.printStackTrace();
                        addBotMessage("Erro ao processar resposta do servidor.");
                    }
                } else {
                    String msg = "Resposta inválida do servidor. (code: " + response.code() + ")";
                    addBotMessage(msg);
                }
            }
        });
    }


    private void addBotMessage(String text) {
        new Handler(Looper.getMainLooper()).post(() -> {
            messages.add(new Message(text, false));
            adapter.notifyItemInserted(messages.size() - 1);
            recyclerView.scrollToPosition(messages.size() - 1);
        });
    }
}
