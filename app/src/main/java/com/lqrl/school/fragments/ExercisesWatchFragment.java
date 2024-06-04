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
import com.lqrl.school.adapters.ExerciseAdapter;
import com.lqrl.school.entities.Exercise;
import com.lqrl.school.entities.Lesson;
import com.lqrl.school.interfaces.ArraySetter;
import com.lqrl.school.interfaces.ExerciseCreator;
import com.lqrl.school.dialogs.ExerciseCreateDialogFragment;
import com.lqrl.school.web_services.RefreshExercisesTask;

import java.util.ArrayList;

public class ExercisesWatchFragment extends Fragment implements ArraySetter<Exercise>,
        ExerciseCreator {
    Context activity;
    String accessToken;
    Lesson lesson;
    ArrayList<Exercise> exercises;
    ExerciseAdapter exercisesAdapter;
    public ExercisesWatchFragment(Context context, String accessToken, Lesson lesson){
        this.activity = context;
        this.accessToken = accessToken;
        this.lesson = lesson;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.creator_exercises_fragment, parent, false);
        Button refreshExercises = view.findViewById(R.id.creator_exercises_list_refresh_button);
        Button createExercises = view.findViewById(R.id.creator_create_exercise_btn);
        RecyclerView recyclerView = view.findViewById(R.id.exercisesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        exercises = new ArrayList<>();
        exercisesAdapter = new ExerciseAdapter(activity, exercises, this);
        recyclerView.setAdapter(exercisesAdapter);
        refreshExercises.setOnClickListener(v -> {
            refreshList();
        });
        createExercises.setOnClickListener(v -> {
            new ExerciseCreateDialogFragment(this, activity).show(getChildFragmentManager(), "CREATE_EXERCISE");
        });
        return view;
    }

    private void refreshList() {
        new RefreshExercisesTask(lesson, activity, accessToken, this).execute();
    }

    @Override
    public void setArrayList(ArrayList<Exercise> src, boolean ok) {
        if(ok){
            exercises.clear();
            exercises.addAll(src);
            exercisesAdapter.notifyDataSetChanged();
            Toast.makeText(activity, "Refreshed successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Network or server error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void sendExerciseEntity(Exercise exercise) {
        exercise.LessonId = lesson.Id;
        //new CreateLessonTask(activity, this, lesson, accessToken).execute();
        // TODO сделать CreateExerciseTask
        Toast.makeText(activity, "debug: Exercise created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){

    }
}
