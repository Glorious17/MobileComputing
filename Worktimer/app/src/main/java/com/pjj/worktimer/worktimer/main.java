package com.pjj.worktimer.worktimer;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class main extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter vpa;
    private Dashboard dashboard;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TextView logo = (TextView) findViewById(R.id.Logo);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        vpa = new ViewPagerAdapter(getSupportFragmentManager());
        vpa.addFragment("Statistik", new Statistik());
        dashboard = new Dashboard();
        vpa.addFragment("Dashboard", dashboard);
        vpa.addFragment("Einstellungen", new Einstellungen());
        viewPager.setAdapter(vpa);
        tabLayout.setupWithViewPager(viewPager);
        TabLayout.Tab firstTab = tabLayout.getTabAt(1);
        firstTab.select();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(onclick());
    }

    public View.OnClickListener onclick(){
        return(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                dashboard.addProject();

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
