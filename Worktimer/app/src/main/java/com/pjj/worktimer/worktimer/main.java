package com.pjj.worktimer.worktimer;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.pjj.worktimer.worktimer.form.Generate_Project_Form;
import com.pjj.worktimer.worktimer.helpClasses.HelpFunctions;
import com.pjj.worktimer.worktimer.helpClasses.RequestCodes;
import com.pjj.worktimer.worktimer.helpClasses.Save;
import com.pjj.worktimer.worktimer.helpClasses.ViewPagerAdapter;
import com.pjj.worktimer.worktimer.form.Login;
import com.pjj.worktimer.worktimer.mainScreen.Dashboard;
import com.pjj.worktimer.worktimer.mainScreen.Einstellungen;
import com.pjj.worktimer.worktimer.mainScreen.Statistik;

import java.io.IOException;

public class main extends AppCompatActivity {

    private Dashboard dashboard;
    private FloatingActionButton fab;
    private TextView login;
    private TabLayout tabLayout;
    private TabLayout.Tab firstTab;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private ViewPagerAdapter vpa;

    /*------------------------------------*/
    /*-----Override super()-functions-----*/
    /*------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TextView logo = (TextView) findViewById(R.id.Logo);
        setContentView(R.layout.activity_main);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        //Tablayout um zwischen den einzelnen Fragmenten (Einstellungen, Dashboard, und Statistik) gewechselt werden kann
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Der ViewPager wird für die Darstellung der Fragmente benötigt
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        //Der ViewPager-Adapter ist notwendig, da mehrere Fragmente generiert werden.
        vpa = new ViewPagerAdapter(getSupportFragmentManager());

        //Jedes Fragment wird einzeln dem vpa hinzugefügt
        vpa.addFragment("Statistik", new Statistik());

        //Dashboard besitzt, anders als die anderen beiden Fragmente, keine Layout-XML. Dieses Fragment wurde per Hand gecoded.
        //Um später auf Funktionen zugreifen zu können wird hier eine globale Instanz von Dashboard erzeugt.
        dashboard = new Dashboard();
        vpa.addFragment("Dashboard", dashboard);
        vpa.addFragment("Einstellungen", new Einstellungen());

        //Das TabLayout wird mit dem ViewPager-Adapter initialisiert
        viewPager.setAdapter(vpa);
        tabLayout.setupWithViewPager(viewPager);

        //Der zweite Tab im tabLayout soll nach dem Start der App direkt ausgewählt sein
        firstTab = tabLayout.getTabAt(1);
        firstTab.select();

        //Ein FloatingActionButton wird für das Hinzufügen von Projekten verwendet
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(onClickForm());

        //Ein Listener für die TextView-Resource "Login"
        login = (TextView) findViewById(R.id.Login);
        login.setOnClickListener(onClickLogin());
    }


    /*
    Jedes Mal, wenn die Activity gestoppt wird, sollen alle Projekte gespeichert werden.
    Die Methoden zum Speichern wurden in der Klasse "Save()" statisch angelegt.
     */
    @Override
    protected void onStop() {
        super.onStop();
        Save.saveProjects(getBaseContext());
        dashboard.saveOrder();
    }

    /*
    Sobald der FloatingActionButton geklickt wird, startet die Activity Generate_Project_Form.
    Beim finish() dieser Activity wird ein "Result" generiert, dass hier abgefangen wird.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case (RequestCodes.GENERATE_PROJECT_FORM):
                if(resultCode == RESULT_OK){
                    writeFormData(data.getIntExtra("generate_project", 0));
                }
                break;
            case (RequestCodes.LOGIN):
                if(resultCode == RESULT_OK){
                    login.setVisibility(View.GONE);
                }
        }
    }

    /*------------------------------------*/
    /*--------------Listener--------------*/
    /*------------------------------------*/

    /*
    Der OnClickListener für den FloatingActionButton. Es wird die Methode startActivityForResult() genutzt,
    um die Daten aus dem Formular zu übergeben.
     */
    public View.OnClickListener onClickForm(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent generateProjectForm = new Intent(getBaseContext(), Generate_Project_Form.class);
                startActivityForResult(generateProjectForm, RequestCodes.GENERATE_PROJECT_FORM);
            }
        };
    }

    public View.OnClickListener onClickLogin(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(getBaseContext(), Login.class);
                startActivityForResult(login, RequestCodes.LOGIN);
            }
        };
    }

    /*------------------------------------*/
    /*-----------Help functions-----------*/
    /*------------------------------------*/

    public void writeFormData(int projectId){
        firstTab.select();
        dashboard.addProject(projectId);
    }
}
