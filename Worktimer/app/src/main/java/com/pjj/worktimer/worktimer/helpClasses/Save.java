package com.pjj.worktimer.worktimer.helpClasses;

import android.content.Context;

import com.pjj.worktimer.worktimer.projectScreen.Project;
import com.pjj.worktimer.worktimer.projectScreen.ProjectFolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Jens on 10.05.2017.
 */

public class Save {

    private static FileOutputStream fos;
    private static ObjectOutputStream oos;

    private static FileInputStream fis;
    private static ObjectInputStream ois;

    public static void saveProjects(Context c) {

        ArrayList<Project> projects = ProjectFolder.getProjectFolder();

        File file = new File(c.getFilesDir(), "project.wk");
        if(file.exists()){
            file.delete();
        }

        try {
            file.createNewFile();

            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);

            oos.writeInt(projects.size());

            for (Project p : projects) {

                oos.writeObject(p);

            }

            oos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<Project> readProjects(Context c) {

        File file = new File(c.getFilesDir(), "project.wk");

        if(c.getFilesDir().exists() && file.exists()){
            ArrayList<Project> output = new ArrayList<Project>();
            try {
                fis = new FileInputStream(file);
                ois = new ObjectInputStream(fis);

                int size = ois.readInt();

                for(int i = 0; i < size; i++){
                    output.add((Project) ois.readObject());
                }
                fis.close();

                return output;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        c.getFilesDir().mkdirs();
        return null;
    }

    public static void saveOrder(Context c, ArrayList<Integer> ids) {

        File file = new File(c.getFilesDir(), "order.wk");
        if(file.exists()){
            file.delete();
        }

        try {
            file.createNewFile();
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);

            oos.writeInt(ids.size());

            for (int i : ids) {

                oos.writeInt(i);

            }
            oos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Integer> readOrder(Context c) {

        File file = new File(c.getFilesDir(), "order.wk");

        if(c.getFilesDir().exists() && file.exists()){
            ArrayList<Integer> output = new ArrayList<Integer>();
            try {
                fis = new FileInputStream(file);
                ois = new ObjectInputStream(fis);
                int size = ois.readInt();

                for(int i = 0; i < size; i++){
                    output.add(ois.readInt());
                }
                fis.close();

                return output;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        c.getFilesDir().mkdirs();
        return null;
    }
}
