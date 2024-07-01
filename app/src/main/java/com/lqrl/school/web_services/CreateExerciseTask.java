package com.lqrl.school.web_services;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.lqrl.school.BuildConfig;
import com.lqrl.school.HomeActivity;
import com.lqrl.school.R;
import com.lqrl.school.TokenManager;
import com.lqrl.school.entities.Exercise;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateExerciseTask extends AsyncTask<Void, Void, String> {

    Context activity;
    Exercise exercise;
    String accessToken;

    public CreateExerciseTask(Context activity, Exercise exercise){
        this.activity = activity;
        this.exercise = exercise;
        this.accessToken = TokenManager.getToken(activity);
    }

    @Override
    protected String doInBackground(Void... voids) {
        return createExercise(exercise, accessToken);
    }

    @Nullable
    public static String createExercise(Exercise exercise, String accessToken) {
        OkHttpClient client = new OkHttpClient();
        String postBody = "";
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body;

        try {
            postBody = exercise.toCreateExercisePayloadJSON();
            body = RequestBody.create(postBody, JSON);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Request createCourse = new Request.Builder()
                .url(BuildConfig.SERVER_ROOT + "/api/course/lesson/exercise/create")
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
            Toast.makeText(activity, R.string.exercise_created_successfully, Toast.LENGTH_SHORT).show();
            try {
                JSONObject resp = new JSONObject(result);
                exercise.Id = resp.getInt("exercise_id");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            //fragment.refreshList(exercise);
            ((HomeActivity)activity).exerciseService.notifyExerciseCreated(exercise);
        } else {
            Toast.makeText(activity, R.string.failed_to_create_exercise, Toast.LENGTH_SHORT).show();
        }
    }
}
