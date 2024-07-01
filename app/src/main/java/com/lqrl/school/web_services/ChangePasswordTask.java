package com.lqrl.school.web_services;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.lqrl.school.BuildConfig;
import com.lqrl.school.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChangePasswordTask extends AsyncTask<Void, Void, String> {
    String accessToken, oldP, newP;
    OkHttpClient client = new OkHttpClient();
    Context context;
    public ChangePasswordTask(Context activity, String accessToken, String oldP, String newP){
        this.context = activity;
        this.accessToken = accessToken;
        this.oldP = oldP;
        this.newP = newP;
    }
    @Override
    protected String doInBackground(Void... voids) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String requestBody = "";
        JSONObject rbj = new JSONObject();
        try {
            rbj.put("pwd_clear", oldP);
            rbj.put("new_pwd_clear", newP);
            requestBody = rbj.toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(requestBody, JSON);
        Request.Builder requestBuilder = new Request.Builder().url(BuildConfig.SERVER_ROOT + "/api/user/change_pwd").put(body);
        requestBuilder.addHeader("Authorization", "Bearer " + accessToken);
        Request request = requestBuilder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Запрос к серверу не был успешен: " +
                        response.code() + " " + response.message() + response.body().string());
            }
            return response.body().string();

        } catch (IOException e) {
            System.out.println("Ошибка подключения: " + e);
            return "";
        }
    }

    @Override
    protected void onPostExecute(String result){
        if(result.toUpperCase().contains("true".toUpperCase())){
            Toast.makeText(context, R.string.password_changed_successfully, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(context, R.string.server_error_wrong_old_password, Toast.LENGTH_SHORT).show();
        }
    }
}
