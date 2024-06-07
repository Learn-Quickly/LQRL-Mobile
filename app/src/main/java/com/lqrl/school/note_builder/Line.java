package com.lqrl.school.note_builder;

import android.graphics.RectF;

public class Line {
    private Node n1, n2;

    public Line(int beginX, int beginY, int endX, int endY){

    }
    public Line(RectF rect){

    }

    public Line(Node n1, Node n2){
        this.n1 = n1; this.n2 = n2;
    }
}
