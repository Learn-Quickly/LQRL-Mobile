package com.lqrl.school.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lqrl.school.HomeActivity;
import com.lqrl.school.R;
import com.lqrl.school.entities.Lesson;
import com.lqrl.school.fragments.LessonsWatchFragment;
import com.lqrl.school.interfaces.LessonDeleter;
import com.lqrl.school.interfaces.LessonOpener;

import java.util.ArrayList;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    private final ArrayList<Lesson> lessonsList;
    private Context activity;
    private LessonsWatchFragment lessonsWatchFragment;

    public LessonAdapter(Context activity, ArrayList<Lesson> lessons, LessonsWatchFragment lessonsWatchFragment){
        this.lessonsList = lessons;
        this.activity = activity;
        this.lessonsWatchFragment = lessonsWatchFragment;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_item, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lesson currentItem = lessonsList.get(position);

        holder.textViewTitle.setText(currentItem.Title);
        holder.textViewDescription.setText(currentItem.Description);
        holder.openButton.setOnClickListener(v -> {
           // ((HomeActivity)activity).exerciseService.setCurrentLesson(currentItem);
            ((LessonOpener)activity).requestOpenLesson(currentItem);
        });
        holder.deleteButton.setOnClickListener(v -> {
            ((LessonDeleter)lessonsWatchFragment).requestDeleteLesson(currentItem.Id);
        });
    }

    @Override
    public int getItemCount() {
        return lessonsList.size();
    }

    public static class LessonViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public TextView textViewDescription;
        public CardView lessonCardView;
        public Button openButton;
        public Button deleteButton;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.lesson_title);
            textViewDescription = itemView.findViewById(R.id.lesson_desc);
            lessonCardView = itemView.findViewById(R.id.lesson_card_view);
            openButton = itemView.findViewById(R.id.lesson_open_btn);
            deleteButton = itemView.findViewById(R.id.lesson_delete_btn);
        }
    }
}
