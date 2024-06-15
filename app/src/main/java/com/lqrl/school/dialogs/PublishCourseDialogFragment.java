package com.lqrl.school.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.lqrl.school.R;
import com.lqrl.school.entities.Course;
import com.lqrl.school.interfaces.CoursePublisher;

public class PublishCourseDialogFragment extends DialogFragment {
    Context activity;
    Course course;

    public PublishCourseDialogFragment(Context activity, Course course){
        this.activity = activity;
        this.course = course;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.do_you_want_to_publish_this_course))
                .setPositiveButton(R.string.yes, (dialog, which) -> ((CoursePublisher) activity).approveDialogPublish(true, course))
                .setNegativeButton(R.string.cancel_dialog, (dialog, which) -> ((CoursePublisher) activity).approveDialogPublish(false, course));
        return builder.create();
    }
}
