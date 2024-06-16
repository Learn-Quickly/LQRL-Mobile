package com.lqrl.school.web_services;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.lqrl.school.BuildConfig;
import com.lqrl.school.interfaces.StringSetter;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.Base64;
import org.json.*;

public class LoginTask extends AsyncTask<Void, Void, String> {
    public String accessToken = "";
    public String username, password;
    StringSetter context;
    public LoginTask(Context activity, String username, String password){
        this.username = username;
        this.password = password;
        this.context = (StringSetter) activity;
    }

    @Override
    protected String doInBackground(Void... voids) {
        return loginUser(username, password);
    }

    @NonNull
    public static String loginUser(String username, String password) {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String requestBody = "";

        RequestBody body = RequestBody.create(requestBody, JSON);
        String basicAuthCredentials = username + ":" + password;
        String base64EncodedCredentials = Base64.getEncoder().encodeToString(basicAuthCredentials.getBytes());
        Request.Builder requestBuilder = new Request.Builder().url(BuildConfig.SERVER_ROOT + "/api/login").post(body);
        requestBuilder.addHeader("Authorization", "Basic " + base64EncodedCredentials);
        Request request = requestBuilder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Запрос к серверу не был успешен: " +
                        response.code() + " " + response.message());
            }
            return response.body().string();

        } catch (IOException e) {
            System.out.println("Ошибка подключения: " + e);
            return "";
        }
    }

    @Override
    protected void onPostExecute(String result){
        if(!result.isEmpty()){
            try {
                JSONObject root = new JSONObject(result);
                JSONObject res = root.getJSONObject("result");
                accessToken = res.getString("access_token");
                context.setStringState(accessToken, true);

            } catch (JSONException e) {
                context.setStringState(accessToken, false);
                throw new RuntimeException(e);
            }

        } else {
            context.setStringState(accessToken, false);
        }
    }
}
