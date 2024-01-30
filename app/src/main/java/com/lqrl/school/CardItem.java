package com.lqrl.school;

public class CardItem {
    private String courseName;
    private int courseParticipantsCount;
    private float coursePrice;

    public CardItem(String courseName, int courseParticipantsCount, float coursePrice){
        this.courseName = courseName; this.courseParticipantsCount = courseParticipantsCount;
        this.coursePrice = coursePrice;
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
}
