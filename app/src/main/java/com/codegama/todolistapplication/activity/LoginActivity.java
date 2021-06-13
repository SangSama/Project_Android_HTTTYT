package com.codegama.todolistapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codegama.todolistapplication.R;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText etUsername, etPassword;
    Button btSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btSubmit = findViewById(R.id.bt_submit);

        btSubmit.setOnClickListener(this::onClick);
    }

    private void onClick(View v) {
        if (etUsername.getText().toString().equals("admin") &&
                etPassword.getText().toString().equals("admin")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            // chuyển màn hình
        } else if (etUsername.getText().toString().equals("") ||
                etPassword.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Empty Username or Password", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(),
                    "Invalid Username or Password", Toast.LENGTH_SHORT).show();
        }
    }
}
