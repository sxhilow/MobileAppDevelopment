package com.example.assignmentq1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    TextInputLayout nameLayout, phoneLayout, emailLayout;
    TextInputEditText etName, etPhone, etEmail;
    ToggleButton toggleBtn;
    SharedPreferences sharedPreferences;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameLayout = findViewById(R.id.nameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        phoneLayout = findViewById(R.id.phoneLayout);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);

        toggleBtn = findViewById(R.id.toggleButton);
        saveBtn = findViewById(R.id.saveButton);

        sharedPreferences = getSharedPreferences("ContactPref", MODE_PRIVATE);

        etName.setText(sharedPreferences.getString("name", ""));
        etPhone.setText(sharedPreferences.getString("phone", ""));
        etEmail.setText(sharedPreferences.getString("email", ""));

        fieldsEnabled(false);

        toggleBtn.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            fieldsEnabled(isChecked);
        }));

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (etName.getText().toString().matches("[a-zA-Z ]+")) {
                    nameLayout.setError(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (etPhone.getText().toString().matches("\\d{10,13}")) {
                    phoneLayout.setError(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
                    emailLayout.setError(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        saveBtn.setOnClickListener(v -> {
            if(validateInputs()){
                saveContact();
                Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                toggleBtn.setChecked(false);
            }else{
                Toast.makeText(this, "Fix Errors before saving", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs() {
        boolean valid = true;

        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (name.isEmpty() || !name.matches("[a-zA-Z ]+")) {
            nameLayout.setError("Name required");
            valid = false;
        } else nameLayout.setError(null);

        if (!phone.matches("\\d{10,13}")) {
            phoneLayout.setError("Invalid phone number");
            valid = false;
        } else phoneLayout.setError(null);

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Invalid email");
            valid = false;
        } else emailLayout.setError(null);

        return valid;
    }


    private void saveContact() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", etName.getText().toString());
        editor.putString("phone", etPhone.getText().toString());
        editor.putString("email", etEmail.getText().toString());
        editor.apply();
    }

    private void fieldsEnabled(boolean enabled){
        etName.setEnabled(enabled);
        etPhone.setEnabled(enabled);
        etEmail.setEnabled(enabled);
    }
}