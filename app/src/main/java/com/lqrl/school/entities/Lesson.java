package com.lqrl.school.entities;

public class Lesson {
    public String Title;
    public String Description;
    public int CourseId;

    public Lesson(String title, String description, int courseId){
        Title = title;
        Description = description;
        CourseId = courseId;
    }
}
