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

import com.google.android.material.navigation.NavigationView;
import com.lqrl.school.dialogs.PublishCourseDialogFragment;
import com.lqrl.school.entities.Course;
import com.lqrl.school.fragments.CreatorCoursesWatchFragment;
import com.lqrl.school.fragments.CreateCourseFragment;
import com.lqrl.school.fragments.LessonsWatchFragment;
import com.lqrl.school.interfaces.CoursePublisher;
import com.lqrl.school.interfaces.StringSetter;
import com.lqrl.school.web_services.PublishCourseTask;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        StringSetter,
        CoursePublisher {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private String accessToken = "";
    private CreatorCoursesWatchFragment creatorCoursesWatchFragment;
    private LessonsWatchFragment lessonsWatchFragment;


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        } else if(id == R.id.filter) {
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
        creatorCoursesWatchFragment = new CreatorCoursesWatchFragment(this, accessToken);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, creatorCoursesWatchFragment)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        Intent intent = getIntent();
        this.accessToken = intent.getStringExtra("access_token");

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        registerForContextMenu(navigationView);

        launchCreatorCoursesWatchFragment();

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
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, lessonsWatchFragment)
                .commit();
    }

    @Override
    public void sendPublishStatus(boolean status){
        if(status){
            creatorCoursesWatchFragment.refreshCourses();
        }
    }
}
