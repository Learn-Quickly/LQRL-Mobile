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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private Paint linePaint;

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
        nodeTextPaint.setTextSize(20.0f);
        nodeTextPaint.setStrokeWidth(1.0f);
        nodeTextPaint.setStyle(Paint.Style.FILL);

        linePaint = new Paint();
        linePaint.setColor(Color.RED);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(5.0f);

        gestureDetector = new GestureDetector(context, this);
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        nodes = new ArrayList<>();
        lines = new ArrayList<>();
    }

//    String testJSON = "{\"connections\": [{\"from": "Node_1488" // Node id
//      "to": "Node_228" // Node id
//    }
//  ],
//  "nodes": [
//      {
//        "id": "Node_1488",
//        "x": 56,
//        "y": -34,
//        "node_type": "Definition",
//        "body": {
//          "header": "",
//          "definition": ""
//        }
//      }
//  ]
//}
//";

    public void drawFromJSON(String json){
        nodes.clear();
        lines.clear();
        try{
            JSONObject root = new JSONObject(json);
            JSONArray nodesJSON = root.getJSONArray("nodes");
            for(int i = 0; i < nodesJSON.length(); i++){
                JSONObject nodeObj = nodesJSON.getJSONObject(i);
                String id = nodeObj.getString("id");
                int x = nodeObj.getInt("x");
                int y = nodeObj.getInt("y");
                String node_type = nodeObj.getString("node_type");
                String header = "", definition = "";
                JSONObject bodyObj = nodeObj.getJSONObject("body");
                if(node_type.equals("Definition")){
                    header = bodyObj.getString("header");
                    definition = bodyObj.getString("definition");
                } else if(node_type.equals("Header")){
                    header = bodyObj.getString("header");
                }
                Node node = new Node(id, header, definition, x, y);
                nodes.add(node);
            }

            JSONArray connectionsJSON = root.getJSONArray("connections");
            for(int i = 0; i < connectionsJSON.length(); i++){
                JSONObject lineObj = connectionsJSON.getJSONObject(i);
                String from = lineObj.getString("from");
                String to = lineObj.getString("to");
                Node n1 = findNodeById(from);
                Node n2 = findNodeById(to);
                if(n1 != null && n2 != null){
                    Line line = new Line(n1, n2);
                    lines.add(line);
                }
            }

        } catch(JSONException e){
            throw new RuntimeException(e);
        }
        invalidate();
    }

    private Node findNodeById(String id){
        for(int i = 0; i < nodes.size(); i++){
            if(nodes.get(i).id.equals(id)) return nodes.get(i);
        }
        return null;
    }

    public void drawNode(Node node){
        nodes.add(node);
        invalidate();
    }

    public void drawLine(Line line){
        lines.add(line);
        invalidate();
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

        for(int i = 0; i < lines.size(); i++){
            renderLine(canvas, lines.get(i));
        }

        canvas.restore();
    }

    private void renderNode(Canvas canvas, Node node) {
        renderNodeBlock(canvas, node);
        renderNodeTitle(canvas, node);
        renderNodeDescriptionWidthAligned(canvas, node);
    }

    private void renderLine(Canvas canvas, Line line){
        Path linePath = new Path();
        linePath.moveTo(line.bX, line.bY);
        linePath.lineTo(line.eX, line.eY);
        canvas.drawPath(linePath, linePaint);

        float lineVectorX = line.eX - line.bX;
        float lineVectorY = line.eY - line.bY;

        float searchOffsetCoeff = 10f;

        float triangleHeightGroundXPoint = line.eX - (lineVectorX / searchOffsetCoeff);
        float triangleHeightGroundYPoint = line.eY - (lineVectorY / searchOffsetCoeff);

        float heightSearchBeginLineXPoint = triangleHeightGroundXPoint - (lineVectorX / (2 * searchOffsetCoeff));
        float heightSearchBeginLineYPoint = triangleHeightGroundYPoint - (lineVectorY / (2 * searchOffsetCoeff));

        float heightSearchEndLineXPoint = triangleHeightGroundXPoint + (lineVectorX / (2 * searchOffsetCoeff));
        float heightSearchEndLineYPoint = triangleHeightGroundYPoint + (lineVectorY / (2 * searchOffsetCoeff));

        float parallelLinePixelOffset = 30f;

        float hypotenuseLineVector = (float) Math.sqrt((lineVectorX * lineVectorX) + (lineVectorY * lineVectorY));
        float sinCoeff = lineVectorY / hypotenuseLineVector;
        float cosCoeff = lineVectorX / hypotenuseLineVector;

        float parallelLineSearchHeightBeginX = heightSearchBeginLineXPoint + sinCoeff * parallelLinePixelOffset;
        float parallelLineSearchHeightBeginY = heightSearchBeginLineYPoint + cosCoeff * parallelLinePixelOffset;

        float parallelLineSearchHeightEndX = heightSearchEndLineXPoint + sinCoeff * parallelLinePixelOffset;
        float parallelLineSearchHeightEndY = heightSearchEndLineYPoint + cosCoeff * parallelLinePixelOffset;

        float shortestLine = Float.MAX_VALUE;

        float hX = triangleHeightGroundXPoint, hY = triangleHeightGroundYPoint;
        float stepX = lineVectorX / (10 * searchOffsetCoeff);
        float stepY = lineVectorY / (10 * searchOffsetCoeff);
        float sX = parallelLineSearchHeightBeginX;
        float sY = parallelLineSearchHeightBeginY;
        float minX = 0, minY = 0;

        while ((sX != parallelLineSearchHeightEndX) && (sY != parallelLineSearchHeightEndY)) {
            float lineLen = (float) Math.sqrt((sX - hX) * (sX - hX) + (sY - hY) * (sY - hY));
            if(shortestLine > lineLen) {
                shortestLine = lineLen;
                minX = sX; minY = sY;
            }
            sX += stepX;
            sY += stepY;
        }

        float heightVectorX = minX - hX;
        float heightVectorY = minY - hY;

        float rightFinalPointX = hX + heightVectorX;
        float rightFinalPointY = hY + heightVectorY;

        float leftFinalPointX = hX - heightVectorX;
        float leftFinalPointY = hY - heightVectorY;

        Path arrowPath = new Path();
        arrowPath.moveTo(line.eX, line.eY);
        arrowPath.lineTo(rightFinalPointX, rightFinalPointY);
        arrowPath.moveTo(leftFinalPointX, leftFinalPointY);
        arrowPath.lineTo(line.eX, line.eY);
        canvas.drawPath(arrowPath, linePaint);
    }

    private void renderNodeBlock(Canvas canvas, Node node){
        canvas.drawRect(node.rect, paint);
        Path pathHeader = new Path();
        pathHeader.moveTo(node.rect.left, node.rect.top + node.rect.height()/4f);
        pathHeader.lineTo(node.rect.right, node.rect.top + node.rect.height()/4f);
        canvas.drawPath(pathHeader, paint);
    }

    private void renderNodeTitle(Canvas canvas, Node node){
        nodeTextPaint.setTextSize(30.0f);
        nodeTextPaint.setStrokeWidth(2.0f);
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
        nodeTextPaint.setTextSize(30.0f);
        nodeTextPaint.setStrokeWidth(2.0f);
        float descriptionPaddingPx = 10f;
        float lineSpacingPx = 20f;
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
            wordIndex--;
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
