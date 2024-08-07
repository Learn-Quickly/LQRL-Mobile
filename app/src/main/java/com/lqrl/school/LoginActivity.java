package com.lqrl.school;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.lqrl.school.interfaces.ActivityRedirecter;
import com.lqrl.school.interfaces.StringSetter;
import com.lqrl.school.web_services.LoginTask;

public class LoginActivity extends AppCompatActivity implements StringSetter, ActivityRedirecter {

    private TextInputLayout editTextEmail, editTextPassword;
    private Button btnSignIn;
    public String accessToken = "";
    public boolean singInState = false;
    private FloatingActionButton fab;
    public static String TAG = "LoginActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "after setContentView()");
        editTextEmail = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(v -> signIn());

        TextView hyperlinkPwdRestore = findViewById(R.id.tv_change_pwd_ref);
        TextView hyperlinkSingUp= findViewById(R.id.tv_signup_ref);

        String textPwdRestore = getString(R.string.change_password);
        SpannableString spannableStringPwdRestore = new SpannableString(textPwdRestore);

        String textSignUp = getString(R.string.sign_up);
        SpannableString spannableStringSignUp = new SpannableString(textSignUp);

        CustomClickableSpan customClickableSpanPwdRestore = new CustomClickableSpan(this, CustomClickableSpan.Action.ChangePassword);
        CustomClickableSpan customClickableSpanSignUp = new CustomClickableSpan(this, CustomClickableSpan.Action.SignUp);

        spannableStringPwdRestore.setSpan(customClickableSpanPwdRestore, 0, textPwdRestore.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableStringSignUp.setSpan(customClickableSpanSignUp, 0, textSignUp.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        hyperlinkPwdRestore.setText(spannableStringPwdRestore);
        hyperlinkPwdRestore.setMovementMethod(LinkMovementMethod.getInstance());

        hyperlinkSingUp.setText(spannableStringSignUp);
        hyperlinkSingUp.setMovementMethod(LinkMovementMethod.getInstance());

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            if(singInState){
                Intent intent = new Intent(this, HomeActivity.class);
                Bundle args = new Bundle();
                args.putString("access_token", accessToken);
                intent.putExtras(args);
                startActivity(intent);

            } else {
                Toast.makeText(this, R.string.please_log_in, Toast.LENGTH_SHORT).show();
            }
        });
        //new DemoFiller(this).execute();
    }

    private void signIn() {
        String username = editTextEmail.getEditText().getText().toString().trim();
        String password = editTextPassword.getEditText().getText().toString().trim();

        LoginTask loginTask = new LoginTask(this, username, password);
        loginTask.execute();
    }

    @Override
    public void setStringState(String src, boolean state) {
        if(state) {
            accessToken = src;
            singInState = true;
            TokenManager.saveToken(this, accessToken);
            Toast.makeText(this, R.string.signed_in_successfully, Toast.LENGTH_SHORT).show();

        } else {
            singInState = false;
            TokenManager.saveToken(this, null);
            Toast.makeText(this, R.string.empty_token_due_to_an_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void redirectSignUpActivity(Bundle bundle) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void redirectChangePwdActivity(Bundle bundle) {
        if(!singInState){
            Toast.makeText(this, R.string.please_log_in_first, Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            bundle.putString("access_token", accessToken);
            bundle.putBoolean("did_login", singInState);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void redirectSignInActivity(Bundle bundle) {
        // No sense
    }
}
