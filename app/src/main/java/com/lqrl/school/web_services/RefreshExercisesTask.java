package com.lqrl.school.web_services;

import android.content.Context;
import android.os.AsyncTask;

import com.lqrl.school.entities.Exercise;
import com.lqrl.school.entities.Lesson;
import com.lqrl.school.fragments.ExercisesWatchFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RefreshExercisesTask extends AsyncTask<Void, Void, String> {
    Context activity;
    String accessToken;
    ExercisesWatchFragment exercisesWatchFragment;
    OkHttpClient client = new OkHttpClient();
    Lesson lesson;

    public RefreshExercisesTask(Lesson lesson, Context context, String accessToken, ExercisesWatchFragment exercisesWatchFragment){
        activity = context;
        this.accessToken = accessToken;
        this.exercisesWatchFragment = exercisesWatchFragment;
        this.lesson = lesson;
    }

    @Override
    protected String doInBackground(Void... voids){

        Request getLessonsByCourse = new Request.Builder()
                .url("http://109.86.250.207:8080/api/course/lesson/exercise/get_lesson_exercises/" + lesson.Id)
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
        ArrayList<Exercise> exercisesArrayList = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(result);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Exercise tmp = new Exercise(
                        jsonObject.getInt("exercise_id"),
                        jsonObject.getString("title"),
                        jsonObject.getString("description"),
                        jsonObject.getInt("lesson_id"),
                        jsonObject.getInt("time_to_complete"),
                        jsonObject.getString("difficult"),
                        jsonObject.getString("exercise_body"),
                        jsonObject.getString("answer_body"));
                exercisesArrayList.add(tmp);
            }
            exercisesWatchFragment.setArrayList(exercisesArrayList, true);
        } catch(JSONException e){
            exercisesWatchFragment.setArrayList(exercisesArrayList, false);
            throw new RuntimeException(e);
        }

    }
}
