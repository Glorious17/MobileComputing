package com.pjj.worktimer.worktimer;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class main extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter vpa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        vpa = new ViewPagerAdapter(getSupportFragmentManager());
        vpa.addFragment("Statistik", new Statistik());
        vpa.addFragment("Dashboard", new Dashboard());
        vpa.addFragment("Einstellungen", new Einstellungen());
        viewPager.setAdapter(vpa);
        tabLayout.setupWithViewPager(viewPager);
        TabLayout.Tab firstTab = tabLayout.getTabAt(1);
        firstTab.select();
    }
}
