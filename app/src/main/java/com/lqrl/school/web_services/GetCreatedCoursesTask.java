package com.lqrl.school.web_services;

import android.content.Context;
import android.os.AsyncTask;

import com.lqrl.school.entities.CourseCardItem;
import com.lqrl.school.interfaces.ArraySetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetCreatedCoursesTask extends AsyncTask<Void, Void, String> {
    ArraySetter<CourseCardItem> activity;
    String accessToken;
    OkHttpClient client = new OkHttpClient();
    public GetCreatedCoursesTask(ArraySetter<CourseCardItem> context, String accessToken){
        this.activity = context;
        this.accessToken = accessToken;
    }

    @Override
    protected String doInBackground(Void... voids) {
        Request getCreatedCourses = new Request.Builder()
                .url("http://109.86.250.207:8080/api/course/get_created_courses")
                .addHeader("Authorization", "Bearer " + accessToken)
                .get()
                .build();

        try (Response response = client.newCall(getCreatedCourses).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Запрос к серверу не был успешен: " +
                        response.code() + " " + response.message());
            }
            return response.body().string();

        } catch (IOException e) {
            System.out.println("Ошибка подключения: " + e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result){
        ArrayList<CourseCardItem> arrayListCourseCardItem = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(result);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                arrayListCourseCardItem.add(new CourseCardItem(jsonObject.getString("title")
                ,0, (float) jsonObject.getDouble("price"), jsonObject.getInt("color"), jsonObject.getString("description")));
                // TODO: web query to get participants count
                activity.setArrayList(arrayListCourseCardItem, true);
            }

        } catch (JSONException e) {
            activity.setArrayList(null, false);
            throw new RuntimeException(e);
        }
    }
}
