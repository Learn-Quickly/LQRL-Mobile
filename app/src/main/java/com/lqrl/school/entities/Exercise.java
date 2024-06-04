package com.lqrl.school.entities;

public class Exercise {
    public String Title;
    public String Description;
    public int LessonId;
    public int Id;
    public int CompletionTime;
    public String Difficulty;
    public String ExerciseBody;
    public String AnswerBody;

    public Exercise(int id, String title, String description, int lessonId, int completionTime, String difficulty, String exerciseBody, String answerBody){
        Id = id;
        Title = title;
        Description = description;
        LessonId = lessonId;
        CompletionTime = completionTime;
        Difficulty = difficulty;
        ExerciseBody = exerciseBody;
        AnswerBody = answerBody;
    }
}
