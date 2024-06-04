package com.lqrl.school.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.lqrl.school.R;
import com.flask.colorpicker.builder.*;
import com.lqrl.school.web_services.CreateCourseDraftTask;

public class CreateCourseFragment extends Fragment {
    public CreateCourseFragment(){}
    public CreateCourseFragment(Context context, String accessToken){
        activity = context;
        this.accessToken = accessToken;
    }
    View view;
    Button createCourse, pickColor;
    EditText editTextTitle, editTextDesc, editTextPrice;
    Integer defaultColor = (int) Color.pack(55,55,55);
    Integer pickedColor;
    Context activity;
    View colorDemo;
    String title, desc, price, accessToken;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View newView = inflater.inflate(R.layout.create_course_fragment, parent, false);
        createCourse = newView.findViewById(R.id.create_course_btn);
        colorDemo = newView.findViewById(R.id.create_course_color_demo);
        pickColor = newView.findViewById(R.id.create_course_pick_color_btn);
        pickColor.setOnClickListener(v -> {
            ColorPickerDialogBuilder
                    .with(activity)
                    .setTitle("Choose color")
                    .initialColor((int) Color.pack(252,3,3))
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setOnColorSelectedListener(new OnColorSelectedListener() {
                        @Override
                        public void onColorSelected(int selectedColor) {
                            //toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                        }
                    })
                    .setPositiveButton("Ok", new ColorPickerClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                            //toast("onClick: 0x" + Integer.toHexString(selectedColor));
                            colorDemo.setBackgroundColor(selectedColor);
                            pickedColor = selectedColor;
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            colorDemo.setBackgroundColor(defaultColor);
                            pickedColor = defaultColor;
                        }
                    })
                    .build()
                    .show();
        });
        editTextTitle = newView.findViewById(R.id.edit_create_course_title);
        editTextDesc = newView.findViewById(R.id.edit_create_course_desc);
        editTextPrice = newView.findViewById(R.id.edit_create_course_price);

        createCourse.setOnClickListener(v -> {
            boolean ok = checkFields();
            if(ok){
                float fPrice = Float.parseFloat(price);
                CreateCourseDraftTask createCourseDraftTask = new CreateCourseDraftTask(activity, accessToken, title, desc, fPrice, pickedColor);
                createCourseDraftTask.execute();
            } else {
                toast("Empty text fields!");
            }
        });

        return newView;
    }

    private boolean checkFields() {
        title = editTextTitle.getText().toString();
        desc = editTextDesc.getText().toString();
        price = editTextPrice.getText().toString();
        return !title.isEmpty() && !desc.isEmpty() && !price.isEmpty();
    }

    private void toast(String s) {
        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){

    }
}
