package com.pjj.worktimer.worktimer;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class ProjectView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_view);
        setSupportActionBar((Toolbar) findViewById(R.id.toolBar_project_view));
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