package com.pjj.worktimer.worktimer.projectScreen;

import java.io.Serializable;
import java.util.ArrayList;

public class Project implements Serializable{

    private ArrayList<String> projectData;

    private int databaseID;
    private int id;

    private int minutes;
    private int hours;
    private int seconds;

    private String dateOfStart;

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

        seconds = 0;
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

    public Object[] getWorkTime() {
        Object[] values = new Object[4];
        values[0] = seconds;
        values[1] = minutes;
        values[2] = hours;
        values[3] = dateOfStart;
        return values;
    }

    public void setDatabaseID(int databaseID) { this.databaseID = databaseID; }

    public void setWorkTime(int seconds, int minutes, int hours, String date) {
        this.minutes = minutes;
        this.hours = hours;
        this.seconds = seconds;
        this.dateOfStart = date;
    }
}
