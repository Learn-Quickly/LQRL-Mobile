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
import com.lqrl.school.web_services.ChangePasswordTask;

public class ChangePasswordActivity extends AppCompatActivity implements ActivityRedirecter {
    private TextInputLayout editTextPassword, editTextNewPassword;
    private Button btnChangePassword;
    public boolean singInState = false;
    public String accessToken = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        Intent intent = getIntent();
        this.accessToken = intent.getStringExtra("access_token");
        this.singInState = intent.getBooleanExtra("did_login", false);

        editTextPassword = findViewById(R.id.editTextPassword);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        btnChangePassword = findViewById(R.id.btnChangePwd);
        btnChangePassword.setOnClickListener(v -> changePassword());

        TextView hyperlinkLogin = findViewById(R.id.tv_signin_ref);

        String textLogin = getString(R.string.login);
        SpannableString spannableStringLogin = new SpannableString(textLogin);

        CustomClickableSpan customClickableSpanLogin = new CustomClickableSpan(this, CustomClickableSpan.Action.Login);

        spannableStringLogin.setSpan(customClickableSpanLogin, 0, textLogin.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        hyperlinkLogin.setText(spannableStringLogin);
        hyperlinkLogin.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void changePassword(){
        if(!singInState){
            Toast.makeText(this, R.string.please_log_in, Toast.LENGTH_SHORT).show();
            return;
        }
        if(editTextNewPassword.getEditText().getText().toString().isEmpty() || editTextPassword.getEditText().getText().toString().isEmpty()){
            Toast.makeText(this, R.string.empty_text_field_s, Toast.LENGTH_SHORT).show();
            return;
        }

        String oldP = editTextPassword.getEditText().getText().toString();
        String newP = editTextNewPassword.getEditText().getText().toString();
        if(oldP.equals(newP)){
            Toast.makeText(this, R.string.error_passwords_match, Toast.LENGTH_SHORT).show();
        } else {
            ChangePasswordTask changePasswordTask = new ChangePasswordTask(this, accessToken, oldP, newP);
            changePasswordTask.execute();
        }
    }

    @Override
    public void redirectSignUpActivity(Bundle bundle) {
        // Not used
    }

    @Override
    public void redirectChangePwdActivity(Bundle bundle) {
        // No sense
    }

    @Override
    public void redirectSignInActivity(Bundle bundle) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
