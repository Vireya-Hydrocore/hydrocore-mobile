package com.vireya.hydrocore.potabilidade;

import android.text.Editable;
import android.text.TextWatcher;
import com.google.android.material.textfield.TextInputLayout;

public class SimpleTextWatcher implements TextWatcher {
    private final java.util.function.Function<String, String> validator;
    private final TextInputLayout layout;

    public SimpleTextWatcher(java.util.function.Function<String, String> validator, TextInputLayout layout) {
        this.validator = validator;
        this.layout = layout;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        String error = validator.apply(s.toString());
        layout.setError(error);
    }
}
