package com.lqrl.school.note_builder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class NoteBuilderView extends View implements GestureDetector.OnGestureListener {
    private Paint paint;
    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;
    private Canvas bCanvas;
    private Bitmap bitmap;
    private float scaleFactor = 1.0f;
    private float scrollX = 0;
    private float scrollY = 0;
    private ArrayList<Node> nodes;
    private ArrayList<Line> lines;
    private Paint nodeTextPaint;

    public NoteBuilderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        nodeTextPaint = new Paint();
        nodeTextPaint.setAntiAlias(true);
        nodeTextPaint.setColor(0xFF000000);
        nodeTextPaint.setTextSize(50.0f);
        nodeTextPaint.setStrokeWidth(3.0f);
        nodeTextPaint.setStyle(Paint.Style.FILL);
        //nodeTextPaint.setShadowLayer(5.0f, 10.0f, 10.0f, Color.BLACK);

        gestureDetector = new GestureDetector(context, this);
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        nodes = new ArrayList<>();
        lines = new ArrayList<>();
    }

    public void drawNode(Node node){
        nodes.add(node);
        invalidate();
    }

    public void drawConnection(Line line){

    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        // Apply scaling and translation
        canvas.save();
        canvas.scale(scaleFactor, scaleFactor);
        canvas.translate(scrollX, scrollY);

        for(int i = 0; i < nodes.size(); i++){
            renderNode(canvas, nodes.get(i));
        }

        canvas.restore();
    }

    private void renderNode(Canvas canvas, Node node) {
        renderNodeBlock(canvas, node);
        renderNodeTitle(canvas, node);
        renderNodeDescriptionWidthAligned(canvas, node);
    }

    private void renderNodeBlock(Canvas canvas, Node node){
        canvas.drawRect(node.rect, paint);
        Path pathHeader = new Path();
        pathHeader.moveTo(node.rect.left, node.rect.top + node.rect.height()/4f);
        pathHeader.lineTo(node.rect.right, node.rect.top + node.rect.height()/4f);
        canvas.drawPath(pathHeader, paint);
    }

    private void renderNodeTitle(Canvas canvas, Node node){
        nodeTextPaint.setTextSize(50.0f);
        nodeTextPaint.setStrokeWidth(3.0f);
        drawRectCenteredString(canvas, node.title,
                new RectF(node.rect.left,
                        node.rect.top,
                        node.rect.left + node.rect.width(),
                        node.rect.top + node.rect.height() / 4f));
    }

    private void drawRectCenteredString(Canvas canvas, String text, RectF rect){
        Rect headerRect = new Rect();
        nodeTextPaint.getTextBounds(text, 0, text.length(), headerRect);
        float textWidth, textHeight;
        textWidth = nodeTextPaint.measureText(text);
        textHeight = headerRect.height();
        float xTextBegin, yTextBegin;
        float xTextCenter, yTextCenter;
        xTextCenter = rect.left + rect.width()/2f;
        yTextCenter = rect.top + rect.height()/2f;
        xTextBegin = xTextCenter - textWidth/2f;
        yTextBegin = yTextCenter + textHeight/2f;
        canvas.drawText(text, xTextBegin, yTextBegin, nodeTextPaint);
    }

    private void renderNodeDescriptionWidthAligned(Canvas canvas, Node node){
        nodeTextPaint.setTextSize(40.0f);
        nodeTextPaint.setStrokeWidth(2.0f);
        float descriptionPaddingPx = 30f;
        float lineSpacingPx = 10f;
        float maxLineWidthPx = node.rect.width() - 2 * descriptionPaddingPx;
        float textHeight;
        Rect textBounds = new Rect();
        nodeTextPaint.getTextBounds(node.description, 0, node.description.length(), textBounds);
        textHeight = textBounds.height();
        int maxLinesCount = (int) ((node.rect.height() - 2 * descriptionPaddingPx - lineSpacingPx)
                        / (textHeight + lineSpacingPx));
        float currentLineWidth = 0f;
        String[] words = node.description.split(" ");
        int wordIndex = 0; int lineIndex = 0;

        StringBuilder currentLine = new StringBuilder(); boolean hitMaxInside = false;
        String lastCopy = null;
        while(lineIndex < maxLinesCount){
            if(wordIndex == words.length) break;
            hitMaxInside = false;
            currentLine.setLength(0);
            while(!hitMaxInside && wordIndex < words.length){
                lastCopy = currentLine.toString();
                currentLine.append(words[wordIndex++]);
                currentLine.append(" ");
                currentLineWidth = nodeTextPaint.measureText(currentLine.toString());
                hitMaxInside = currentLineWidth > maxLineWidthPx;
            }
            // drawText
            float titleHeight = node.rect.height() / 4f;

            float xBeginLine = descriptionPaddingPx + node.rect.left;
            float yBeginLine = node.rect.top
                    + titleHeight
                    + descriptionPaddingPx
                    + (textHeight + lineSpacingPx) * lineIndex;
            float xEndLine = xBeginLine + maxLineWidthPx;
            float yEndLine = yBeginLine + textHeight;
            drawRectCenteredString(canvas, lastCopy,
                    new RectF(xBeginLine,
                            yBeginLine,
                            xEndLine,
                            yEndLine));
            lineIndex++;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // Handle single tap
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        scrollX -= distanceX / scaleFactor;
        scrollY -= distanceY / scaleFactor;
        invalidate();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) { }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f));
            invalidate();
            return true;
        }
    }
}
