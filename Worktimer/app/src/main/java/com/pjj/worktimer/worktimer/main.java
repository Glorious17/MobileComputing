package com.pjj.worktimer.worktimer;

import android.app.Activity;
import android.content.Intent;
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

import java.util.ArrayList;

public class main extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter vpa;
    private Dashboard dashboard;
    private FloatingActionButton fab;
    TabLayout.Tab firstTab;

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
        firstTab = tabLayout.getTabAt(1);
        firstTab.select();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(onclick());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case (RequestCodes.GENERATE_PROJECT_FROM):
                if(resultCode == RESULT_OK){
                    writeFormData(data.getStringArrayListExtra("generate_project_form"));
                }
                break;
        }
    }

    public void writeFormData(ArrayList<String> formData){
        if(formData != null){
            firstTab.select();
            dashboard.addProject(formData);
        }
    }

    public View.OnClickListener onclick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent generateProjectForm = new Intent(getBaseContext(), Generate_Project_Form.class);
                startActivityForResult(generateProjectForm, RequestCodes.GENERATE_PROJECT_FROM);
            }
        };
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
