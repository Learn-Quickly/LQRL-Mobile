package com.lqrl.school.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.lqrl.school.R;
import com.lqrl.school.UserMode;
import com.lqrl.school.entities.Course;
import com.lqrl.school.interfaces.CourseEnroller;
import com.lqrl.school.interfaces.CoursePublisher;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CardViewHolder> {

    private final ArrayList<Course> courses;
    private Context activity;

    private UserMode userMode;

    public CourseAdapter(Context activity, ArrayList<Course> cards, UserMode userMode) {
        this.courses = cards;
        this.activity = activity;
        this.userMode = userMode;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Course currentItem = courses.get(position);

        holder.textViewParticipantsCount.setText(String.valueOf(currentItem.getCourseParticipantsCount()));
        holder.textViewPrice.setText(String.valueOf(currentItem.getCoursePrice()));
        holder.textViewCourseTitle.setText(currentItem.getCourseName());
        holder.textViewCourseDescription.setText(currentItem.getCourseDescription());
//        if(userMode == UserMode.SearchCourse){
//            holder.learnButton.setText(R.string.enroll_course);
//        }
//        if(userMode == UserMode.SearchCourse){
//            holder.learnButton.setOnClickListener(v -> {
//                ((CourseEnroller)activity).tryEnrollCourse(currentItem);
//            });
//        } else {
        if(userMode == UserMode.Create) {
            holder.learnButton.setText(R.string.enroll_course_adapter);
            holder.learnButton.setOnClickListener(v -> {
                ((CoursePublisher) activity).requestPublishCourse(currentItem);
            });
        }
        //}

        holder.constraintLayout.setBackgroundColor(currentItem.getCourseColor());
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewParticipantsCount;
        public TextView textViewPrice;
        public TextView textViewCourseTitle;
        public TextView textViewCourseDescription;
        public CardView courseCardView;
        public Button learnButton;
        public ConstraintLayout constraintLayout;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewParticipantsCount = itemView.findViewById(R.id.participants_count);
            textViewPrice = itemView.findViewById(R.id.course_price);
            textViewCourseTitle = itemView.findViewById(R.id.course_name_title);
            textViewCourseDescription = itemView.findViewById(R.id.course_desc);
            courseCardView = itemView.findViewById(R.id.course_card_view);
            learnButton = itemView.findViewById(R.id.learn_btn);
            constraintLayout = itemView.findViewById(R.id.constraint_layout_course);
        }
    }
}
