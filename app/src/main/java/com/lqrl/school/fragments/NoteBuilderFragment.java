package com.lqrl.school.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.lqrl.school.HomeActivity;
import com.lqrl.school.R;
import com.lqrl.school.note_builder.NoteBuilderView;

public class NoteBuilderFragment extends Fragment {
    Context activity;
    NoteBuilderView.Mode mode; // TODO actual need right here?

    public NoteBuilderFragment(Context context, NoteBuilderView.Mode mode){
        activity = context;
        this.mode = mode;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.note_builder_fragment, parent, false);
        NoteBuilderView noteBuilderView = root.findViewById(R.id.diagramView);
        noteBuilderView.drawFromJSON(getString(R.string.test_diagram1));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((HomeActivity)activity).clearToolbarMenu();
    }
}
