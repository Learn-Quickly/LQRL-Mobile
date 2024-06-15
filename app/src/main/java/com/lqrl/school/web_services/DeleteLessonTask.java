package com.lqrl.school.web_services;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.lqrl.school.R;
import com.lqrl.school.fragments.LessonsWatchFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeleteLessonTask extends AsyncTask<Void, Void, String> {

    private LessonsWatchFragment lessonsWatchFragment;
    private String accessToken;
    private int lessonId;
    OkHttpClient client = new OkHttpClient();
    private Context activity;
    public DeleteLessonTask(Context context, LessonsWatchFragment lessonsWatchFragment, String accessToken, int lessonId){
        this.lessonsWatchFragment = lessonsWatchFragment;
        this.accessToken = accessToken;
        this.lessonId = lessonId;
        activity = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        JSONObject course = new JSONObject();
        String postBody = "";
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body;

        try {
            course.put("lesson_id", lessonId);
            postBody = course.toString();
            body = RequestBody.create(postBody, JSON);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Request createCourse = new Request.Builder()
                .url("http://109.86.250.207:8080/api/course/lesson/delete")
                .delete(body)
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
            Toast.makeText(activity, R.string.lesson_deleted, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, R.string.failed_to_delete_lesson, Toast.LENGTH_SHORT).show();
        }
    }
}
