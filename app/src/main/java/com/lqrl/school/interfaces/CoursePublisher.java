package com.lqrl.school.interfaces;

import com.lqrl.school.entities.Course;

public interface CoursePublisher {
    void requestPublishCourse(Course course);
    void approveDialogPublish(boolean approve, Course course);
    void sendPublishStatus(boolean status);
}
