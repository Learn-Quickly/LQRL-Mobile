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
import com.lqrl.school.interfaces.NodeCreator;
import com.lqrl.school.note_builder.Node;
import com.lqrl.school.note_builder.NoteBuilderView;

public class NoteBuilderFragment extends Fragment implements NodeCreator {
    Context activity;
    NoteBuilderView.Mode mode; // TODO actual need right here?
    NoteBuilderView noteBuilderView;

    public NoteBuilderFragment(Context context, NoteBuilderView.Mode mode){
        activity = context;
        this.mode = mode;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.note_builder_fragment, parent, false);
        noteBuilderView = root.findViewById(R.id.diagramView);
        noteBuilderView.drawFromOptPrefs(activity);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((HomeActivity)activity).clearToolbarMenu();
    }

    @Override
    public void addNode(Node node) {
        noteBuilderView.createNodeFromPayload(node);
    }

    public void toggleConnectionSetMode() {
        noteBuilderView.toggleConnectionMode();
    }

    public void toggleDeleteNodeMode() {
        noteBuilderView.toggleDeleteNodeMode();
    }

    public void saveJSONDiagramToPrefs(Context activity) {
        noteBuilderView.saveJSONDiagramToPrefs(activity);
    }
}
