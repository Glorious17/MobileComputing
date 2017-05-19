package com.pjj.worktimer.worktimer.projectScreen;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Jens on 10.05.2017.
 */

public class ProjectFolder {

    private static ArrayList<Project> projectFolder = new ArrayList<Project>();

    public static void addProject(Project project){
        if(projectFolder.contains(null)){
            projectFolder.set(projectFolder.indexOf(null), project);
        }else{
            projectFolder.add(project);
        }
    }

    public static boolean removeProject(int id){
        Project p = getProjectById(id);
        if(projectFolder.contains(p)){
            projectFolder.set(projectFolder.indexOf(p), null);
            cleanup();

            for (Project i: projectFolder) {
                Log.d("ProjectFolder", "removeProject: " + i);
            }
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

    private static void cleanup(){
        int counter = projectFolder.size()-1;
        Project currentObject = projectFolder.get(counter);

        while(counter >= 0 && currentObject == null){
            projectFolder.remove(counter);
            counter--;
            if(counter >= 0){
                currentObject = projectFolder.get(counter);
            }
        }
    }
}
