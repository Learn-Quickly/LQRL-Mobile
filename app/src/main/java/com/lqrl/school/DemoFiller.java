package com.lqrl.school;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.lqrl.school.entities.Course;
import com.lqrl.school.entities.Exercise;
import com.lqrl.school.entities.Lesson;
import com.lqrl.school.note_builder.Line;
import com.lqrl.school.note_builder.Node;
import com.lqrl.school.note_builder.NoteBuilderView;
import com.lqrl.school.web_services.CreateCourseDraftTask;
import com.lqrl.school.web_services.CreateExerciseTask;
import com.lqrl.school.web_services.CreateLessonTask;
import com.lqrl.school.web_services.LoginTask;
import com.lqrl.school.web_services.RegisterTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class DemoFiller extends AsyncTask<Void, Void, String> {
    public static String TAG = "DemoFiller";
    private Context context;
    public DemoFiller(Context context){
        this.context = context;
    }

    public void FillDemo(Context context) throws JSONException {
        Random random = new Random();
        String username = "3", password = "3";
        RegisterTask.registerUser(username, password);

        String result = LoginTask.loginUser(username, password);
        if(!result.isEmpty()){
            JSONObject root = new JSONObject(result);
            JSONObject res = root.getJSONObject("result");
            String accessToken = res.getString("access_token");
            Course course = new Course(-1, "Комбінаторика №" + random.nextInt(1000), 0, 0, Color.GRAY,
                    context.getString(R.string.combinatorics_course_desc),
                    "Draft");
            result = CreateCourseDraftTask.createCourseDraft(course, accessToken);
            if(!result.isEmpty()){
                root = new JSONObject(result);
                int courseId = root.getInt("course_id");
                course.courseId = courseId;
                Lesson l1 = new Lesson(-1, context.getString(R.string.l1_title),
                        context.getString(R.string.l1_desc), courseId);
                Lesson l2 = new Lesson(-1, context.getString(R.string.l2_title),
                        context.getString(R.string.l2_desc), courseId);
                String l1Res = CreateLessonTask.createLesson(l1, accessToken);
                String l2Res = CreateLessonTask.createLesson(l2, accessToken);
                if(l1Res != null && l2Res != null) {
                    root = new JSONObject(l1Res);
                    int l1Id = root.getInt("lesson_id");
                    l1.Id = l1Id;
                    root = new JSONObject(l2Res);
                    int l2Id = root.getInt("lesson_id");
                    l2.Id = l2Id;
                    ArrayList<Node> nodes = new ArrayList<>();
                    Node n1 = new Node("n1", "", "Комбінаторика", 10, 10);
                    Node n2 = new Node("n2",  "", "Вивчає побудову комбінацій",700, 10);
                    Node n3 = new Node("n3",  "", "Природа комбінацій не є важливою",360, 400);
                    nodes.add(n1); nodes.add(n2); nodes.add(n3);
                    ArrayList<Line> lines = new ArrayList<>();
                    lines.add(new Line(n1, n2));
                    lines.add(new Line(n1, n3));
                    Exercise ex1 = new Exercise(-1, "Що таке комбінаторика?",
                            "Дізнаємося, що таке комбінаторика, чим вона займається, і чи варто її взагалі вивчати.", l1.Id, 60, "Easy", null, null);
                    NoteBuilderView.saveJSONDiagramToExercise(NoteBuilderView.Mode.NoteConstructor, ex1, context, nodes, lines);
                    NoteBuilderView.saveJSONDiagramToExercise(NoteBuilderView.Mode.AnswerConstructor, ex1, context, nodes, lines);
                    result = CreateExerciseTask.createExercise(ex1, accessToken);
                    if(result != null){
                        root = new JSONObject(result);
                        int exerciseId = root.getInt("exercise_id");
                        ex1.Id = exerciseId;
                        Log.i(TAG, "ex1 created successfully with Id = " + ex1.Id);
                    } else {
                        Log.e(TAG, "ERROR: Create exercise failed");
                    }
                } else {
                    Log.e(TAG, "ERROR: Create lesson failed");
                }
            } else{
                Log.e(TAG, "ERROR: Create course draft failed");
            }
        } else {
            Log.e(TAG, "ERROR: Login failed");
        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            FillDemo(context);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return "";
    }
}
