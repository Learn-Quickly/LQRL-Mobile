package com.lqrl.school.note_builder;

import android.graphics.RectF;

public class Line {
    public Node n1, n2;
    public float bX, bY, eX, eY;

    public Line(float beginX, float beginY, float endX, float endY){
        bX = beginX; bY = beginY; eX = endX; eY = endY;
    }

    public Line(RectF rect){
        bX = rect.left;
        bY = rect.top;
        eX = rect.right;
        eY = rect.bottom;
    }

    public Line(Node n1, Node n2){

    }
}
