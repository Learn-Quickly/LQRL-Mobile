package com.lqrl.school.web_services;

import android.os.AsyncTask;

import com.lqrl.school.BuildConfig;
import com.lqrl.school.entities.Course;
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
    ArraySetter<Course> activity;
    String accessToken;
    OkHttpClient client = new OkHttpClient();
    public GetCreatedCoursesTask(ArraySetter<Course> context, String accessToken){
        this.activity = context;
        this.accessToken = accessToken;
    }

    @Override
    protected String doInBackground(Void... voids) {
        Request getCreatedCourses = new Request.Builder()
                .url(BuildConfig.SERVER_ROOT + "/api/course/get_created_courses")
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
        ArrayList<Course> arrayListCourse = new ArrayList<>();
        try {
            JSONObject jsonRoot = new JSONObject(result);
            JSONArray jsonArray = jsonRoot.getJSONArray("courses");
            int count = jsonRoot.getInt("count");
            for(int i = 0; i < count; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                arrayListCourse.add(new Course(jsonObject.getInt("id"), jsonObject.getString("title")
                ,0, (float) jsonObject.getDouble("price"), jsonObject.getInt("color"), jsonObject.getString("description"),
                        jsonObject.getString("state")));
                // TODO: web query to get participants count
                activity.setArrayList(arrayListCourse, true);
            }

        } catch (JSONException e) {
            activity.setArrayList(arrayListCourse, false);
            throw new RuntimeException(e);
        }
    }
}
