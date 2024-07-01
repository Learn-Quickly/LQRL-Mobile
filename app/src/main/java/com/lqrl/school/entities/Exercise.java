package com.lqrl.school.entities;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class Exercise {
    public String Title;
    public String Description;
    public int LessonId;
    public int Id;
    public int CompletionTime;
    public String Difficulty;
    public JSONObject ExerciseBody;
    public JSONObject AnswerBody;

    public Exercise(int id, String title, String description, int lessonId, int completionTime, String difficulty, JSONObject exerciseBody, JSONObject answerBody){
        Id = id;
        Title = title;
        Description = description;
        LessonId = lessonId;
        CompletionTime = completionTime;
        Difficulty = difficulty;
        ExerciseBody = exerciseBody;
        AnswerBody = answerBody;
    }

    public String toCreateExercisePayloadJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("title", Title);
        json.put("description", Description);
        json.put("difficult", Difficulty);
        json.put("time_to_complete", CompletionTime);
        json.put("lesson_id", LessonId);
        json.put("exercise_type", "Conspect");
        json.put("exercise_body", ExerciseBody);
        json.put("answer_body", AnswerBody);
        return json.toString();
    }

    public String toLessonUpdatePayloadJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("title", Title);
        json.put("description", Description);
        json.put("lesson_id", LessonId);
        json.put("exercise_id", Id);

        return json.toString();
    }
}
