package com.lqrl.school;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lqrl.school.web_services.ChangePasswordTask;
import com.lqrl.school.web_services.LoginTask;
import com.lqrl.school.web_services.RegisterTask;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class SignInSignUpActivity extends AppCompatActivity implements TokenSetter {

    private EditText editTextEmail, editTextPassword, editTextNewPassword;
    private Button btnSignIn, btnSignUp, btnChangePassword;
    public String accessToken = "";
    public boolean singInState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_signup);

        editTextEmail = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnChangePassword = findViewById(R.id.btnChangePwd);
        btnChangePassword.setOnClickListener(v -> changePassword());
        btnSignIn.setOnClickListener(v -> {
            signIn();
            Intent intent = new Intent(this, CoursesActivity.class);
            Bundle args = new Bundle();
            args.putString("access_token", accessToken);
            intent.putExtras(args);
            startActivity(intent);
        });
        btnSignUp.setOnClickListener(v -> signUp());
    }

    private void signIn() {
        String username = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        LoginTask loginTask = new LoginTask(this, username, password);
        loginTask.execute();
    }

    private void signUp() {
        String username = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        RegisterTask registration = new RegisterTask(this, username, password);
        registration.execute();
    }

    private void changePassword(){
        if(!singInState){
            Toast.makeText(this, "Please, Log in.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(editTextNewPassword.getText().toString().isEmpty() || editTextPassword.getText().toString().isEmpty()){
            Toast.makeText(this, "Empty text field(s).", Toast.LENGTH_SHORT).show();
            return;
        }

        String oldP = editTextPassword.getText().toString();
        String newP = editTextNewPassword.getText().toString();
        if(oldP.equals(newP)){
            Toast.makeText(this, "Error: passwords match", Toast.LENGTH_SHORT).show();

        } else {
            ChangePasswordTask changePasswordTask = new ChangePasswordTask(this, accessToken, oldP, newP);
            changePasswordTask.execute();
        }
    }

    @Override
    public void setSingInStatus(String token, boolean state) {
        if(state) {
            accessToken = token;
            singInState = true;
            Toast.makeText(this, "Signed in successfully!", Toast.LENGTH_SHORT).show();

        } else {
            singInState = false;
            Toast.makeText(this, "Empty token due to an error!", Toast.LENGTH_SHORT).show();
        }
    }
}
