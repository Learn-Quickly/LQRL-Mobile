package com.lqrl.school;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lqrl.school.web_services.GetUserDataTask;

import java.util.ArrayList;

public class CoursesListFragment extends Fragment implements UsernameFieldSetter{
    Context context;
    String accessToken;
    View rootView;

    public CoursesListFragment(){

    }
    public CoursesListFragment(Context activity, String accessToken){
        context = activity;
        this.accessToken = accessToken;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.content_main, parent, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        ArrayList<CardItem> cardItems = new ArrayList<>();
        cardItems.add(new CardItem("Ведмедик Медик", 272, 0.0f));
        cardItems.add(new CardItem("Соціологія", 34, 0.0f));
        CourseAdapter cardAdapter = new CourseAdapter(cardItems);
        recyclerView.setAdapter(cardAdapter);
        rootView = view;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        new GetUserDataTask(this, accessToken).execute();
    }

    @Override
    public void setUsernameField(String username) {
        TextView helloUsername = rootView.findViewById(R.id.hello_username);
        helloUsername.setText("Hello, " + username + "!");
    }
}
