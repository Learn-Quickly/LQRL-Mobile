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
import com.lqrl.school.entities.CourseCardItem;
import com.lqrl.school.entities.Lesson;
import com.lqrl.school.interfaces.ArraySetter;
import com.lqrl.school.interfaces.LessonOpener;
import com.lqrl.school.interfaces.LessonsRefresher;
import com.lqrl.school.web_services.RefreshLessonTask;

import java.util.ArrayList;

public class LessonsWatchFragment extends Fragment implements ArraySetter<Lesson> {
    Context activity;
    String accessToken;
    LessonAdapter lessonAdapter;
    ArrayList<Lesson> lessons;
    CourseCardItem courseCardItem;

    public LessonsWatchFragment(Context context, String accessToken, CourseCardItem courseCardItem){
        this.activity = context; this.accessToken = accessToken; this.courseCardItem = courseCardItem;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.creator_lessons, parent, false);
        Button refreshLessons = view.findViewById(R.id.creator_lessons_list_refresh_button);
        RecyclerView recyclerView = view.findViewById(R.id.lessonsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        lessons = new ArrayList<>();
        lessonAdapter = new LessonAdapter(activity, lessons);
        recyclerView.setAdapter(lessonAdapter);
        refreshLessons.setOnClickListener(v -> {
            new RefreshLessonTask(courseCardItem, activity, accessToken, this).execute();
        });
        return view;
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
}
