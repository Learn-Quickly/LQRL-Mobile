package com.lqrl.school.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.lqrl.school.entities.CourseCardItem;
import com.lqrl.school.interfaces.CoursePublisher;

public class PublishCourseDialogFragment extends DialogFragment {
    Context activity;
    CourseCardItem courseCardItem;

    public PublishCourseDialogFragment(Context activity, CourseCardItem courseCardItem){
        this.activity = activity;
        this.courseCardItem = courseCardItem;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want to publish this course?")
                .setPositiveButton("Yes", (dialog, which) -> ((CoursePublisher) activity).approveDialogPublish(true, courseCardItem))
                .setNegativeButton("Cancel", (dialog, which) -> ((CoursePublisher) activity).approveDialogPublish(false, courseCardItem));
        return builder.create();
    }
}
