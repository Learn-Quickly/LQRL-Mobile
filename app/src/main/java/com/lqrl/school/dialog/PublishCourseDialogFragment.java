package com.lqrl.school.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.lqrl.school.interfaces.CoursePublisher;

public class PublishCourseDialogFragment extends DialogFragment {
    Context activity;

    public PublishCourseDialogFragment(Context activity){
        this.activity = activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want to publish this course?")
                .setPositiveButton("Yes", (dialog, which) -> ((CoursePublisher) activity).approveDialogPublish(true))
                .setNegativeButton("Cancel", (dialog, which) -> ((CoursePublisher) activity).approveDialogPublish(false));
        return builder.create();
    }
}
