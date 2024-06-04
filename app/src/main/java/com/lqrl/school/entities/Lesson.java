package com.lqrl.school.entities;

public class Lesson {
    public String Title;
    public String Description;
    public int CourseId;
    public int Id;

    public Lesson(int id, String title, String description, int courseId){
        Id = id;
        Title = title;
        Description = description;
        CourseId = courseId;
    }
}
