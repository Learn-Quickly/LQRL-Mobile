package com.lqrl.school.service;

import android.content.Context;

import com.lqrl.school.TokenManager;
import com.lqrl.school.adapters.ExerciseAdapter;
import com.lqrl.school.entities.Exercise;
import com.lqrl.school.entities.Lesson;
import com.lqrl.school.interfaces.ArraySetter;
import com.lqrl.school.web_services.CreateExerciseTask;
import com.lqrl.school.web_services.RefreshExercisesByLessonTask;
import com.lqrl.school.web_services.UpdateExerciseTask;

import java.util.ArrayList;

public class ExerciseService implements ArraySetter<Exercise> {
    private Context activity;
    private ArrayList<Exercise> exercisesToCreate;
    private ArrayList<Exercise> exercisesEditedNote;
    public ArrayList<Exercise> exercises;
    private ExerciseAdapter exerciseAdapter = null;
    private Lesson lesson;

    public void setCurrentLesson(Lesson lesson){
        this.lesson = lesson;
    }

    public ExerciseService(Context context){
        exercisesToCreate = new ArrayList<>();
        exercisesEditedNote = new ArrayList<>();
        exercises = new ArrayList<>();
        activity = context;
    }

    public void setExerciseAdapter(ExerciseAdapter exerciseAdapter) {
        this.exerciseAdapter = exerciseAdapter;
    }

    public void refreshExercisesList() {
        new RefreshExercisesByLessonTask(lesson, activity, TokenManager.getToken(activity), this).execute();
    }

    @Override
    public void setArrayList(ArrayList<Exercise> src, boolean ok) {
        if(ok) {
            exercises.clear();
            exercises.addAll(exercisesToCreate);
            exercises.addAll(exercisesEditedNote);
            //exercises.addAll(src);
            boolean toAdd = false;
            for(int i = 0; i < src.size(); i++){
                toAdd = true;
                for(int j = 0; j < exercisesEditedNote.size(); j++){
                    if(exercisesEditedNote.get(j).Id == src.get(i).Id){
                        toAdd = false; break;
                    }
                }
                if(toAdd) exercises.add(src.get(i));
            }
            if(exerciseAdapter != null) {
                exerciseAdapter.notifyDataSetChanged();
                setExerciseAdapter(null);
            }
        }
    }

    public void saveExerciseTempPayload(Exercise exercise){
        exercisesToCreate.add(exercise);
        exercises.add(exercise);
    }

    public void createOrUpdateExercise(Exercise exercise){
        if(exercisesToCreate.contains(exercise)) {
            exercise.LessonId = lesson.Id;
            new CreateExerciseTask(activity, exercise).execute();
        }
        else if(exercisesEditedNote.contains(exercise))
            new UpdateExerciseTask(activity, exercise).execute();
    }

    public void notifyExerciseCreated(Exercise exercise){
        exercisesToCreate.remove(exercise);
    }

    public void notifyExerciseUpdated(Exercise exercise){
        exercisesEditedNote.remove(exercise);
    }

    public void notifyExerciseEditedNote(Exercise exercise){
        if(!exercisesEditedNote.contains(exercise)){
            exercisesEditedNote.add(exercise);
        }
    }

}
