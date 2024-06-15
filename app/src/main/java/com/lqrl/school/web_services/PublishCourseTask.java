package com.lqrl.school.web_services;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.lqrl.school.R;
import com.lqrl.school.entities.Course;
import com.lqrl.school.interfaces.CoursePublisher;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PublishCourseTask extends AsyncTask<Void,Void,String> {

    Context activity;
    Course course;
    OkHttpClient client = new OkHttpClient();
    String accessToken;

    public PublishCourseTask(Context context, Course course, String accessToken){
        this.activity = context; this.course = course;
        this.accessToken = accessToken;
    }

    @Override
    protected String doInBackground(Void... voids) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body;
        String putBody;
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("course_id", course.getCourseId());
            putBody = jsonObject.toString();
            body = RequestBody.create(putBody, JSON);
        } catch(JSONException e){
            throw new RuntimeException(e);
        }
        Request publishCourse = new Request.Builder()
                .url("http://109.86.250.207:8080/api/course/publish_course")
                .addHeader("Authorization", "Bearer " + accessToken)
                .put(body)
                .build();

        try (Response response = client.newCall(publishCourse).execute()) {
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
            Toast.makeText(activity, R.string.course_published, Toast.LENGTH_SHORT).show();
            ((CoursePublisher)activity).sendPublishStatus(true);
        } else {
            Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show();
            ((CoursePublisher)activity).sendPublishStatus(false);
        }
    }
}
