package com.lqrl.school.note_builder;

import android.graphics.RectF;

public class Node {
    public RectF rect;
    public String title;
    public String description;

    public Node(String title, String description, RectF rect){
        this.title = title; this.description = description; this.rect = rect;
    }
}
