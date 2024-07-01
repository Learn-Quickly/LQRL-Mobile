package com.lqrl.school.web_services;

import android.content.Context;
import android.os.AsyncTask;

import com.lqrl.school.BuildConfig;
import com.lqrl.school.entities.Exercise;
import com.lqrl.school.entities.Lesson;
import com.lqrl.school.interfaces.ArraySetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RefreshExercisesByLessonTask extends AsyncTask<Void, Void, String> {
    Context activity;
    String accessToken;
    ArraySetter<Exercise> exercisesArraySetter;
    OkHttpClient client = new OkHttpClient();
    Lesson lesson;

    public RefreshExercisesByLessonTask(Lesson lesson, Context context, String accessToken, ArraySetter<Exercise> exercisesArraySetter){
        activity = context;
        this.accessToken = accessToken;
        this.exercisesArraySetter = exercisesArraySetter;
        this.lesson = lesson;
    }

    @Override
    protected String doInBackground(Void... voids){

        Request getLessonsByCourse = new Request.Builder()
                .url(BuildConfig.SERVER_ROOT + "/api/course/lesson/exercise/get_lesson_exercises/" + lesson.Id)
                .addHeader("Authorization", "Bearer " + accessToken)
                .get()
                .build();

        try (Response response = client.newCall(getLessonsByCourse).execute()) {
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
        if(result != null) {
            ArrayList<Exercise> exercisesArrayList = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    Exercise tmp = new Exercise(
                            jsonObject.getInt("exercise_id"),
                            jsonObject.getString("title"),
                            jsonObject.getString("description"),
                            jsonObject.getInt("lesson_id"),
                            jsonObject.getInt("time_to_complete"),
                            jsonObject.getString("difficult"),
                            jsonObject.getJSONObject("exercise_body"),
                            jsonObject.getJSONObject("answer_body"));
                    exercisesArrayList.add(tmp);
                }
                exercisesArraySetter.setArrayList(exercisesArrayList, true);
            } catch (JSONException e) {
                exercisesArraySetter.setArrayList(exercisesArrayList, false);
                throw new RuntimeException(e);
            }
        }
    }
}
