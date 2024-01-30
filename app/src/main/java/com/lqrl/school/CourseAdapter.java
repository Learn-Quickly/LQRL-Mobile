package com.lqrl.school;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CardViewHolder> {

    private ArrayList<CardItem> cardItems;

    public CourseAdapter(ArrayList<CardItem> cards){
        this.cardItems = cards;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardItem currentItem = cardItems.get(position);

        holder.textViewParticipantsCount.setText(Integer.toString(currentItem.getCourseParticipantsCount()));
        holder.textViewPrice.setText(Float.toString(currentItem.getCoursePrice()));
        holder.textViewCourseTitle.setText(currentItem.getCourseName());
    }

    @Override
    public int getItemCount() {
        return cardItems.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewParticipantsCount;
        public TextView textViewPrice;
        public TextView textViewCourseTitle;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewParticipantsCount = itemView.findViewById(R.id.participants_count);
            textViewPrice = itemView.findViewById(R.id.course_price);
            textViewCourseTitle = itemView.findViewById(R.id.course_name_title);
        }
    }
}
