package com.pjj.worktimer.worktimer;

import java.util.ArrayList;

/**
 * Created by Jens on 10.05.2017.
 */

public class ProjectFolder {

    private static ArrayList<Project> projectFolder = new ArrayList<Project>();

    public static void addProject(Project project){
        projectFolder.add(project);
    }

    public static Project getProjectById(int id){
        return projectFolder.get(id);
    }

    public static int getProjectId(Project project){
        return projectFolder.indexOf(project);
    }

    public void setProjectFolder(ArrayList<Project> projectFolder){
        ProjectFolder.projectFolder = projectFolder;
    }
}
