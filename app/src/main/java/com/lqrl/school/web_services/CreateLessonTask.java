package com.lqrl.school.web_services;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.lqrl.school.entities.Lesson;
import com.lqrl.school.fragments.LessonsWatchFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateLessonTask extends AsyncTask<Void, Void, String> {

    OkHttpClient client = new OkHttpClient();
    Context activity;
    LessonsWatchFragment fragment;
    Lesson lesson;
    String accessToken;

    public CreateLessonTask(Context activity, LessonsWatchFragment fragment, Lesson lesson, String accessToken){
        this.activity = activity;
        this.fragment = fragment;
        this.lesson = lesson;
        this.accessToken = accessToken;
    }

    @Override
    protected String doInBackground(Void... voids) {
        JSONObject course = new JSONObject();
        String postBody = "";
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body;

        try {
            course.put("course_id", lesson.CourseId);
            course.put("description", lesson.Description);
            course.put("title", lesson.Title);
            postBody = course.toString();
            body = RequestBody.create(postBody, JSON);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Request createCourse = new Request.Builder()
                .url("http://109.86.250.207:8080/api/course/lesson/create")
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
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result){
        if(result != null){
            Toast.makeText(activity, "Lesson created successfully", Toast.LENGTH_SHORT).show();
            fragment.refreshList();
        } else {
            Toast.makeText(activity, "Failed to create lesson", Toast.LENGTH_SHORT).show();
        }
    }
}
