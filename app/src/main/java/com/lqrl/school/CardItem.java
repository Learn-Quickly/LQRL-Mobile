package com.lqrl.school;

public class CardItem {
    private final String courseName;
    private final int courseParticipantsCount;
    private final float coursePrice;

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
