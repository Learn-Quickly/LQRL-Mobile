package com.lqrl.school.entities;

public class CourseCardItem {
    private final String courseName;
    private final int courseParticipantsCount;
    private final float coursePrice;
    private final int color;
    private final String courseDescription;
    private final int courseId;
    private final String state;

    public CourseCardItem(int courseId, String courseName, int courseParticipantsCount, float coursePrice, int color, String courseDescription, String state){
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
}
