package com.lqrl.school.web_services;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TestTask extends AsyncTask<String, Void, String>{
    OkHttpClient client = new OkHttpClient();
    public static String tag = "TEST_TASK_TAG";
    @Override
    protected String doInBackground(String... strings) {
        Request request = new Request.Builder()
                .url("http://109.86.250.207:8080/swagger-ui/")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Запрос к серверу не был успешен: " +
                        response.code() + " " + response.message());
            }
            // вывод тела ответа
            return response.body().string();

        } catch (IOException e) {
            System.out.println("Ошибка подключения: " + e);
            return "";
        }
    }

    @Override
    protected void onPostExecute(String result){
        Log.d(tag, result);
    }
}
