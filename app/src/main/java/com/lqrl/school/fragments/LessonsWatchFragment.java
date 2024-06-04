package com.lqrl.school.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lqrl.school.R;
import com.lqrl.school.adapters.LessonAdapter;
import com.lqrl.school.dialogs.LessonCreateDialogFragment;
import com.lqrl.school.entities.Course;
import com.lqrl.school.entities.Lesson;
import com.lqrl.school.interfaces.ArraySetter;
import com.lqrl.school.interfaces.LessonCreator;
import com.lqrl.school.interfaces.LessonDeleter;
import com.lqrl.school.web_services.CreateLessonTask;
import com.lqrl.school.web_services.DeleteLessonTask;
import com.lqrl.school.web_services.RefreshLessonTask;

import java.util.ArrayList;

public class LessonsWatchFragment extends Fragment implements ArraySetter<Lesson>,
        LessonCreator,
        LessonDeleter {
    Context activity;
    String accessToken;
    LessonAdapter lessonAdapter;
    ArrayList<Lesson> lessons;
    Course course;

    public LessonsWatchFragment(Context context, String accessToken, Course course){
        this.activity = context; this.accessToken = accessToken; this.course = course;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.creator_lessons_fragment, parent, false);
        Button refreshLessons = view.findViewById(R.id.creator_lessons_list_refresh_button);
        Button createLessons = view.findViewById(R.id.creator_create_lesson_btn);
        RecyclerView recyclerView = view.findViewById(R.id.lessonsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        lessons = new ArrayList<>();
        lessonAdapter = new LessonAdapter(activity, lessons, this);
        recyclerView.setAdapter(lessonAdapter);
        refreshLessons.setOnClickListener(v -> {
            refreshList();
        });
        createLessons.setOnClickListener(v -> {
            new LessonCreateDialogFragment(this, activity).show(getChildFragmentManager(), "CREATE_LESSON");
        });
        return view;
    }

    public void refreshList() {
        new RefreshLessonTask(course, activity, accessToken, this).execute();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        refreshList();
    }

    @Override
    public void setArrayList(ArrayList<Lesson> src, boolean ok) {
        if(ok){
            lessons.clear();
            lessons.addAll(src);
            lessonAdapter.notifyDataSetChanged();
            Toast.makeText(activity, "Refreshed successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Network or server error", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void sendLessonEntity(Lesson lesson) {
        lesson.CourseId = course.getCourseId();
        new CreateLessonTask(activity, this, lesson, accessToken).execute();
    }

    @Override
    public void requestDeleteLesson(int lessonId) {
        new DeleteLessonTask(activity, this, accessToken, lessonId).execute();
        refreshList();
    }
}
