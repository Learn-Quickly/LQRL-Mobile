package com.lqrl.school.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.lqrl.school.HomeActivity;
import com.lqrl.school.R;
import com.lqrl.school.entities.Exercise;
import com.lqrl.school.fragments.ExercisesWatchFragment;
import com.lqrl.school.interfaces.ExerciseCreator;

public class ExerciseCreateDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {
    ExercisesWatchFragment fragment;
    Context activity;
    int itemChosen = 0;

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
        Spinner spinnerDifficulty = view.findViewById(R.id.sp_exercise_difficulty);
        String[] items = new String[] {getString(R.string.difficulty_easy), getString(R.string.difficulty_medium), getString(R.string.difficulty_hard)};

        String choosedItem = null;
        ArrayAdapter<String> spItemsAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, items);
        spinnerDifficulty.setAdapter(spItemsAdapter);
        spinnerDifficulty.setOnItemSelectedListener(this);

        builder.setTitle(getString(R.string.create_task))
                .setView(view)
                .setPositiveButton("Ok", (dialog, which) -> {
                    String title = editTextTitle.getText().toString();
                    String description = editTextDescription.getText().toString();
                    int completionTime = Integer.parseInt(editTextCompletionTime.getText().toString());
                    if(title.isEmpty()){
                        Toast.makeText(activity, getString(R.string.field_title_is_empty), Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                    String difficult = null;
                    switch(itemChosen){
                        case 0: difficult = "Easy"; break;
                        case 1: difficult = "Medium"; break;
                        case 2: difficult = "Hard"; break;
                    }
                    Exercise ex = new Exercise(-1, title, description, -1, completionTime, difficult, null, null);
                    ((HomeActivity)activity).exerciseService.saveExerciseTempPayload(ex);
                })
                .setNegativeButton(R.string.cancel_dialog, (dialog, which) -> {

                });
        return builder.create();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        itemChosen = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
