package com.lqrl.school.fragments;

import android.content.Context;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.lqrl.school.R;
import com.lqrl.school.note_builder.Node;
import com.lqrl.school.note_builder.NoteBuilderView;

public class NoteBuilderFragment extends Fragment {
    Context activity;

    public NoteBuilderFragment(Context context){
        activity = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.note_builder_fragment, parent, false);
        NoteBuilderView noteBuilderView = root.findViewById(R.id.diagramView);
        noteBuilderView.drawNode(new Node("Psychology", getString(R.string.lorem), new RectF(100, 100, 700, 500)));
        return root;
    }
}
