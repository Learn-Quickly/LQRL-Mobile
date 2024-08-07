package com.lqrl.school;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.lqrl.school.interfaces.ActivityRedirecter;
import com.lqrl.school.web_services.RegisterTask;

public class SignUpActivity extends AppCompatActivity implements ActivityRedirecter {

    private TextInputLayout editTextEmail, editTextPassword;
    private Button btnSignUp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextEmail = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(v -> signUp());

        TextView hyperlinkLogin = findViewById(R.id.tv_signin_ref);

        String textLogin = getString(R.string.login);
        SpannableString spannableStringLogin = new SpannableString(textLogin);

        CustomClickableSpan customClickableSpanLogin = new CustomClickableSpan(this, CustomClickableSpan.Action.Login);

        spannableStringLogin.setSpan(customClickableSpanLogin, 0, textLogin.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        hyperlinkLogin.setText(spannableStringLogin);
        hyperlinkLogin.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void signUp() {
        String username = editTextEmail.getEditText().getText().toString().trim();
        String password = editTextPassword.getEditText().getText().toString().trim();
        PasswordValidator.PasswordStrength passwordStrength = PasswordValidator.validatePassword(this, password);
        if(passwordStrength == PasswordValidator.PasswordStrength.WEAK){
            Toast.makeText(this, R.string.insufficient_password_strength, Toast.LENGTH_SHORT).show();
            return;
        }
        RegisterTask registration = new RegisterTask(this, username, password);
        registration.execute();
    }

    @Override
    public void redirectSignUpActivity(Bundle bundle) {
        // No sense
    }

    @Override
    public void redirectChangePwdActivity(Bundle bundle) {
        // Not used
    }

    @Override
    public void redirectSignInActivity(Bundle bundle) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
