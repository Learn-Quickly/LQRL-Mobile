package com.lqrl.school.web_services;

import android.content.Context;
import android.os.AsyncTask;

import com.lqrl.school.UsernameFieldSetter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetUserDataTask extends AsyncTask<Void, Void, String> {
    OkHttpClient client = new OkHttpClient();
    String accessToken;
    UsernameFieldSetter activity;
    public GetUserDataTask(UsernameFieldSetter usernameFieldSetter, String token){
        activity = usernameFieldSetter;
        accessToken = token;
    }

    @Override
    protected String doInBackground(Void... voids) {
        Request.Builder getUserData = new Request.Builder()
                .url("http://109.86.250.207:8080/api/user/get_user_data")
                .get();
        getUserData.addHeader("Authorization", "Bearer " + accessToken);
        Request getUserDataRequest = getUserData.build();
        try (Response response = client.newCall(getUserDataRequest).execute()) {
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
        if(result != null){
            try {
                JSONObject rootobj = new JSONObject(result);
                String username = rootobj.getString("username");
                activity.setUsernameField(username);
            } catch (JSONException e) {
                activity.setUsernameField("SERVER ERROR");
                throw new RuntimeException(e);
            }

        } else {
            activity.setUsernameField("SERVER ERROR");
        }
    }
}
