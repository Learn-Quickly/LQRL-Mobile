package com.lqrl.school.web_services;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.lqrl.school.BuildConfig;
import com.lqrl.school.entities.Course;
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
    String accessToken;
    Context activity;
    Course course;
    public CreateCourseDraftTask(Context stringSetter, String token, Course course){
        activity = stringSetter;
        accessToken = token;
        this.course = course;
    }

    @Override
    protected String doInBackground(Void... voids) {
        return createCourseDraft(course, accessToken);
    }

    @NonNull
    public static String createCourseDraft(Course course, String accessToken) {
        OkHttpClient client = new OkHttpClient();
        JSONObject courseJSON = new JSONObject();
        String postBody = "";
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body;

        postBody = course.toJSON();
        body = RequestBody.create(postBody, JSON);

        Request createCourse = new Request.Builder()
                .url(BuildConfig.SERVER_ROOT + "/api/course/create_course_draft")
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
