package com.lqrl.school;

import android.os.Bundle;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.content.Context;

import androidx.annotation.NonNull;

import com.lqrl.school.interfaces.ActivityRedirecter;

public class CustomClickableSpan extends ClickableSpan {
    private final Context context;
    private Action action;

    public enum Action {
        SignUp,
        ChangePassword,
        Login
    }

    public CustomClickableSpan(Context context, Action action) {
        this.context = context;
        this.action = action;
    }

    @Override
    public void onClick(@NonNull View widget) {
        if(action == Action.SignUp){
            ((ActivityRedirecter)context).redirectSignUpActivity(new Bundle());
        } else if(action == Action.ChangePassword){
            ((ActivityRedirecter)context).redirectChangePwdActivity(new Bundle());
        } else if(action == Action.Login){
            ((ActivityRedirecter)context).redirectSignInActivity(new Bundle());
        }
    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(ds.linkColor); // Set the link color (default is blue)
        ds.setUnderlineText(true); // Underline the text
    }
}
