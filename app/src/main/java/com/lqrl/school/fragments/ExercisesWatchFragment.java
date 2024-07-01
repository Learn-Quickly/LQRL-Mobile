package com.lqrl.school.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lqrl.school.HomeActivity;
import com.lqrl.school.R;
import com.lqrl.school.adapters.ExerciseAdapter;
import com.lqrl.school.entities.Exercise;
import com.lqrl.school.entities.Lesson;
import com.lqrl.school.dialogs.ExerciseCreateDialogFragment;

import java.util.ArrayList;

public class ExercisesWatchFragment extends Fragment {
    Context activity;
    String accessToken;
    Lesson lesson;
    ArrayList<Exercise> exercises;
    ExerciseAdapter exercisesAdapter;
    public static String TAG = "ExercisesWatchFragment";

    public ExercisesWatchFragment(Context context, String accessToken, Lesson lesson){
        this.activity = context;
        this.accessToken = accessToken;
        this.lesson = lesson;
        ((HomeActivity)activity).exerciseService.setCurrentLesson(lesson);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.creator_exercises_fragment, parent, false);
        Button refreshExercises = view.findViewById(R.id.creator_exercises_list_refresh_button);
        Button createExercises = view.findViewById(R.id.creator_create_exercise_btn);
        RecyclerView recyclerView = view.findViewById(R.id.exercisesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        exercises = ((HomeActivity)activity).exerciseService.exercises;

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

    public void refreshList() {
        ((HomeActivity)activity).exerciseService.setExerciseAdapter(exercisesAdapter);
        ((HomeActivity)activity).exerciseService.refreshExercisesList();
        //((HomeActivity)activity).exerciseService.setExerciseAdapter(null);
    }

//    @Override
//    public void setArrayList(ArrayList<Exercise> src, boolean ok) {
//        if(ok){
//            exercises.clear();
//            exercises.addAll(src);
//            exercises.addAll(((HomeActivity)activity).exercisesCache);
//            exercisesAdapter.notifyDataSetChanged();
//            //Toast.makeText(activity, R.string.refreshed_successfully, Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(activity, R.string.network_or_server_error, Toast.LENGTH_SHORT).show();
//        }
//    }

//    @Override
//    public void sendExerciseEntity(Exercise exercise) {
//        exercise.LessonId = lesson.Id;
//        exercises.add(exercise);
//        ((HomeActivity)activity).exerciseService.addToCache(exercise);
//        exercisesAdapter.notifyDataSetChanged();
//    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        refreshList();
    }

    @Override
    public void onPause() {
        super.onPause();
        //((HomeActivity) activity).exerciseService.clearCache();
    }

    //    public void saveExercise(Exercise currentItem) {
//        Log.e(TAG, "saveExercise: exercise body:" + currentItem.ExerciseBody);
//        Log.e(TAG, "saveExercise: exercise answer:" + currentItem.ExerciseBody);
//        new CreateExerciseTask(activity, this, currentItem, accessToken).execute();
//    }
}
