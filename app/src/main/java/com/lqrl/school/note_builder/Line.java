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
        n1 = n2 = null;
    }

    boolean isNode2ToLeftSideOf1(Node n1, Node n2){
        return n1.rect.left > n2.rect.right;
    }

    boolean isNode2ToBottomSideOf1(Node n1, Node n2){
        return n1.rect.bottom < n2.rect.top;
    }

    boolean isNode2ToTopSideOf1(Node n1, Node n2){
        return n1.rect.top > n2.rect.bottom;
    }

    boolean isNode2ToRightSideOf1(Node n1, Node n2){
        return n1.rect.right < n2.rect.left;
    }

    public Line(Node n1, Node n2){ // line from 1 to 2
        this.n1 = n1; this.n2 = n2;
        if(isNode2ToLeftSideOf1(n1, n2)){ // from 1's left mid side to 2's right mid side
            bX = n1.rect.left; bY = n1.rect.top + n1.rect.height() / 2;
            eX = n2.rect.right; eY = n2.rect.top + n2.rect.height() / 2;
        } else if(isNode2ToRightSideOf1(n1, n2)){ // from 1's right mid side to 2's left mid side
            bX = n1.rect.right; bY = n1.rect.top + n1.rect.height() / 2;
            eX = n2.rect.left; eY = n2.rect.top + n2.rect.height() / 2;
        } else if(isNode2ToBottomSideOf1(n1, n2)){ // from 1's bottom mid side to 2's top mid side
            bX = n1.rect.left + n1.rect.width() / 2; bY = n1.rect.bottom;
            eX = n2.rect.left + n2.rect.width() / 2; eY = n2.rect.top;
        } else if(isNode2ToTopSideOf1(n1, n2)){ // from 1's top mid side to 2's bottom mid side
            bX = n1.rect.left + n1.rect.width() / 2; bY = n1.rect.top;
            eX = n2.rect.left + n2.rect.width() / 2; eY = n2.rect.bottom;
        } else {
            throw new RuntimeException("Cannot calculate relative positions of Nodes");
        }
    }

    public void reCalculateLinks(){ // line from 1 to 2
        if(n1 != null && n2 != null){
            if(isNode2ToLeftSideOf1(n1, n2)){ // from 1's left mid side to 2's right mid side
                bX = n1.rect.left; bY = n1.rect.top + n1.rect.height() / 2;
                eX = n2.rect.right; eY = n2.rect.top + n2.rect.height() / 2;
            } else if(isNode2ToRightSideOf1(n1, n2)){ // from 1's right mid side to 2's left mid side
                bX = n1.rect.right; bY = n1.rect.top + n1.rect.height() / 2;
                eX = n2.rect.left; eY = n2.rect.top + n2.rect.height() / 2;
            } else if(isNode2ToBottomSideOf1(n1, n2)){ // from 1's bottom mid side to 2's top mid side
                bX = n1.rect.left + n1.rect.width() / 2; bY = n1.rect.bottom;
                eX = n2.rect.left + n2.rect.width() / 2; eY = n2.rect.top;
            } else if(isNode2ToTopSideOf1(n1, n2)){ // from 1's top mid side to 2's bottom mid side
                bX = n1.rect.left + n1.rect.width() / 2; bY = n1.rect.top;
                eX = n2.rect.left + n2.rect.width() / 2; eY = n2.rect.bottom;
            } else {
                throw new RuntimeException("Cannot calculate relative positions of Nodes");
            }
        }
    }
}
