package com.pjj.worktimer.worktimer.projectScreen;

import android.util.Log;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class Project implements Serializable{

    private ArrayList<String> projectData;

    private int databaseID;
    private int id;

    private int minutes;
    private int hours;

    public static final int NAME = 0;
    public static final int FIRMA = 1;
    public static final int STRASSE = 2;
    public static final int NR = 3;
    public static final int LAND = 4;
    public static final int UST_IDNR = 5;
    public static final int STEUERNUMMER = 6;


    public Project(ArrayList<String> projectData){

        this.projectData = projectData;
        ProjectFolder.addProject(this);
        id = ProjectFolder.getProjectId(this);

        minutes = 0;
        hours = 0;
    }

    /*------------------------------------*/
    /*----------GETTER & SETTER-----------*/
    /*------------------------------------*/

    public String getProjectInfo(int infoName){
        if(infoName >= projectData.size()){
            return null;
        }
        return projectData.get(infoName);
    }

    public void setProjectInfo(int infoName, String newInfo){
        if(infoName >= projectData.size()){
            return;
        }
        projectData.set(infoName, newInfo);
    }

    public int getId(){
        return id;
    }

    public int getDatabaseID(){
        return databaseID;
    }

    public int[] getWorkTime() {
        int[] values = new int[2];
        values[0] = minutes;
        values[1] = hours;
        return values;
    }

    public void setDatabaseID(int databaseID) { this.databaseID = databaseID; }

    public void setWorkTime(int minutes, int hours) {
        this.minutes = minutes;
        this.hours = hours;
    }
}
