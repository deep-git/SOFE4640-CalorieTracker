package com.example.calorietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    TextView back;
    TextView account;
    TextView signUp;
    EditText enterUsername;
    EditText enterEmail;
    EditText enterPassword;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        back = (TextView) findViewById(R.id.back);
        account = (TextView) findViewById(R.id.account);
        signUp = (TextView) findViewById(R.id.signUp);
        enterUsername = (EditText) findViewById(R.id.username);
        enterEmail = (EditText) findViewById(R.id.email);
        enterPassword = (EditText) findViewById(R.id.password);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        });

        account.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String username = enterUsername.getText().toString();
                String email = enterEmail.getText().toString();
                String password = enterPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Register.this, "All Fields Required!", Toast.LENGTH_SHORT).show();
                } else if (!(email.isEmpty() && password.isEmpty())){
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(Register.this, Home.class));
                            } else {
                                Toast.makeText(Register.this, "Register failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(Register.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}