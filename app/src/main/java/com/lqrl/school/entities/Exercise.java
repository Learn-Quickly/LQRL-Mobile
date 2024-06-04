package com.lqrl.school.entities;

public class Exercise {
    public String Title;
    public String Description;
    public int LessonId;
    public int Id;
    public int CompletionTime;
    public String Difficulty;

    public Exercise(int id, String title, String description, int lessonId, int completionTime, String difficulty){
        Id = id;
        Title = title;
        Description = description;
        LessonId = lessonId;
        CompletionTime = completionTime;
        Difficulty = difficulty;
    }
}
