package com.pjj.worktimer.worktimer.projectScreen.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pjj.worktimer.worktimer.R;
import com.pjj.worktimer.worktimer.helpClasses.Save;
import com.pjj.worktimer.worktimer.projectScreen.Project;

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

    private Button btnStart;
    private Button btnPause;
    private Button btnStop;

    private String startDateTime;

    private boolean isPause;

    private Project project;

    private Timer timer;

    /*------------------------------------*/
    /*-----Override super()-functions-----*/
    /*------------------------------------*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_work, container, false);

        hours = (TextView) view.findViewById(R.id.workHours);
        minutes = (TextView) view.findViewById(R.id.workMinutes);
        txtviewSeconds = (TextView) view.findViewById(R.id.workSeconds);

        date = (TextView) view.findViewById(R.id.workDate);

        btnStart = (Button) view.findViewById(R.id.workBtnStart);
        btnPause = (Button) view.findViewById(R.id.workBtnPause);
        btnStop = (Button) view.findViewById(R.id.workBtnStop);

        btnStart.setOnClickListener(startTimer());
        btnPause.setOnClickListener(pauseTimer());
        btnStop.setOnClickListener(stopTimer());

        btnPause.setVisibility(View.GONE);
        btnStop.setVisibility(View.GONE);

        readProjectWorktime();

        isPause = false;

        return view;
    }

    /*------------------------------------*/
    /*---------Program-Functions----------*/
    /*------------------------------------*/

    private void readProjectWorktime(){

        Object[] time = project.getWorkTime();

        txtviewSeconds.setText(setTimes((int)time[0]));
        minutes.setText(setTimes((int)time[1]));
        hours.setText(setTimes((int)time[2]));

        String dateTime = (String) time[3];

        if(dateTime == null){
            date.setText("----");
        }else{
            date.setText("Gestartet am " + (String)time[3]);
        }

    }

    private String setDate(){
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


        date.setText("Gestartet am " + text);
        return text;
    }

    /*------------------------------------*/
    /*----------GETTER & SETTER-----------*/
    /*------------------------------------*/

    public void setProject(Project p){
        project = p;
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

    /*------------------------------------*/
    /*--------------Listener--------------*/
    /*------------------------------------*/

    private View.OnClickListener startTimer(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                timer = new Timer();
                timer.start();
                btnStart.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.VISIBLE);
                startDateTime = setDate();
            }
        };
    }

    private View.OnClickListener pauseTimer(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Save.saveProjects(getContext());
                if(!isPause){
                    timer.pause();
                    btnPause.setText("Fortsetzen");
                    isPause = true;
                }else{
                    timer.resumeThread();
                    btnPause.setText("Pause");
                    isPause = false;
                }
            }
        };
    }

    private View.OnClickListener stopTimer(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Save.saveProjects(getContext());
                timer.stopTimer();
                btnStart.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.GONE);
                btnStop.setVisibility(View.GONE);
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


    /*------------------------------------*/
    /*-----------Backgroundtasks----------*/
    /*------------------------------------*/

    class Timer extends Thread {

        private boolean running;
        private boolean pause;

        public Timer(){
            running = true;
            pause = false;
        }

        @Override
        public void run() {

            long workMillis;
            long currentMillis = System.currentTimeMillis();

            int minutesCount = (int) project.getWorkTime()[1];
            int hoursCount = (int) project.getWorkTime()[2];
            int seconds = 0;

            while(running){
                if(!pause){

                    workMillis = System.currentTimeMillis() - currentMillis;

                    if(seconds >= 60){

                        seconds = 0;
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
                        project.setWorkTime(seconds, minutesCount, hoursCount, startDateTime);
                        Save.saveProjects(getContext());
                    }


                    if(workMillis/1000 >= 1){
                        currentMillis = System.currentTimeMillis();
                        seconds++;
                        setTxtviewSeconds(setTimes(seconds));
                    }

                }
            }

            // END OF LOOP
            project.setWorkTime(seconds, minutesCount, hoursCount, startDateTime);
            Save.saveProjects(getContext());
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
