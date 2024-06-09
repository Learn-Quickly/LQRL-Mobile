package com.lqrl.school.note_builder;

import android.graphics.Rect;
import android.graphics.RectF;

public class Node {
    public String title;
    public String description;
    public String id;
    public int xBegin, yBegin;
    public RectF rect;

    public Node(String id, String title, String description, int xBegin, int yBegin){
        this.title = title; this.description = description;
        this.id = id; this.xBegin = xBegin; this.yBegin = yBegin;
        rect = new RectF(xBegin, yBegin, xBegin + 500, yBegin + 300);
    }

    public Node(String id, String title, String description, RectF rect){
        this.title = title; this.description = description;
        this.id = id; this.rect = new RectF(rect);
    }
}
