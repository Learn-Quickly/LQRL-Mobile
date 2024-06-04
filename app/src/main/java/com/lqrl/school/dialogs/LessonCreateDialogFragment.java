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
import com.lqrl.school.entities.Lesson;
import com.lqrl.school.fragments.LessonsWatchFragment;
import com.lqrl.school.interfaces.LessonCreator;

public class LessonCreateDialogFragment extends DialogFragment {
    LessonsWatchFragment fragment;
    Context activity;
    public LessonCreateDialogFragment(LessonsWatchFragment fragment, Context context){
        this.fragment = fragment;
        this.activity = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.create_lesson_dialog, null);
        EditText editTextTitle = view.findViewById(R.id.et_lesson_title);
        EditText editTextDescription = view.findViewById(R.id.et_lesson_desc);

        builder.setTitle("Create lesson")
                .setView(view)
                .setPositiveButton("Ok", (dialog, which) -> {
                    String title = editTextTitle.getText().toString();
                    String description = editTextDescription.getText().toString();
                    if(title.isEmpty()){
                        Toast.makeText(activity, "Field title is empty.", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                    ((LessonCreator) fragment).sendLessonEntity(new Lesson(-1, title, description, -1));
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    //((LessonCreator) fragment).sendLessonEntity(null);
                });
        return builder.create();
    }
}
