package com.pjj.worktimer.worktimer;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Jens on 10.05.2017.
 */

public class Save {

    private static FileOutputStream fos;
    private static ObjectOutputStream oos;

    private static FileInputStream fis;
    private static ObjectInputStream ois;

    public static void saveProjects(Context c) throws IOException {

        ArrayList<Project> projects = ProjectFolder.getProjectFolder();

        File file = new File(c.getFilesDir(), "project.wk");
        if(file.exists()){
            file.delete();
        }
        file.createNewFile();

        fos = new FileOutputStream(file);
        oos = new ObjectOutputStream(fos);

        oos.writeInt(projects.size());

        for (Project p : projects) {

            oos.writeObject(p);

        }

        fos.close();
    }

    public static ArrayList<Project> readProjects(Context c) throws IOException, ClassNotFoundException {

        File file = new File(c.getFilesDir(), "project.wk");

        if(c.getFilesDir().exists() && file.exists()){
            ArrayList<Project> output = new ArrayList<Project>();
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);

            int size = ois.readInt();

            for(int i = 0; i < size; i++){
                output.add((Project) ois.readObject());
            }
            fis.close();

            return output;
        }
        c.getFilesDir().mkdirs();
        return null;
    }

    public static void saveOrder(Context c, ArrayList<Integer> ids) throws IOException {

        File file = new File(c.getFilesDir(), "order.wk");
        if(file.exists()){
            file.delete();
        }
        file.createNewFile();

        fos = new FileOutputStream(file);
        oos = new ObjectOutputStream(fos);

        oos.writeInt(ids.size());

        for (int i : ids) {

            oos.writeInt(i);

            Log.d("Save", "SaveOrder: " + i);

        }

        fos.close();
    }

    public static ArrayList<Integer> readOrder(Context c) throws IOException, ClassNotFoundException {

        File file = new File(c.getFilesDir(), "order.wk");

        if(c.getFilesDir().exists() && file.exists()){
            ArrayList<Integer> output = new ArrayList<Integer>();
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);

            int size = ois.readInt();

            for(int i = 0; i < size; i++){
                output.add(ois.readInt());
            }
            fis.close();

            return output;
        }
        c.getFilesDir().mkdirs();
        return null;
    }
}
