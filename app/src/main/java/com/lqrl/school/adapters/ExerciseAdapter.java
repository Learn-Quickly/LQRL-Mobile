package com.lqrl.school.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lqrl.school.R;
import com.lqrl.school.entities.Exercise;
import com.lqrl.school.fragments.ExercisesWatchFragment;
import com.lqrl.school.interfaces.NoteBuilderDealer;
import com.lqrl.school.note_builder.NoteBuilderView;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private final ArrayList<Exercise> exerciseList;
    private Context activity;
    private ExercisesWatchFragment exerciseWatchFragment;

    public ExerciseAdapter(Context activity, ArrayList<Exercise> exercises, ExercisesWatchFragment exerciseWatchFragment){
        this.exerciseList = exercises;
        this.activity = activity;
        this.exerciseWatchFragment = exerciseWatchFragment;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise currentItem = exerciseList.get(position);

        holder.textViewTitle.setText(currentItem.Title);
        holder.textViewDescription.setText(currentItem.Description);
        holder.difficulty.setText(currentItem.Difficulty);
        holder.timeSeconds.setText(String.valueOf(currentItem.CompletionTime));

        holder.goNoteBuilder.setOnClickListener(v -> {
            ((NoteBuilderDealer)activity).goToBuilder(currentItem, NoteBuilderView.Mode.NoteConstructor);
        });
        holder.goAnswerBuilder.setOnClickListener(v -> {
            ((NoteBuilderDealer)activity).goToBuilder(currentItem, NoteBuilderView.Mode.AnswerConstructor);
        });
        holder.saveExercise.setOnClickListener(v ->{
            exerciseWatchFragment.saveExercise(currentItem);
        });
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public TextView textViewDescription;
        public Button goNoteBuilder;
        public Button goAnswerBuilder;
        public Button saveExercise;
        public TextView timeSeconds;
        public TextView difficulty;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.exercise_title);
            textViewDescription = itemView.findViewById(R.id.exercise_desc);
            goNoteBuilder = itemView.findViewById(R.id.exercise_task_btn);
            goAnswerBuilder = itemView.findViewById(R.id.exercise_answer_btn);
            timeSeconds = itemView.findViewById(R.id.tv_time_set);
            difficulty = itemView.findViewById(R.id.tv_difficulty_set);
            saveExercise = itemView.findViewById(R.id.exercise_save_btn);
        }
    }
}
