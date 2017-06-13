package com.pjj.worktimer.worktimer.projectScreen.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pjj.worktimer.worktimer.R;
import com.pjj.worktimer.worktimer.helpClasses.Save;
import com.pjj.worktimer.worktimer.projectScreen.Project;


/**
 * A simple {@link Fragment} subclass.
 */
public class Work extends Fragment {

    private TextView hours;
    private TextView minutes;
    private TextView workSoll;
    private TextView workIst;
    private TextView workUmsatz;

    private Button btnStart;
    private Button btnPause;
    private Button btnStop;

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

        btnStart = (Button) view.findViewById(R.id.workBtnStart);
        btnPause = (Button) view.findViewById(R.id.workBtnPause);
        btnStop = (Button) view.findViewById(R.id.workBtnStop);

        btnStart.setOnClickListener(startTimer());
        btnPause.setOnClickListener(pauseTimer());
        btnStop.setOnClickListener(stopTimer());

        btnPause.setEnabled(false);
        btnStop.setEnabled(false);

        readProjectWorktime();

        isPause = false;

        return view;
    }

    /*------------------------------------*/
    /*---------Program-Functions----------*/
    /*------------------------------------*/

    public void readProjectWorktime(){

        int[] time = project.getWorkTime();

        if(time[0] < 10){
            minutes.setText("0" + time[0]);
        }else{
            minutes.setText("" + time[0]);
        }

        if(time[1] < 10){
            hours.setText("0" + time[1]);
        }else{
            hours.setText("" + time[1]);
        }
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

    /*------------------------------------*/
    /*--------------Listener--------------*/
    /*------------------------------------*/

    private View.OnClickListener startTimer(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                timer = new Timer();
                timer.start();
                btnStart.setEnabled(false);
                btnPause.setEnabled(true);
                btnStop.setEnabled(true);
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
                btnStart.setEnabled(true);
                btnPause.setEnabled(false);
                btnStop.setEnabled(false);
            }
        };
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

            int minutesCount = project.getWorkTime()[0];
            int hoursCount = project.getWorkTime()[1];

            while(running){
                if(!pause){

                    workMillis = System.currentTimeMillis() - currentMillis;

                    if(workMillis % 1000 == 0){
                        Log.d("Thread", "run: " + workMillis);
                    }

                    if(workMillis >= 60000){

                        currentMillis = System.currentTimeMillis();

                        if(minutesCount >= 59){

                            setMinutes("00");
                            minutesCount = 0;
                            hoursCount++;

                            if(hoursCount < 10){
                                setHours("0" + hoursCount);
                            }else{
                                setHours("" + hoursCount);
                            }

                        }else{

                            minutesCount++;

                            if(minutesCount < 10){
                                setMinutes("0" + minutesCount);
                            }else{
                                setMinutes("" + minutesCount);
                            }
                        }
                        project.setWorkTime(minutesCount, hoursCount);
                        Save.saveProjects(getContext());
                    }
                }
            }
            project.setWorkTime(minutesCount, hoursCount);
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
