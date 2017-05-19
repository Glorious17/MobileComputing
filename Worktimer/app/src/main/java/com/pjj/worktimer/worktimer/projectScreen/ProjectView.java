package com.pjj.worktimer.worktimer.projectScreen;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.pjj.worktimer.worktimer.R;
import com.pjj.worktimer.worktimer.helpClasses.ViewPagerAdapter;
import com.pjj.worktimer.worktimer.projectScreen.fragments.Export;
import com.pjj.worktimer.worktimer.projectScreen.fragments.Profil;
import com.pjj.worktimer.worktimer.projectScreen.fragments.Statistik_Project;
import com.pjj.worktimer.worktimer.projectScreen.fragments.Work;

public class ProjectView extends AppCompatActivity {

    private Project project;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        project = ProjectFolder.getProjectById(getIntent().getIntExtra("ProjectId", 0));

        setContentView(R.layout.activity_project_view);
        setSupportActionBar((Toolbar) findViewById(R.id.toolBar_project_view));
        getSupportActionBar().setTitle(project.getProjectInfo(Project.NAME));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout_project);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager_project);
        ViewPagerAdapter vpa = new ViewPagerAdapter(getSupportFragmentManager());
        vpa.addFragment("Profil", new Profil());
        vpa.addFragment("Work", new Work());
        vpa.addFragment("Statistik", new Statistik_Project());
        vpa.addFragment("Export", new Export());
        viewPager.setAdapter(vpa);
        tabLayout.setupWithViewPager(viewPager);
    }

}