package com.pjj.worktimer.worktimer.projectScreen;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class Project implements Serializable{

    private ArrayList<String> projectData;
    private ArrayList<String[]> history;

    private int databaseID;
    private int id;

    private int minutes;
    private int hours;
    private int seconds;

    private float soll;
    private float ist;
    private float value;

    private String dateOfStart;

    public static final int NAME = 0;
    public static final int FIRMA = 1;
    public static final int STRASSE = 2;
    public static final int NR = 3;
    public static final int LAND = 4;


    public Project(ArrayList<String> projectData){

        this.projectData = projectData;
        this.history = new ArrayList<String[]>();
        ProjectFolder.addProject(this);
        id = ProjectFolder.getProjectId(this);

        seconds = 0;
        minutes = 0;
        hours = 0;

        soll = 0;
        ist = 0;
        value = 0;
    }

    /*------------------------------------*/
    /*---------Program-Functions----------*/
    /*------------------------------------*/


    public void updateIst(){
        value += 1/60;
        if(value >= 0.1){
            ist += 0.1;
            value = 0;
        }
    }

    public void updateHistory(String title, int minutes, int hours, String endDate){
        if(title.equals("")){
            title = dateOfStart;
        }
        String[] data = {title, "" + hours, "" + minutes, dateOfStart, endDate};
        history.add(data);
        resetValues();
    }

    public void resetValues(){
        this.seconds = 0;
        this.minutes = 0;
        this.hours = 0;
        dateOfStart = null;
    }

    /*------------------------------------*/
    /*----------GETTER & SETTER-----------*/
    /*------------------------------------*/

    public int getId(){
        return id;
    }

    public int getDatabaseID(){
        return databaseID;
    }

    public float getIst(){ return ist; }

    public ArrayList<String[]> getHistory(){ return history; }

    public String getProjectInfo(int infoName){
        if(infoName >= projectData.size()){
            return null;
        }
        return projectData.get(infoName);
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

    public void setWorkTime(int seconds, int minutes, int hours) {
        this.minutes = minutes;
        this.hours = hours;
        this.seconds = seconds;
        updateIst();
    }

    public void setDateOfStart(String date){
        this.dateOfStart = date;
    }

    public void setProjectInfo(int infoName, String newInfo){
        if(infoName >= projectData.size()){
            return;
        }
        projectData.set(infoName, newInfo);
    }

}
