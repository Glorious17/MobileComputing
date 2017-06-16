package com.pjj.worktimer.worktimer.projectScreen.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pjj.worktimer.worktimer.R;
import com.pjj.worktimer.worktimer.helpClasses.HelpFunctions;
import com.pjj.worktimer.worktimer.projectScreen.Project;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Statistik_Project extends Fragment {

    private RelativeLayout history;

    private Project project;

    private int itemCount;

    /*------------------------------------*/
    /*-----Override super()-functions-----*/
    /*------------------------------------*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistik__project, container, false);

        history = (RelativeLayout) view.findViewById(R.id.statisticContent);

        itemCount = 0;

        return view;
    }

    /*------------------------------------*/
    /*---------Program-Functions----------*/
    /*------------------------------------*/

    public boolean reload(){

        ArrayList<String[]> statisticHistory = project.getHistory();

        if(statisticHistory.size() > itemCount){

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            lp.setMargins(0,0,0,HelpFunctions.dp(getContext(), 20));

            RelativeLayout newItem;
            RelativeLayout oldItem = null;
            String[] dataArray;

            int itemsToAdd = statisticHistory.size() - itemCount;

            while (itemsToAdd > 0){

                dataArray = statisticHistory.get(statisticHistory.size()-itemsToAdd);
                newItem = generateListItem(dataArray[0], dataArray[1], dataArray[2],
                        dataArray[3], dataArray[4]);

                if(itemCount == 0){
                    newItem.setLayoutParams(lp);
                    history.addView(newItem);
                }else{
                    lp.addRule(RelativeLayout.BELOW, oldItem.getId());
                    newItem.setLayoutParams(lp);
                    history.addView(newItem);
                }

                oldItem = newItem;
                itemsToAdd--;

            }

            return true;

        }

        return false;
    }

    /*------------------------------------*/
    /*----------GETTER & SETTER-----------*/
    /*------------------------------------*/

    public void setProject(Project p){ project = p; }

    /*------------------------------------*/
    /*---------Generate - Layout----------*/
    /*------------------------------------*/

    private RelativeLayout generateListItem(String txtTitel, String txtHours, String txtMinutes, String txtFrom, String txtTo){

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        RelativeLayout item = new RelativeLayout(getContext());
        item.setId(View.generateViewId());
        item.setLayoutParams(lp);

        TextView titel = new TextView(getContext());
        titel.setId(View.generateViewId());
        lp.setMargins(HelpFunctions.dp(getContext(), 20), HelpFunctions.dp(getContext(), 20), 0, 0);
        titel.setLayoutParams(lp);
        titel.setText(txtTitel);
        item.addView(titel);

        TextView hours = new TextView(getContext());
        hours.setId(View.generateViewId());
        lp.addRule(RelativeLayout.BELOW, titel.getId());
        hours.setLayoutParams(lp);
        hours.setText("Stunden: " + txtHours);
        item.addView(hours);

        TextView minutes = new TextView(getContext());
        minutes.setId(View.generateViewId());
        lp.addRule(RelativeLayout.RIGHT_OF, hours.getId());
        minutes.setLayoutParams(lp);
        minutes.setText("Minuten: " + txtMinutes);
        item.addView(minutes);

        TextView from = new TextView(getContext());
        from.setId(View.generateViewId());
        lp.removeRule(RelativeLayout.RIGHT_OF);
        lp.addRule(RelativeLayout.BELOW, hours.getId());
        from.setLayoutParams(lp);
        from.setText("Von: " + txtFrom);
        item.addView(from);

        TextView to = new TextView(getContext());
        to.setId(View.generateViewId());
        lp.addRule(RelativeLayout.BELOW, from.getId());
        to.setLayoutParams(lp);
        to.setText("Bis: " + txtTo);
        item.addView(to);

        return item;
    }

}
