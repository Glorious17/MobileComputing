package com.pjj.worktimer.worktimer.projectScreen;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jens on 08.05.2017.
 */

public class Project implements Serializable{

    private ArrayList<String> projectData;
    private int id;

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
    }

    public String getProjectInfo(int infoName){
        if(infoName >= projectData.size()){
            return null;
        }
        return projectData.get(infoName);
    }

    public int getId(){
        return id;
    }
}
