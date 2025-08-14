package com.vireya.hydrocore;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Cadastrar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        AutoCompleteTextView cargoInput = findViewById(R.id.CargoInput);
        String[] cargos = {"Administrador", "Operador", "Técnico", "Gerente"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, cargos);
        cargoInput.setAdapter(adapter);

        cargoInput.setOnClickListener(view -> {
            cargoInput.showDropDown();
        });
    }

}