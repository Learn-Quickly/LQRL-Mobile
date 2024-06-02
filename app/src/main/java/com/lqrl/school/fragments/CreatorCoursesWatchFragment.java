package com.lqrl.school.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lqrl.school.entities.CourseCardItem;
import com.lqrl.school.CourseAdapter;
import com.lqrl.school.R;
import com.lqrl.school.interfaces.ArraySetter;
import com.lqrl.school.interfaces.StringSetter;
import com.lqrl.school.web_services.GetCreatedCoursesTask;
import com.lqrl.school.web_services.GetUserDataTask;

import java.util.ArrayList;

public class CreatorCoursesWatchFragment extends Fragment implements StringSetter, ArraySetter<CourseCardItem> {
    Context context;
    String accessToken;
    View rootView;
    CourseAdapter courseAdapter;
    ArrayList<CourseCardItem> courseCardItems;

    public CreatorCoursesWatchFragment(){

    }
    public CreatorCoursesWatchFragment(Context activity, String accessToken){
        context = activity;
        this.accessToken = accessToken;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.content_main, parent, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        courseCardItems = new ArrayList<>();
        courseAdapter = new CourseAdapter(context, courseCardItems);
        recyclerView.setAdapter(courseAdapter);

        rootView = view;
        Button refreshButton = rootView.findViewById(R.id.creator_course_list_refresh_button);
        refreshButton.setOnClickListener(v -> {
            refreshCourses();
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        new GetUserDataTask(this, accessToken).execute();
    }

    @Override
    public void setStringState(String src, boolean state) {
        TextView helloUsername = rootView.findViewById(R.id.hello_username);
        helloUsername.setText("Hello, " + src + "!");
    }

    @Override
    public void setArrayList(ArrayList<CourseCardItem> src, boolean ok) {
        courseCardItems.clear();
        courseCardItems.addAll(src);
        courseAdapter.notifyDataSetChanged();
    }

    public void refreshCourses() {
        new GetCreatedCoursesTask(this, accessToken).execute();
    }
}
