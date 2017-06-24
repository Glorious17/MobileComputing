package com.pjj.worktimer.worktimer.projectScreen;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.pjj.worktimer.worktimer.R;
import com.pjj.worktimer.worktimer.helpClasses.HelpFunctions;
import com.pjj.worktimer.worktimer.helpClasses.ViewPagerAdapter;
import com.pjj.worktimer.worktimer.mainScreen.Statistik;
import com.pjj.worktimer.worktimer.projectScreen.fragments.Profil;
import com.pjj.worktimer.worktimer.projectScreen.fragments.Statistik_Project;
import com.pjj.worktimer.worktimer.projectScreen.fragments.Work;

public class ProjectView extends AppCompatActivity {


    private TabLayout tabLayout;

    private TextView hours;
    private TextView minutes;
    private TextView workSoll;
    private TextView workIst;
    private TextView workUmsatz;

    private Button btnStart;
    private Button btnPause;
    private Button btnStop;

    private Project project;
    private AdView mAdView;

    private Statistik_Project statistic_Project;
    Work work;

    /*------------------------------------*/
    /*-----Override super()-functions-----*/
    /*------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        project = ProjectFolder.getProjectById(getIntent().getIntExtra("ProjectId", 0));

        setContentView(R.layout.activity_project_view);
        setSupportActionBar((Toolbar) findViewById(R.id.toolBar_project_view));
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'> " +
                project.getProjectInfo(Project.NAME) + " </font>"));
        tabLayout = (TabLayout) findViewById(R.id.tabLayout_project);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager_project);
        ViewPagerAdapter vpa = new ViewPagerAdapter(getSupportFragmentManager());
        vpa.addFragment("Profil", new Profil());
        work = new Work();
        vpa.addFragment("Work", work);
        statistic_Project = new Statistik_Project();
        vpa.addFragment("Statistik", statistic_Project);
        viewPager.setAdapter(vpa);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(1).select();

        tabLayout.addOnTabSelectedListener(selectedTabListener());

        mAdView = (AdView) findViewById(R.id.adViewProject);

        if(HelpFunctions.getIsPremium()){
            mAdView.setVisibility(View.GONE);
        }else{
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }
    }

    @Override
    public void finish(){
        work.setPauseRunning();
        super.finish();
    }

    @Override
    protected void onDestroy() {
        work.setPauseRunning();
        super.onDestroy();
    }

    /*------------------------------------*/
    /*--------------Listener--------------*/
    /*------------------------------------*/

    private TabLayout.OnTabSelectedListener selectedTabListener(){
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab == tabLayout.getTabAt(2)){
                    statistic_Project.reload();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        };
    }
}