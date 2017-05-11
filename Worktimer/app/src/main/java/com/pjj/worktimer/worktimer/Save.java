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

    private FileOutputStream fos;
    private ObjectOutputStream oos;

    private FileInputStream fis;
    private ObjectInputStream ois;

    public void saveProjects(ArrayList<Project> projects, Context c) throws IOException {

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

    public ArrayList<Project> readProjects(Context c) throws IOException, ClassNotFoundException {

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

}
