package com.lqrl.school;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVPasswordReader {

    private Context context;

    public CSVPasswordReader(Context context) {
        this.context = context;
    }

    public List<String> readCSVFile(String fileName) {
        List<String> passwords = new ArrayList<>();
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        BufferedReader reader = null;

        try {
            inputStream = assetManager.open(fileName);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                // Предполагаем, что каждая строка в CSV содержит один пароль
                passwords.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return passwords;
    }
}
