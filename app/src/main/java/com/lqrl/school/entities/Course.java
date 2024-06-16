package com.lqrl.school.entities;

import org.json.JSONException;
import org.json.JSONObject;

public class Course {
    private final String courseName;
    private final int courseParticipantsCount;
    private final float coursePrice;
    private final int color;
    private final String courseDescription;
    private final int courseId;
    private final String state;

    public Course(int courseId, String courseName, int courseParticipantsCount, float coursePrice, int color, String courseDescription, String state){
        this.courseName = courseName; this.courseParticipantsCount = courseParticipantsCount;
        this.coursePrice = coursePrice; this.color = color; this.courseDescription = courseDescription;
        this.courseId = courseId; this.state = state;
    }

    public String getCourseName(){
        return courseName;
    }

    public int getCourseParticipantsCount(){
        return courseParticipantsCount;
    }

    public float getCoursePrice(){
        return coursePrice;
    }

    public String getCourseDescription() { return courseDescription; }

    public int getCourseColor() { return this.color; }

    public int getCourseId() { return this.courseId; }

    public String getState() { return this.state; }

    public String toJSON(){
        JSONObject course = new JSONObject();

        try {
            course.put("title", courseName);
            course.put("description", courseDescription);
            course.put("price", coursePrice);
            course.put("color", String.valueOf(color));
            course.put("course_type", "general");
            return course.toString();

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
