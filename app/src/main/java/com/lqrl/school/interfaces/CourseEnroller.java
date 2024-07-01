package com.lqrl.school.interfaces;

import com.lqrl.school.entities.Course;

public interface CourseEnroller {
    void tryEnrollCourse(Course course);
    void sendSubscribeStatus(boolean status);
}
