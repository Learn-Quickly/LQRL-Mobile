package com.lqrl.school;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignInSignUpActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button btnSignIn, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_signup);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignIn.setOnClickListener(v -> signIn());

        btnSignUp.setOnClickListener(v -> signUp());
    }

    private void signIn() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        Intent intent = new Intent(this, CoursesActivity.class);
        Bundle args = new Bundle();
        args.putString("email", email);
        args.putString("password", password);
        intent.putExtras(args);
        startActivity(intent);
    }

    private void signUp() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        Toast.makeText(this, "Sign Up Clicked. Email: " + email + ", Password: " + password, Toast.LENGTH_SHORT).show();
    }
}
