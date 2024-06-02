package com.lqrl.school.interfaces;

import com.lqrl.school.entities.CourseCardItem;

public interface CoursePublisher {
    void requestPublishCourse(CourseCardItem courseCardItem);
    void approveDialogPublish(boolean approve, CourseCardItem courseCardItem);
    void sendPublishStatus(boolean status);
}
