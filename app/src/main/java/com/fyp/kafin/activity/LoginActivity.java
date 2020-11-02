package com.fyp.kafin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fyp.kafin.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText loginEmail, loginPassword;
    private Button loginButton;
    private final String email = "kafin@mail.com";
    private final String password = "kafin123";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginButton) {
            login();
            Toast.makeText(LoginActivity.this, "hello", Toast.LENGTH_SHORT).show();
        }
    }

    private void login() {
        String emailData = loginEmail.getText().toString();
        String passwordData = loginPassword.getText().toString();

        if (emailData.isEmpty() || passwordData.isEmpty()) {
            loginEmail.setError("Please enter email");
            loginPassword.setError("Please enter password");
        }
        else {
            if(!emailData.equals(email) && !passwordData.equals(password)) {
                loginEmail.setError("Please enter valid email");
                loginPassword.setError("Please enter valid password");
            } else {
                Intent intentMain = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intentMain);
                finish();
            }
        }
    }
}