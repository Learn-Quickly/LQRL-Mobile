package com.lqrl.school.fragments;

import android.content.Context;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.lqrl.school.FileHandler;
import com.lqrl.school.HomeActivity;
import com.lqrl.school.R;
import com.lqrl.school.note_builder.Line;
import com.lqrl.school.note_builder.Node;
import com.lqrl.school.note_builder.NoteBuilderView;

public class NoteBuilderFragment extends Fragment {
    Context activity;

    public NoteBuilderFragment(Context context){
        activity = context;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((HomeActivity)context).addToolbarNoteBuilderButtons();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.note_builder_fragment, parent, false);
        NoteBuilderView noteBuilderView = root.findViewById(R.id.diagramView);
        noteBuilderView.drawFromJSON(getString(R.string.test_diagram1));
//        noteBuilderView.drawNode(new Node("Node1", getString(R.string.irraz_ner_title), getString(R.string.irraz_ner_def), new RectF(100, 100, 700, 500)));
//        noteBuilderView.drawNode(new Node("Node2", getString(R.string.isp_ravn_preobr_title), getString(R.string.isp_ravn_preobr_def), new RectF(50, 900, 500, 1300)));
//        noteBuilderView.drawNode(new Node("Node3", getString(R.string.isp_meth_intervalov_title), getString(R.string.isp_meth_intervalov_def), new RectF(600, 1100, 1050, 1500)));
//        noteBuilderView.drawNode(new Node("Node3", getString(R.string.isp_meth_intervalov_title), getString(R.string.isp_meth_intervalov_def), new RectF(1100, 1100, 1550, 1500)));
//        //noteBuilderView.drawNode(new Node("Node2", getString(R.string.isp_ravn_preobr_title), getString(R.string.isp_ravn_preobr_def), new RectF(550, 900, 500, 1300)));
//        noteBuilderView.drawLine(new Line(400, 500, 275, 900));
//        noteBuilderView.drawLine(new Line(400, 500, 700, 1100));


//        noteBuilderView.drawNode(new Node("Node_2", ""))
//        noteBuilderView.drawNode(new Node("Psychology", getString(R.string.lorem), new RectF(100, 100, 700, 500)));
//        noteBuilderView.drawLine(new Line(700, 500, 900, 100));
//        noteBuilderView.drawLine(new Line(700, 500, 100, 700));
//        noteBuilderView.drawLine(new Line(700, 500, 900, 900));
//        noteBuilderView.drawLine(new Line(700, 500, 1000, 400));
//        noteBuilderView.drawLine(new Line(700, 500, 200, 200));
//        noteBuilderView.drawLine(new Line(700, 500, 1000, 450));
//        noteBuilderView.drawLine(new Line(700, 500, 750, 100));
//        noteBuilderView.drawLine(new Line(700, 500, 100, 100));
//        noteBuilderView.drawLine(new Line(700, 500, 900, 700));
//        noteBuilderView.drawLine(new Line(700, 500, 500, 300));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((HomeActivity)activity).clearToolbarMenu();
    }
}
