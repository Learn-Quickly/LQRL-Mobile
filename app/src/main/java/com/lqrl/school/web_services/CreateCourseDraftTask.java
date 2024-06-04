package com.lqrl.school.web_services;

import android.content.Context;
import android.os.AsyncTask;

import com.lqrl.school.interfaces.StringSetter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateCourseDraftTask extends AsyncTask<Void, Void, String> {
    OkHttpClient client = new OkHttpClient();
    String accessToken, title, desc;
    float price;
    Context activity;
    int color;
    public CreateCourseDraftTask(Context stringSetter, String token, String title, String desc, float price, int color){
        activity = stringSetter;
        accessToken = token;
        this.title = title;
        this.desc = desc;
        this.price = price;
        this.color = color;
    }

    @Override
    protected String doInBackground(Void... voids) {
        JSONObject course = new JSONObject();
        String postBody = "";
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body;

        try {
            course.put("title", title);
            course.put("description", desc);
            course.put("price", price);
            course.put("color", String.valueOf(color));
            course.put("course_type", "general");
            postBody = course.toString();
            body = RequestBody.create(postBody, JSON);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Request createCourse = new Request.Builder()
                .url("http://109.86.250.207:8080/api/course/create_course_draft")
                .post(body)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = client.newCall(createCourse).execute()) {
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
        ((StringSetter)activity).setStringState(result, !result.isEmpty());
    }
}
