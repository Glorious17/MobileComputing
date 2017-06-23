package com.pjj.worktimer.worktimer.projectScreen.fragments;


import android.content.Context;
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
import com.pjj.worktimer.worktimer.projectScreen.ProjectFolder;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Statistik_Project extends Fragment {

    private RelativeLayout history;

    private ArrayList<RelativeLayout> items;

    private Project project;

    /*------------------------------------*/
    /*-----Override super()-functions-----*/
    /*------------------------------------*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        project = ProjectFolder.getProjectById(getActivity().getIntent().getIntExtra("ProjectId", 0));

        ScrollView mainView = new ScrollView(getContext());
        mainView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        history = new RelativeLayout(getContext());
        history.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mainView.addView(history);

        items = new ArrayList<>();

        reload();

        return mainView;
    }

    /*------------------------------------*/
    /*---------Program-Functions----------*/
    /*------------------------------------*/

    public void reload(){

        ArrayList<String[]> statisticHistory = project.getHistory();

        if(statisticHistory.size() > items.size()){

            RelativeLayout newItem;
            RelativeLayout.LayoutParams rlLp;

            int itemsToAdd = statisticHistory.size() - items.size();

            String[] data;

            while(itemsToAdd > 0){

                rlLp = getLayoutParams();

                data = statisticHistory.get(statisticHistory.size()-itemsToAdd);

                newItem = generateListItem(data[0], data[1], data[2], data[3], data[4]);

                history.addView(newItem);
                items.add(newItem);

                newItem.setLayoutParams(rlLp);

                if(items.size() > 1){
                    rlLp = getLayoutParams();
                    rlLp.addRule(RelativeLayout.BELOW, newItem.getId());
                    items.get(items.size()-2).setLayoutParams(rlLp);
                }

                itemsToAdd--;

            }

        }

    }

    /*------------------------------------*/
    /*----------GETTER & SETTER-----------*/
    /*------------------------------------*/

    private RelativeLayout.LayoutParams getLayoutParams(){

        RelativeLayout.LayoutParams rlLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        rlLp.setMargins(HelpFunctions.dp(getContext(),20),
                HelpFunctions.dp(getContext(), 20),
                HelpFunctions.dp(getContext(),20),
                0);

        return rlLp;
    }

    /*------------------------------------*/
    /*---------Generate - Layout----------*/
    /*------------------------------------*/

    private RelativeLayout generateListItem(String txtTitle, String txtHours, String txtMinutes, String txtFrom, String txtTo){

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout item = (RelativeLayout) inflater.inflate(R.layout.project_statistic_item, null, false);
        item.setId(View.generateViewId());

        TextView title = (TextView) item.findViewById(R.id.statistic_item_title);
        TextView hours = (TextView) item.findViewById(R.id.statistic_item_hours);
        TextView minutes = (TextView) item.findViewById(R.id.statistic_item_minutes);
        TextView from = (TextView) item.findViewById(R.id.statistic_item_from);
        TextView to = (TextView) item.findViewById(R.id.statistic_item_to);

        title.setText(txtTitle);
        hours.setText("Stunden: " + txtHours);
        minutes.setText("Minuten: " + txtMinutes);
        from.setText("Von: " + txtFrom);
        to.setText("Bis: " + txtTo);

        return item;
    }

}
