package com.lqrl.school.interfaces;

import com.lqrl.school.entities.Lesson;

import java.util.ArrayList;

public interface LessonsRefresher {
    void refreshLessonsList(ArrayList<Lesson> src, boolean state);
}
