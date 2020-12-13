package com.fyp.kafin.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import static android.content.ContentValues.TAG;
import com.fyp.kafin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText loginEmail, loginPassword;
    private FirebaseAuth mAuth;
    private MaterialButton loginButton;
    FirebaseUser user;
    ProgressBar loginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user != null) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        loginProgress = findViewById(R.id.progressBarLogin);
        loginProgress.setVisibility(View.INVISIBLE);
        loginButton.setOnClickListener(this);
        TextView goToSignup = findViewById(R.id.goToSignup);
        goToSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginButton) {
            loginProgress.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
            String emailData = loginEmail.getText().toString();
            String passwordData = loginPassword.getText().toString();
            if(emailData.equals("") || passwordData.equals("")) {
                Toast.makeText(getApplicationContext(), "Please input your username and password", Toast.LENGTH_SHORT).show();
                loginProgress.setVisibility(View.INVISIBLE);
                loginButton.setVisibility(View.VISIBLE);
            } else {
                mAuth.signInWithEmailAndPassword(emailData, passwordData).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }
                        else  {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed. Please check your credentials.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        loginProgress.setVisibility(View.INVISIBLE);
                        loginButton.setVisibility(View.VISIBLE);
                    }
                });
            }

        } else if(v.getId() == R.id.goToSignup) {
            Intent signup = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(signup);
            finish();
        }
    }

    private void updateUI(FirebaseUser user) {
        if(user != null) {
            Intent main = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(main);
            finish();
        }
    }
}