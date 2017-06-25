package com.pjj.worktimer.worktimer.projectScreen.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pjj.worktimer.worktimer.R;
import com.pjj.worktimer.worktimer.helpClasses.HelpFunctions;
import com.pjj.worktimer.worktimer.helpClasses.Save;
import com.pjj.worktimer.worktimer.projectScreen.Project;
import com.pjj.worktimer.worktimer.projectScreen.ProjectFolder;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class Work extends Fragment {

    private TextView hours;
    private TextView minutes;
    private TextView txtviewSeconds;

    private TextView date;

    private TextView workSoll;
    private TextView workIst;
    private TextView workUmsatz;

    private EditText input;

    private Button btnStart;
    private Button btnPause;
    private Button btnStop;

    private boolean standby;
    private boolean isPause;
    private boolean pauseRunning;

    private long standByMillies;

    private Project project;

    private Timer timer;

    /*------------------------------------*/
    /*-----Override super()-functions-----*/
    /*------------------------------------*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_work, container, false);

        project = ProjectFolder.getProjectById(getActivity().getIntent().getIntExtra("ProjectId", 0));

        hours = (TextView) view.findViewById(R.id.workHours);
        minutes = (TextView) view.findViewById(R.id.workMinutes);
        txtviewSeconds = (TextView) view.findViewById(R.id.workSeconds);

        workSoll = (TextView) view.findViewById(R.id.workSOLL);
        workIst = (TextView) view.findViewById(R.id.workIST);
        //workUmsatz = (TextView) view.findViewById(R.id.workUmsatz);

        date = (TextView) view.findViewById(R.id.workDate);

        btnStart = (Button) view.findViewById(R.id.workBtnStart);
        btnPause = (Button) view.findViewById(R.id.workBtnPause);
        btnStop = (Button) view.findViewById(R.id.workBtnStop);

        btnStart.setOnClickListener(startTimer());
        btnPause.setOnClickListener(pauseTimer());
        btnStop.setOnClickListener(stopTimer());

        if(readProjectWorktime()){
            isPause = true;
            btnStart.setVisibility(View.GONE);
            btnPause.setVisibility(View.VISIBLE);
            btnStop.setVisibility(View.VISIBLE);
            timer = new Timer(true);
            timer.start();
            btnPause.setText("Fortsetzen");
        }else{
            isPause = false;
            btnStart.setVisibility(View.VISIBLE);
            btnPause.setVisibility(View.GONE);
            btnStop.setVisibility(View.GONE);
            timer = new Timer(false);
        }

        pauseRunning = false;
        standby = false;

        return view;
    }

    @Override
    public void onResume() {
        if(standby){
            long duration = System.currentTimeMillis() - standByMillies;

            timer.addSeconds(Math.round(duration/1000));

            standby = false;
            timer.resumeThread();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if(!pauseRunning){
            timer.pause();
            standByMillies = System.currentTimeMillis();
            standby = true;
        }
        super.onPause();
    }

    /*------------------------------------*/
    /*---------Program-Functions----------*/
    /*------------------------------------*/

    private boolean readProjectWorktime(){

        Object[] time = project.getWorkTime();

        txtviewSeconds.setText(setTimes((int)time[0]));
        minutes.setText(setTimes((int)time[1]));
        hours.setText(setTimes((int)time[2]));

        String dateTime = (String) time[3];

        if(dateTime == null){
            date.setText("----");
        }else{

            date.setText("Gestartet am " + (String)time[3]);
            return true;
        }

        return false;
    }

    private String getDate(boolean draw){
        Calendar c = Calendar.getInstance();

        String text = "" + setTimes(c.get(Calendar.DAY_OF_MONTH)) + ".";
        text += setTimes(c.get(Calendar.MONTH)+1) + ".";
        text += c.get(Calendar.YEAR);

        int am_orPM = c.get(Calendar.AM_PM);

        if(am_orPM == 1){
            text += " um " + (c.get(Calendar.HOUR)+12) + ":";
        }else{
            text += " um " + setTimes(c.get(Calendar.HOUR)) + ":";
        }
        text += setTimes(c.get(Calendar.MINUTE));

        if(draw)
            date.setText("Gestartet am " + text);

        return text;
    }

    private void refresh(){
        minutes.setText("00");
        hours.setText("00");
        txtviewSeconds.setText("00");
        date.setText("----");
    }

    /*------------------------------------*/
    /*----------GETTER & SETTER-----------*/
    /*------------------------------------*/

    private void setIst(String text){
        getActivity().runOnUiThread(new UiThreadAdapter(workIst, text));
    }

    private void setHours(String text){
        getActivity().runOnUiThread(new UiThreadAdapter(hours, text));
    }

    private void setMinutes(String text){
        getActivity().runOnUiThread(new UiThreadAdapter(minutes, text));
    }

    private void setTxtviewSeconds(String text){
        getActivity().runOnUiThread(new UiThreadAdapter(txtviewSeconds, text));
    }

    public void setPauseRunning(){
        pauseRunning = true;
    }

    /*------------------------------------*/
    /*--------------Listener--------------*/
    /*------------------------------------*/

    private View.OnClickListener startTimer(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                timer = new Timer(false);
                timer.start();
                btnStart.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.VISIBLE);
                project.setDateOfStart(getDate(true));
            }
        };
    }

    private View.OnClickListener pauseTimer(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!isPause){
                    timer.pause();
                    btnPause.setText("Fortsetzen");
                    isPause = true;
                }else{
                    timer.resumeThread();
                    btnPause.setText("Pause");
                    isPause = false;
                }
                Save.saveProjects();

            }
        };
    }

    private View.OnClickListener stopTimer(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                pauseRunning = false;
                timer.stopTimer();
                btnStart.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.GONE);
                btnStop.setVisibility(View.GONE);
                if(!minutes.getText().toString().equals("00") || !hours.getText().toString().equals("00")){
                    setTitle();
                }
                refresh();
            }
        };
    }

    /*------------------------------------*/
    /*-----------Help functions-----------*/
    /*------------------------------------*/

    private String setTimes(int time){
        if(time < 10){
            return ("0" + time);
        }else{
            return ("" + time);
        }
    }

    private void setTitle(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Was hast du gemacht?");

        input = new EditText(getContext());
        input.setHint("Gib einen Titel ein");
        input.setSingleLine(true);
        builder.setView(input);

        builder.setPositiveButton("weiter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                timer.onTitleSetListener();
            }
        });

        builder.show();
    }

    /*------------------------------------*/
    /*-----------Backgroundtasks----------*/
    /*------------------------------------*/

    class Timer extends Thread {

        private boolean running;
        private boolean pause;

        private int secondsCount;
        private int minutesCount;
        private int hoursCount;

        public Timer(boolean pause){
            running = true;
            this.pause = pause;
        }

        @Override
        public void run() {

            long workMillis;
            long currentMillis = System.currentTimeMillis();

            secondsCount = (int) project.getWorkTime()[0];
            minutesCount = (int) project.getWorkTime()[1];
            hoursCount = (int) project.getWorkTime()[2];

            while(running && !pauseRunning){
                if(!pause){

                    workMillis = System.currentTimeMillis() - currentMillis;

                    if(secondsCount >= 60){

                        secondsCount -= 60;
                        setTxtviewSeconds("00");

                        if(minutesCount >= 59){

                            setMinutes("00");
                            minutesCount = 0;
                            hoursCount++;

                            setHours(setTimes(hoursCount));

                        }else{

                            minutesCount++;

                            setMinutes(setTimes(minutesCount));
                        }
                        project.setWorkTime(secondsCount, minutesCount, hoursCount);
                        setIst("" + project.getIst());
                        Save.saveProjects();
                    }


                    if(workMillis/1000 >= 1){
                        currentMillis = System.currentTimeMillis();
                        secondsCount++;
                        setTxtviewSeconds(setTimes(secondsCount));
                    }

                }else{

                    project.setWorkTime(secondsCount, minutesCount, hoursCount);

                }
            }

            // END OF LOOP
            if(pauseRunning){
                project.setWorkTime(secondsCount, minutesCount, hoursCount);
                Save.saveProjects();
            }else{
                project.resetValues();
            }
        }

        public void addSeconds(int count){
            secondsCount += count;
        }

        public void pause(){
            pause = true;
        }

        public void resumeThread(){
            pause = false;
        }

        public void stopTimer(){
            running = false;
        }

        public void onTitleSetListener(){
            project.updateHistory(input.getText().toString(), minutesCount, hoursCount, getDate(false));
            input.setText("");
            project.resetValues();
            Save.saveProjects();
        }
    }

    class UiThreadAdapter implements Runnable{

        private TextView change;
        private String text;

        public UiThreadAdapter(TextView view, String text){
            change = view;
            this.text = text;
        }

        @Override
        public void run() {
            change.setText(text);
        }
    }

}
