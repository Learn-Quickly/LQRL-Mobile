package com.lqrl.school.note_builder;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.lqrl.school.HomeActivity;
import com.lqrl.school.R;
import com.lqrl.school.entities.Exercise;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class NoteBuilderView extends View implements GestureDetector.OnGestureListener {
    public static String TAG = "NoteBuilderView";
    private Paint paint;
    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.0f;
    private float scrollX = 0;
    private float scrollY = 0;
    private ArrayList<Node> nodes;
    private ArrayList<Line> lines;
    private Paint nodeTextPaint;
    private Paint linePaint;
    private boolean resizeNodeMode = false;
    private boolean connectionMode = false;
    private boolean deleteMode = false;

     public static enum Mode {
        NoteConstructor,
        AnswerConstructor
     }

    // TODO do we need to ensure correctness? Modulo canvas size logic on scrolling
    // UPD fixed move node after scroll
    // UPD fixed move node after scale

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

    public void drawFromJSON(JSONObject json) {
        nodes.clear();
        lines.clear();
        if(json == null) invalidate();
        else {
            try {
                JSONObject root = json;
                JSONArray nodesJSON = root.getJSONArray("nodes");
                for (int i = 0; i < nodesJSON.length(); i++) {
                    JSONObject nodeObj = nodesJSON.getJSONObject(i);
                    String id = nodeObj.getString("id");
                    int x = nodeObj.getInt("x");
                    int y = nodeObj.getInt("y");
                    String node_type = nodeObj.getString("node_type");
                    String header = "", definition = "";
                    JSONObject bodyObj = nodeObj.getJSONObject("body");
                    if (node_type.equals("Definition")) {
                        header = bodyObj.getString("header");
                        definition = bodyObj.getString("definition");
                    } else if (node_type.equals("Header")) {
                        header = bodyObj.getString("header");
                    }
                    Node node = new Node(id, header, definition, x, y);
                    nodes.add(node);
                }

                JSONArray connectionsJSON = root.getJSONArray("connections");
                for (int i = 0; i < connectionsJSON.length(); i++) {
                    JSONObject lineObj = connectionsJSON.getJSONObject(i);
                    String idFrom = lineObj.getString("from");
                    String idTo = lineObj.getString("to");
                    Node fromNode = findNodeById(idFrom);
                    Node toNode = findNodeById(idTo);
                    lines.add(new Line(fromNode, toNode));
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            invalidate();
        }
    }

    public void drawFromOptPrefs(Context activity) throws JSONException {
        SharedPreferences preferences = ((HomeActivity)activity).getPreferences(Context.MODE_PRIVATE);
        String JSON = preferences.getString(activity.getString(R.string.note_builder_mode_diagram_json), "");
        Log.e(TAG, JSON);
        if(!JSON.isEmpty())
            drawFromJSON(new JSONObject(JSON));
        else drawFromJSON(new JSONObject(activity.getString(R.string.test_diagram1)));
    }

    public void saveJSONDiagramToExercise(Mode mode, Exercise exercise, Context activity) {
        String jsonResult = null;

        try {
            JSONObject root = new JSONObject();
            JSONArray nodesJSON = new JSONArray();
            JSONArray connectionsJSON = new JSONArray();

            for (int i = 0; i < nodes.size(); i++) {
                JSONObject jsonNode = convertNodeToJSON(nodes.get(i));
                nodesJSON.put(i, jsonNode);
            }

            for (int i = 0; i < lines.size(); i++) {
                JSONObject jsonConnection = convertLineToJSON(lines.get(i));
                connectionsJSON.put(i, jsonConnection);
            }

            root.put("nodes", nodesJSON);
            root.put("connections", connectionsJSON);

            //jsonResult = root.toString();
            if(mode == Mode.AnswerConstructor) exercise.AnswerBody = root;
            else exercise.ExerciseBody = root;
            Toast.makeText(activity, "Note was saved!", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject convertLineToJSON(Line line) throws JSONException {
        JSONObject lineObj = new JSONObject();
        lineObj.put("from", line.n1.id);
        lineObj.put("to", line.n2.id);
        return lineObj;
    }

    @NonNull
    private JSONObject convertNodeToJSON(Node node) throws JSONException {
        JSONObject nodeObj = new JSONObject();
        nodeObj.put("id", node.id);
        nodeObj.put("x", (int) node.rect.left);
        nodeObj.put("y", (int) node.rect.top);
        JSONObject nodeBody = new JSONObject();
        if(node.description.isEmpty()){
            nodeObj.put("node_type", "Header");
            nodeBody.put("header", node.title);
        } else{
            nodeObj.put("node_type", "Definition");
            nodeBody.put("header", node.title);
            nodeBody.put("definition", node.description);
        }
        nodeObj.put("body", nodeBody);
        return nodeObj;
    }


    private Node findNodeById(String id) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).id.equals(id)) return nodes.get(i);
        }
        return null;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.scale(scaleFactor, scaleFactor);

        renderGrid(canvas);

        canvas.restore();

        canvas.save();
        canvas.scale(scaleFactor, scaleFactor);
        canvas.translate(scrollX, scrollY);

        for (int i = 0; i < nodes.size(); i++) {
            renderNode(canvas, nodes.get(i), 40f);
        }

        for (int i = 0; i < lines.size(); i++) {
            renderLine(canvas, lines.get(i));
        }

        canvas.restore();
    }

    public void createNodeFromPayload(Node node){
        UUID uuid = UUID.randomUUID();
        node.id = uuid.toString();
        int maxH = getHeight() - 100, maxW = getWidth() - 100;
        boolean stuck = true; int x = 0, y = 0;
        Random r = new Random();
        while(stuck){
            x = r.nextInt(maxW);
            y = r.nextInt(maxH);
            stuck = false;
            for(Node n : nodes){
                stuck |= isCursorInsideNodeRect(x, y, n.rect);
            }
        }
        node.xBegin = x; node.yBegin = y;
        node.rect = new RectF(x, y, x + 500, y + 300);
        nodes.add(node);
    }

    public void toggleConnectionMode() {
        deleteMode = false;
        connectionMode = !connectionMode;
        Toast.makeText(getContext(), "Connection mode " + (connectionMode ? "ON" : "OFF"), Toast.LENGTH_SHORT).show();
    }

    public void toggleDeleteNodeMode() {
        connectionMode = false;
        deleteMode = !deleteMode;
        Toast.makeText(getContext(), "Delete mode " + (connectionMode ? "ON" : "OFF"), Toast.LENGTH_SHORT).show();
    }

    private void renderGrid(Canvas canvas) {
        float canvasWidth = canvas.getWidth() / scaleFactor;
        float canvasHeight = canvas.getHeight() / scaleFactor;
        float gridColumnMargin = canvasWidth / 10f * scaleFactor * scaleFactor;
        float gridRowMargin = canvasHeight / 10f * scaleFactor * scaleFactor;
        Paint paintGrid = new Paint();
        paintGrid.setColor(Color.GRAY);
        paintGrid.setColor(0xffacacac);
        paintGrid.setAntiAlias(true);
        paintGrid.setStrokeWidth(3 * scaleFactor);
        paintGrid.setStyle(Paint.Style.STROKE);
        for (float startX = gridColumnMargin; startX < canvasWidth; startX += gridColumnMargin)
            canvas.drawLine(startX, 0, startX, canvasHeight, paintGrid);
        for (float startY = gridRowMargin; startY < canvasHeight; startY += gridRowMargin)
            canvas.drawLine(0, startY, canvasWidth, startY, paintGrid);
    }

    private void renderNode(Canvas canvas, Node node, float fontSize) {
        renderNodeBlock(canvas, node);
        renderNodeTitle(canvas, node, fontSize);
        renderNodeDescriptionWidthAligned(canvas, node, fontSize);
        renderNodeAnchors(canvas, node);
    }

    private void renderLine(Canvas canvas, Line line) {
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

        boolean isMinusStepX = stepX < 0;
        boolean isMinusStepY = stepY < 0;
        boolean condUntilX, condUntilY;
        if (isMinusStepX) condUntilX = sX >= parallelLineSearchHeightEndX;
        else condUntilX = sX <= parallelLineSearchHeightEndX;
        if (isMinusStepY) condUntilY = sY >= parallelLineSearchHeightEndY;
        else condUntilY = sY <= parallelLineSearchHeightEndY;

        while (condUntilX && condUntilY) {
            float lineLen = (float) Math.sqrt((sX - hX) * (sX - hX) + (sY - hY) * (sY - hY));
            if (shortestLine > lineLen) {
                shortestLine = lineLen;
                minX = sX;
                minY = sY;
            }
            sX += stepX;
            sY += stepY;

            if (isMinusStepX) condUntilX = sX >= parallelLineSearchHeightEndX;
            else condUntilX = sX <= parallelLineSearchHeightEndX;
            if (isMinusStepY) condUntilY = sY >= parallelLineSearchHeightEndY;
            else condUntilY = sY <= parallelLineSearchHeightEndY;
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

    private void renderNodeBlock(Canvas canvas, Node node) {
        canvas.drawRect(node.rect, paint);
        Path pathHeader = new Path();
        pathHeader.moveTo(node.rect.left, node.rect.top + node.rect.height() / 4f);
        pathHeader.lineTo(node.rect.right, node.rect.top + node.rect.height() / 4f);
        canvas.drawPath(pathHeader, paint);
    }

    private void drawRectCenteredString(Canvas canvas, String text, RectF rect) {
        Rect headerRect = new Rect();
        nodeTextPaint.getTextBounds(text, 0, text.length(), headerRect);
        float textWidth, textHeight;
        textWidth = nodeTextPaint.measureText(text);
        textHeight = headerRect.height();
        float xTextBegin, yTextBegin;
        float xTextCenter, yTextCenter;
        xTextCenter = rect.left + rect.width() / 2f;
        yTextCenter = rect.top + rect.height() / 2f;
        xTextBegin = xTextCenter - textWidth / 2f;
        yTextBegin = yTextCenter + textHeight / 2f;
        canvas.drawText(text, xTextBegin, yTextBegin, nodeTextPaint);
    }

    private void renderNodeTitle(Canvas canvas, Node node, float fontSize) {
        nodeTextPaint.setTextSize(fontSize);
        nodeTextPaint.setStrokeWidth(4f);
        float titlePaddingPx = 5f;
        float lineSpacingPx = 10f;
        float maxLineWidthPx = node.rect.width() - 2 * titlePaddingPx;
        float textHeight;
        Rect textBounds = new Rect();
        nodeTextPaint.getTextBounds(node.title, 0, node.title.length(), textBounds);
        textHeight = textBounds.height();
        int maxLinesCount = (int) ((node.rect.height() - 2 * titlePaddingPx - lineSpacingPx)
                / (textHeight + lineSpacingPx));
        //int maxLinesCount = 20;
        float currentLineWidth;
        String[] words = node.title.split(" ");
        int wordIndex = 0;
        int lineIndex = 0;

        StringBuilder currentLine = new StringBuilder();
        String lastCopy = null;
        while (lineIndex < maxLinesCount) {
            if (wordIndex == words.length) break;
            currentLine.setLength(0);
            while (wordIndex < words.length) {
                currentLine.append(words[wordIndex++]);
                currentLine.append(" ");
                currentLineWidth = nodeTextPaint.measureText(currentLine.toString());
                if (currentLineWidth > maxLineWidthPx) {
                    wordIndex--;
                    break;
                } else {
                    lastCopy = currentLine.toString();
                }
            }

            renderNodeText(canvas, node, titlePaddingPx, textHeight, lineSpacingPx, lineIndex, maxLineWidthPx, lastCopy, Float.MAX_VALUE);
            lineIndex++;
        }
    }

    private void renderNodeDescriptionWidthAligned(Canvas canvas, Node node, float fontSize) {
        nodeTextPaint.setTextSize(fontSize);
        nodeTextPaint.setStrokeWidth(4f);
        float descriptionPaddingPx = 30f;
        float lineSpacingPx = 20f;
        float maxLineWidthPx = node.rect.width() - 2 * descriptionPaddingPx;
        float textHeight;
        Rect textBounds = new Rect();
        nodeTextPaint.getTextBounds(node.description, 0, node.description.length(), textBounds);
        textHeight = textBounds.height();
        int maxLinesCount = (int) ((node.rect.height() - 2 * descriptionPaddingPx - lineSpacingPx)
                / (textHeight + lineSpacingPx));
        float currentLineWidth;
        String[] words = node.description.split(" ");
        int wordIndex = 0;
        int lineIndex = 0;

        StringBuilder currentLine = new StringBuilder();
        String lastCopy = null;
        while (lineIndex < maxLinesCount) {
            if (wordIndex == words.length) break;
            currentLine.setLength(0);
            while (wordIndex < words.length) {
                currentLine.append(words[wordIndex++]);
                currentLine.append(" ");
                currentLineWidth = nodeTextPaint.measureText(currentLine.toString());
                if (currentLineWidth > maxLineWidthPx) {
                    wordIndex--;
                    break;
                } else {
                    lastCopy = currentLine.toString();
                }
            }

            renderNodeText(canvas, node, descriptionPaddingPx, textHeight, lineSpacingPx, lineIndex, maxLineWidthPx, lastCopy, 4);
            lineIndex++;
        }
    }

    private void renderNodeText(Canvas canvas, Node node, float textPaddingPx, float textHeight,
                                float lineSpacingPx, int lineIndex, float maxLineWidthPx, String lastCopy,
                                float offsetFract) {
        float heightOffset;
        if (offsetFract == Float.MAX_VALUE) {
            heightOffset = 0;
        } else {
            heightOffset = node.rect.height() / offsetFract;
        }

        float xBeginLine = textPaddingPx + node.rect.left;
        float yBeginLine = node.rect.top
                + heightOffset
                + textPaddingPx
                + (textHeight + lineSpacingPx) * lineIndex;
        float xEndLine = xBeginLine + maxLineWidthPx;
        float yEndLine = yBeginLine + textHeight;
        drawRectCenteredString(canvas, lastCopy,
                new RectF(xBeginLine,
                        yBeginLine,
                        xEndLine,
                        yEndLine));
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

    private void renderNodeAnchors(Canvas canvas, Node node) {
        if (node.anchors) {
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(node.rect.left, node.rect.top, 10 * scaleFactor, paint);
            canvas.drawCircle(node.rect.right, node.rect.bottom, 10 * scaleFactor, paint);
            canvas.drawCircle(node.rect.right, node.rect.top, 10 * scaleFactor, paint);
            canvas.drawCircle(node.rect.left, node.rect.bottom, 10 * scaleFactor, paint);
            paint.setStyle(Paint.Style.STROKE);
        }
    }

    private void switchResizeNode(Node n) {
        for (Node node : nodes) {
            node.anchors = false;
        }
        if (n != null)
            n.anchors = true;
    }

    private boolean processResizeMode(MotionEvent e) {
        float touchX = e.getX(), touchY = e.getY();
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            RectF transRect = getTransformedRect(node.rect);
            if (isCursorInsideNodeRect(touchX, touchY, transRect)) {
                switchResizeNode(node);
                resizeNodeMode = true;
                invalidate();
                return true;
            }
        }
        switchResizeNode(null);
        resizeNodeMode = false;
        invalidate();
        return true;
    }

    private Node choosedFirstToConnect = null;
    private boolean processConnectionMode(MotionEvent e){
        if(choosedFirstToConnect == null){
            float touchX = e.getX(), touchY = e.getY();
            for (int i = 0; i < nodes.size(); i++) {
                Node node = nodes.get(i);
                RectF transRect = getTransformedRect(node.rect);
                if (isCursorInsideNodeRect(touchX, touchY, transRect)) {
                    choosedFirstToConnect = node;
                    invalidate();
                    return true;
                }
            }
        } else {
            float touchX = e.getX(), touchY = e.getY();
            for (int i = 0; i < nodes.size(); i++) {
                Node node = nodes.get(i);
                RectF transRect = getTransformedRect(node.rect);
                if (isCursorInsideNodeRect(touchX, touchY, transRect)) {
                    lines.add(new Line(choosedFirstToConnect, node));
                    toggleConnectionMode();
                    choosedFirstToConnect = null;
                    invalidate();
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if(deleteMode) return processDeleteMode(e);
        else if(connectionMode) return processConnectionMode(e);
        else return processResizeMode(e);
    }

    private boolean processDeleteMode(MotionEvent e) {
        float touchX = e.getX(), touchY = e.getY();
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            RectF transRect = getTransformedRect(node.rect);
            if (isCursorInsideNodeRect(touchX, touchY, transRect)) {

                for(int j = 0; j < lines.size(); j++){
                    if(lines.get(j).n1 == node || lines.get(j).n2 == node){
                        lines.remove(j--);
                    }
                }

                nodes.remove(i); // nodes.remove(node);
                invalidate();
                return true;
            }
        }
        return true;
    }

// TODO do not cross with another node, causing bug!

    RectF getTransformedRect(RectF rect) {
        RectF rectF = new RectF(rect);
        rectF.left *= scaleFactor;
        rectF.top *= scaleFactor;
        rectF.right *= scaleFactor;
        rectF.bottom *= scaleFactor;
        rectF.left += scrollX;
        rectF.top += scrollY;
        rectF.right += scrollX;
        rectF.bottom += scrollY;
        return rectF;
    }

    boolean isCursorInsideNodeRect(float X, float Y, RectF rectF) {
        return X >= rectF.left && X <= rectF.right
                && Y >= rectF.top && Y <= rectF.bottom;
    }

    boolean processCursorMovingAnchors(Node node, RectF rect, MotionEvent e2, float distanceX, float distanceY) {
        if(resizeNodeMode) {
            float touchX = e2.getX(), touchY = e2.getY();
            float pxTouchBoxRadius = 100f / scaleFactor;
            RectF leftTopBox = new RectF(rect.left - pxTouchBoxRadius,
                    rect.top - pxTouchBoxRadius,
                    rect.left + pxTouchBoxRadius,
                    rect.top + pxTouchBoxRadius);
            RectF leftBottomBox = new RectF(rect.left - pxTouchBoxRadius,
                    rect.bottom - pxTouchBoxRadius,
                    rect.left + pxTouchBoxRadius,
                    rect.bottom + pxTouchBoxRadius);
            RectF rightTopBox = new RectF(rect.right - pxTouchBoxRadius,
                    rect.top - pxTouchBoxRadius,
                    rect.right + pxTouchBoxRadius,
                    rect.top + pxTouchBoxRadius);
            RectF rightBottomBox = new RectF(rect.right - pxTouchBoxRadius,
                    rect.bottom - pxTouchBoxRadius,
                    rect.right + pxTouchBoxRadius,
                    rect.bottom + pxTouchBoxRadius);

            if (isCursorInsideNodeRect(touchX, touchY, leftTopBox)) {
                node.rect.left -= distanceX / scaleFactor;
                node.rect.top -= distanceY / scaleFactor;
                return true;
            }
            if (isCursorInsideNodeRect(touchX, touchY, leftBottomBox)) {
                node.rect.left -= distanceX / scaleFactor;
                node.rect.bottom -= distanceY / scaleFactor;
                return true;
            }
            if (isCursorInsideNodeRect(touchX, touchY, rightTopBox)) {
                node.rect.right -= distanceX / scaleFactor;
                node.rect.top -= distanceY / scaleFactor;
                return true;
            }
            if (isCursorInsideNodeRect(touchX, touchY, rightBottomBox)) {
                node.rect.right -= distanceX / scaleFactor;
                node.rect.bottom -= distanceY / scaleFactor;
                return true;
            }
        }

        return false;
    }

    boolean handleNodeMove(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float touchX = e2.getX(), touchY = e2.getY();
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            RectF transRect = getTransformedRect(node.rect);

            if (isCursorInsideNodeRect(touchX, touchY, transRect)) {
                boolean processed = processCursorMovingAnchors(node, transRect, e2, distanceX, distanceY);
                if(!processed) {
                    node.rect.left -= distanceX / scaleFactor;
                    node.rect.right -= distanceX / scaleFactor;
                    node.rect.top -= distanceY / scaleFactor;
                    node.rect.bottom -= distanceY / scaleFactor;
                }
                for (Line line : lines) {
                    line.reCalculateLinks();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (!handleNodeMove(e1, e2, distanceX, distanceY)) {
            scrollX -= distanceX / scaleFactor;
            scrollY -= distanceY / scaleFactor;
        }

        invalidate();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

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
