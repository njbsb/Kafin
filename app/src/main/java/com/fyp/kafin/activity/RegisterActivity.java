package com.fyp.kafin.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.kafin.R;
import com.fyp.kafin.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

public class RegisterActivity extends AppCompatActivity {

    private EditText email, name, password, confirmpassword;
    ProgressBar registerProgress;
    MaterialTextView goToLogin;
    MaterialButton btn_signup;
    private FirebaseAuth mAuth;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.signup_email);
        name = findViewById(R.id.signup_name);
        password = findViewById(R.id.signup_password);
        confirmpassword = findViewById(R.id.signup_confirmpassword);
        btn_signup = findViewById(R.id.btn_signup);
        goToLogin = findViewById(R.id.goToLogin);
        registerProgress = findViewById(R.id.progressBarRegister);
        registerProgress.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signIn = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(signIn);
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerProgress.setVisibility(View.VISIBLE);
                btn_signup.setVisibility(View.INVISIBLE);
                String emailval = email.getText().toString();
                final String nameval = name.getText().toString();
                String passval = password.getText().toString();
                String confirmval = confirmpassword.getText().toString();
                boolean allEmpty = emailval.equals("") || nameval.equals("") || passval.equals("") || confirmval.equals("");
                if(!passval.equals(confirmval) || allEmpty) {
                    Toast.makeText(getApplicationContext(), "Password mismatch / empty fields detected!", Toast.LENGTH_SHORT).show();
                    registerProgress.setVisibility(View.INVISIBLE);
                    btn_signup.setVisibility(View.VISIBLE);
                }
                else {
                    mAuth.createUserWithEmailAndPassword(emailval, passval).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {

                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                final FirebaseUser user = mAuth.getCurrentUser();
                                assert user != null;
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(nameval)
                                        .build();
                                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        DatabaseReference userRef = dbRef.child("users").child(user.getUid());
                                        User appUser = new User();
                                        appUser.setUserID(user.getUid());
                                        appUser.setUserEmail(user.getEmail());
                                        appUser.setUsername(user.getDisplayName());
                                        userRef.setValue(appUser);
                                        updateUI(user);
                                    }
                                });

                            } else  {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                            registerProgress.setVisibility(View.INVISIBLE);
                            btn_signup.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user == null) {
            Toast.makeText(getApplicationContext(), "Something wrong happened. Please review your registration and try again", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Your registration is successful. Please sign in using the registered credentials", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(login);
            finish();
        }
    }
}