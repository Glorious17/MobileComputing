package com.pjj.worktimer.worktimer;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Jens on 10.05.2017.
 */

public class ProjectFolder {

    private static ArrayList<Project> projectFolder = new ArrayList<Project>();

    public static void addProject(Project project){
        if(projectFolder.contains(null)){
            projectFolder.add(projectFolder.indexOf(null), project);
            projectFolder.remove(projectFolder.indexOf(null));
        }else{
            projectFolder.add(project);
        }
    }

    public static boolean removeProject(Project p){
        if(projectFolder.contains(p)){
            projectFolder.set(projectFolder.indexOf(p), null);
            return true;
        }else {
            return false;
        }
    }

    public static Project getProjectById(int id){
        return projectFolder.get(id);
    }

    public static int getProjectId(Project project){
        return projectFolder.indexOf(project);
    }

    public static void setProjectFolder(ArrayList<Project> projectFolder){
        ProjectFolder.projectFolder = projectFolder;
    }

    public static ArrayList getProjectFolder(){
        return projectFolder;
    }
}
