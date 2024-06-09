package com.lqrl.school;

import android.content.Context;

import java.io.*;

public class FileHandler {
    // Method to create a text file and write content to it
    public static void createFileAndWriteContent(Context context, String fileName, String content) {
        try {
            File file = new File(context.getExternalFilesDir(null), fileName);
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
            writer.write(content);
            writer.close();
            fos.close();
            System.out.println("File created and content written successfully.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to read content from a text file
    public static String readFileContent(Context context, String fileName) {
        StringBuilder content = new StringBuilder();
        try {
            File file = new File(context.getExternalFilesDir(null), fileName);
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content.toString();
    }
}
