package com.lqrl.school;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lqrl.school.entities.CourseCardItem;
import com.lqrl.school.interfaces.CoursePublisher;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CardViewHolder> {

    private final ArrayList<CourseCardItem> courseCardItems;
    private Context activity;

    public CourseAdapter(Context activity, ArrayList<CourseCardItem> cards){
        this.courseCardItems = cards;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CourseCardItem currentItem = courseCardItems.get(position);

        holder.textViewParticipantsCount.setText(String.valueOf(currentItem.getCourseParticipantsCount()));
        holder.textViewPrice.setText(String.valueOf(currentItem.getCoursePrice()));
        holder.textViewCourseTitle.setText(currentItem.getCourseName());
        holder.textViewCourseDescription.setText(currentItem.getCourseDescription());
        holder.learnButton.setOnClickListener(v -> {
            ((CoursePublisher) activity).requestPublishCourse(currentItem);
        });
        //holder.courseCardView.setCardBackgroundColor(currentItem.getCourseColor());
        // TODO CardView color does not change! Somehow use color property!
    }

    @Override
    public int getItemCount() {
        return courseCardItems.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewParticipantsCount;
        public TextView textViewPrice;
        public TextView textViewCourseTitle;
        public TextView textViewCourseDescription;
        public CardView courseCardView;
        public Button learnButton;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewParticipantsCount = itemView.findViewById(R.id.participants_count);
            textViewPrice = itemView.findViewById(R.id.course_price);
            textViewCourseTitle = itemView.findViewById(R.id.course_name_title);
            textViewCourseDescription = itemView.findViewById(R.id.course_desc);
            courseCardView = itemView.findViewById(R.id.course_card_view);
            learnButton = itemView.findViewById(R.id.learn_btn);
        }
    }
}
