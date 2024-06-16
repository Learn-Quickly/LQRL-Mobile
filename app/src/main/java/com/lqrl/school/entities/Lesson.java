package com.lqrl.school.entities;

import org.json.JSONException;
import org.json.JSONObject;

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

    public String toJSON(){
        JSONObject lesson = new JSONObject();
        try {
            lesson.put("course_id", CourseId);
            lesson.put("description", Description);
            lesson.put("title", Title);
            return lesson.toString();

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
