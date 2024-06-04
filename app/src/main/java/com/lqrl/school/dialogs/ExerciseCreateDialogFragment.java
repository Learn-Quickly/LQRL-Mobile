package com.lqrl.school.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.lqrl.school.R;
import com.lqrl.school.entities.Exercise;
import com.lqrl.school.entities.Lesson;
import com.lqrl.school.fragments.ExercisesWatchFragment;
import com.lqrl.school.fragments.LessonsWatchFragment;
import com.lqrl.school.interfaces.ExerciseCreator;
import com.lqrl.school.interfaces.LessonCreator;

public class ExerciseCreateDialogFragment extends DialogFragment {
    ExercisesWatchFragment fragment;
    Context activity;
    public ExerciseCreateDialogFragment(ExercisesWatchFragment fragment, Context context){
        this.fragment = fragment;
        this.activity = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.create_exercise_dialog, null);
        EditText editTextTitle = view.findViewById(R.id.et_exercise_title);
        EditText editTextDescription = view.findViewById(R.id.et_exercise_desc);
        EditText editTextCompletionTime = view.findViewById(R.id.et_exercise_time);
        EditText editTextDifficulty = view.findViewById(R.id.et_exercise_difficulty);

        builder.setTitle("Create exercise")
                .setView(view)
                .setPositiveButton("Ok", (dialog, which) -> {
                    String title = editTextTitle.getText().toString();
                    String description = editTextDescription.getText().toString();
                    int completionTime = Integer.parseInt(editTextCompletionTime.getText().toString());
                    String difficulty = editTextDifficulty.getText().toString();

                    if(title.isEmpty()){
                        Toast.makeText(activity, "Field title is empty.", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                    ((ExerciseCreator) fragment).sendExerciseEntity(new Exercise(-1, title, description, -1, completionTime, difficulty));
                })
                .setNegativeButton("Cancel", (dialog, which) -> {

                });
        return builder.create();
    }
}
