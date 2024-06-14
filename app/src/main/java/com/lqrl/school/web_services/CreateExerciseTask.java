package com.lqrl.school.web_services;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.lqrl.school.entities.Exercise;
import com.lqrl.school.entities.Lesson;
import com.lqrl.school.fragments.ExercisesWatchFragment;
import com.lqrl.school.fragments.LessonsWatchFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateExerciseTask extends AsyncTask<Void, Void, String> {

    OkHttpClient client = new OkHttpClient();
    Context activity;
    ExercisesWatchFragment fragment;
    Exercise exercise;
    String accessToken;

    public CreateExerciseTask(Context activity, ExercisesWatchFragment fragment, Exercise exercise, String accessToken){
        this.activity = activity;
        this.fragment = fragment;
        this.exercise = exercise;
        this.accessToken = accessToken;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String postBody = "";
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body;

        try {
            postBody = exercise.toJSON();
            body = RequestBody.create(postBody, JSON);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Request createCourse = new Request.Builder()
                .url("http://109.86.250.207:8080/api/course/lesson/exercise/create")
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
            Toast.makeText(activity, "Exercise created successfully", Toast.LENGTH_SHORT).show();
            try {
                JSONObject resp = new JSONObject(result);
                exercise.Id = resp.getInt("exercise_id");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            fragment.refreshList(exercise);
        } else {
            Toast.makeText(activity, "Failed to create exercise", Toast.LENGTH_SHORT).show();
        }
    }
}
