package com.lqrl.school.web_services;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.lqrl.school.BuildConfig;
import com.lqrl.school.R;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class RegisterTask extends AsyncTask<String, Void, String> {
    OkHttpClient client = new OkHttpClient();
    Context context;
    String username, password;
    public RegisterTask(Context activity, String username, String pwd){
        this.context = activity;
        this.username = username;
        this.password = pwd;
    }

    @Override
    protected String doInBackground(String... strings) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String jsonRequest = "{\n" +
                "  \"pwd\": \"" + password + "\",\n" +
                "  \"username\": \"" + username + "\"\n" +
                "}";

        RequestBody body = RequestBody.create(jsonRequest, JSON);
        Request.Builder requestBuilder = new Request.Builder().url(BuildConfig.SERVER_ROOT + "/api/register").post(body);
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
        if(result.toUpperCase().contains("true".toUpperCase())){
            Toast.makeText(context, R.string.registration_successful, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(context, R.string.error_username_is_occupied, Toast.LENGTH_SHORT).show();
        }
        //str1.toUpperCase().contains(str2.toUpperCase())
    }
}
