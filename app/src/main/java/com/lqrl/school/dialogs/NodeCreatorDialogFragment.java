package com.lqrl.school.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.lqrl.school.R;
import com.lqrl.school.fragments.NoteBuilderFragment;
import com.lqrl.school.interfaces.NodeCreator;
import com.lqrl.school.note_builder.Node;

public class NodeCreatorDialogFragment extends DialogFragment {
    NoteBuilderFragment fragment;
    Context activity;
    public NodeCreatorDialogFragment(NoteBuilderFragment fragment, Context context){
        this.fragment = fragment;
        this.activity = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.create_node_dialog, null);
        EditText editTextTitle = view.findViewById(R.id.et_node_title);
        EditText editTextDescription = view.findViewById(R.id.et_node_desc);

        builder.setTitle(R.string.create_node)
                .setView(view)
                .setPositiveButton("Ok", (dialog, which) -> {
                    String title = editTextTitle.getText().toString();
                    String description = editTextDescription.getText().toString();
                    if(title.isEmpty()){
                        Toast.makeText(activity, getString(R.string.field_title_is_empty), Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                    ((NodeCreator) fragment).addNode(new Node("_id", title, description, 0, 0));
                })
                .setNegativeButton(getString(R.string.cancel_dialog), (dialog, which) -> {

                });
        return builder.create();
    }
}
