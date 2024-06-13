package com.lqrl.school;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.lqrl.school.dialogs.PublishCourseDialogFragment;
import com.lqrl.school.entities.Course;
import com.lqrl.school.entities.Lesson;
import com.lqrl.school.fragments.CoursesWatchFragment;
import com.lqrl.school.fragments.CreateCourseFragment;
import com.lqrl.school.fragments.ExercisesWatchFragment;
import com.lqrl.school.fragments.LessonsWatchFragment;
import com.lqrl.school.fragments.NoteBuilderFragment;
import com.lqrl.school.interfaces.CoursePublisher;
import com.lqrl.school.interfaces.LessonOpener;
import com.lqrl.school.interfaces.NoteBuilderDealer;
import com.lqrl.school.interfaces.StringSetter;
import com.lqrl.school.note_builder.NoteBuilderView;
import com.lqrl.school.web_services.PublishCourseTask;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        StringSetter,
        CoursePublisher,
        LessonOpener,
        NoteBuilderDealer {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private String accessToken = "";
    private CoursesWatchFragment coursesWatchFragment;
    private LessonsWatchFragment lessonsWatchFragment;
    private ExercisesWatchFragment exercisesWatchFragment;
    private NoteBuilderFragment noteBuilderFragment;
    private Toolbar toolbar;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_learn) {
            // Precaution of global variable check
            if(drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START);
            item.setChecked(true);
            return true;
        } else if (id == R.id.nav_create) {
            launchCreatorCoursesWatchFragment();
            if(drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START);
            item.setChecked(true);
            return true;
        }
        else if(id == R.id.nav_builder){
            launchNoteBuilderFragment(NoteBuilderView.Mode.NoteConstructor);
            if(drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START);
            item.setChecked(true);
            return true;
        }
        else if(id == R.id.filter) {
            openContextMenu(navigationView);
            return true;
        } else if(id == R.id.create_course_draft) {
            CreateCourseFragment createCourseFragment = new CreateCourseFragment(this, accessToken);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, createCourseFragment)
                    .commit();
            if (drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    private void launchCreatorCoursesWatchFragment() {
        coursesWatchFragment = new CoursesWatchFragment(this, accessToken);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, coursesWatchFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        Intent intent = getIntent();
        this.accessToken = intent.getStringExtra("access_token");

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        registerForContextMenu(navigationView);

        launchCreatorCoursesWatchFragment();
    }

    public void addToolbarNoteBuilderButtons(NoteBuilderView.Mode mode) {
        if(mode == NoteBuilderView.Mode.NoteConstructor){
            toolbar.inflateMenu(R.menu.note_builder_toolbar);
            toolbar.post(() -> {
                MenuItem addNodeButton = toolbar.getMenu().findItem(R.id.add_node_dialog);
                addNodeButton.setOnMenuItemClickListener(v -> {
                    Toast.makeText(this, "Hello toolbar", Toast.LENGTH_SHORT).show();
                    return true;
                });
            });
        } else if(mode == NoteBuilderView.Mode.AnswerConstructor){
            toolbar.inflateMenu(R.menu.note_answer_toolbar);
            toolbar.post(() -> {
                MenuItem addNodeButton = toolbar.getMenu().findItem(R.id.add_node_dialog);
                addNodeButton.setOnMenuItemClickListener(v -> {
                    // TODO
                    Toast.makeText(this, "Add node", Toast.LENGTH_SHORT).show();
                    return true;
                });
                MenuItem shuffleButton = toolbar.getMenu().findItem(R.id.shuffle_nodes);
                shuffleButton.setOnMenuItemClickListener(v -> {
                    // TODO
                    Toast.makeText(this, "Shuffle nodes", Toast.LENGTH_SHORT).show();
                    return true;
                });
            });
        }
    }

    public void clearToolbarMenu(){
        toolbar.getMenu().clear();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.filter_all){
            return true;
        } else if (id == R.id.filter_complete){
            return true;
        } else if (id == R.id.filter_in_progress){
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void setStringState(String src, boolean state) {
        if(state){
            toast("Course created successfully!");
        } else {
            toast("Course already exists!");
        }
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void requestPublishCourse(Course course) {
        if(course.getState().equals("Draft")){
            new PublishCourseDialogFragment(this, course).show(getSupportFragmentManager(), "PUBLISH_COURSE");
        } else if(course.getState().equals("Published")){
            launchLessonsFragment(course);
        }
    }

    @Override
    public void approveDialogPublish(boolean approve, Course course){
        if(approve){
            new PublishCourseTask(this, course, accessToken).execute();
            launchLessonsFragment(course);
        } else {
            launchLessonsFragment(course);
        }
    }

    private void launchLessonsFragment(Course course) {
        lessonsWatchFragment = new LessonsWatchFragment(this, accessToken, course);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, lessonsWatchFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void launchExercisesFragment(Lesson lesson) {
        exercisesWatchFragment = new ExercisesWatchFragment(this, accessToken, lesson);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, exercisesWatchFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void launchNoteBuilderFragment(NoteBuilderView.Mode mode){
        noteBuilderFragment = new NoteBuilderFragment(this, mode);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, noteBuilderFragment);
        //transaction.addToBackStack(null);
        transaction.commit();
        addToolbarNoteBuilderButtons(mode);
    }

    @Override
    public void sendPublishStatus(boolean status){
        if(status){
            coursesWatchFragment.refreshCourses();
        }
    }

    @Override
    public void requestOpenLesson(Lesson lesson) {
        launchExercisesFragment(lesson);
    }

    @Override
    public void goToBuilder(NoteBuilderView.Mode mode) {
        addToolbarNoteBuilderButtons(mode);
        launchNoteBuilderFragment(mode);
    }

}
