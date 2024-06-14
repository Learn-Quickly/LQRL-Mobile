package com.lqrl.school.interfaces;

import com.lqrl.school.entities.Exercise;
import com.lqrl.school.note_builder.NoteBuilderView;

public interface NoteBuilderDealer {
    void goToBuilder(Exercise currentItem, NoteBuilderView.Mode mode);
}
