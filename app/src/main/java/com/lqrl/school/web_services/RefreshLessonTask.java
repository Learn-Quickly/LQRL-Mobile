package com.lqrl.school.web_services;

import android.content.Context;
import android.os.AsyncTask;

import com.lqrl.school.entities.CourseCardItem;
import com.lqrl.school.entities.Lesson;
import com.lqrl.school.fragments.LessonsWatchFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RefreshLessonTask extends AsyncTask<Void, Void, String> {
    Context activity;
    String accessToken;
    LessonsWatchFragment lessonsWatchFragment;
    OkHttpClient client = new OkHttpClient();
    CourseCardItem course;

    public RefreshLessonTask(CourseCardItem course, Context context, String accessToken, LessonsWatchFragment lessonsWatchFragment){
        activity = context;
        this.accessToken = accessToken;
        this.lessonsWatchFragment = lessonsWatchFragment;
        this.course = course;
    }

    @Override
    protected String doInBackground(Void... voids){

        Request getLessonsByCourse = new Request.Builder()
                .url("http://109.86.250.207:8080/api/course/lesson/get_lessons/" + course.getCourseId())
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
        ArrayList<Lesson> lessonArrayList = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(result);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Lesson tmp = new Lesson(jsonObject.getString("title"), jsonObject.getString("description"), jsonObject.getInt("course_id"));
                lessonArrayList.add(tmp);
            }
            lessonsWatchFragment.setArrayList(lessonArrayList, true);
        } catch(JSONException e){
            lessonsWatchFragment.setArrayList(lessonArrayList, false);
            throw new RuntimeException(e);
        }

    }
}
