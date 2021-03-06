package com.pjj.worktimer.worktimer.mainScreen;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.*;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pjj.worktimer.worktimer.helpClasses.HelpFunctions;
import com.pjj.worktimer.worktimer.main;
import com.pjj.worktimer.worktimer.projectScreen.Project;
import com.pjj.worktimer.worktimer.projectScreen.ProjectFolder;
import com.pjj.worktimer.worktimer.projectScreen.ProjectView;
import com.pjj.worktimer.worktimer.R;
import com.pjj.worktimer.worktimer.helpClasses.Save;

import java.util.ArrayList;


public class Dashboard extends Fragment {

    private ScrollView scrollView;
    private RelativeLayout relativeLayout;
    private MarginLayoutParams mlp;
    private RelativeLayout.LayoutParams rlLp;
    private ArrayList<RelativeLayout> relativeLayouts = new ArrayList<RelativeLayout>();
    private ArrayList<Integer> projectIds = new ArrayList<Integer>();

    private Project currentProject;

    private TextView tv;
    private ImageView iv;

    /*------------------------------------*/
    /*-----Override super()-functions-----*/
    /*------------------------------------*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Log.d("dashboard", "onCreate: ");
        relativeLayout = new RelativeLayout(main.getActivity());
        relativeLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        scrollView = new ScrollView(main.getActivity());
        scrollView.addView(relativeLayout);
        readSavedProjects();
        return scrollView;
    };

    /*------------------------------------*/
    /*----------GETTER & SETTER-----------*/
    /*------------------------------------*/

    public int getId(int position){
        return relativeLayouts.get(position).getId();
    }

    /*------------------------------------*/
    /*---------Program-Functions----------*/
    /*------------------------------------*/

    public void addProject(int projectId){
        if(relativeLayout == null){
            relativeLayout = new RelativeLayout(main.getActivity());
            scrollView = new ScrollView(main.getActivity());
            scrollView.addView(relativeLayout);
            readSavedProjects();
        }

        currentProject = ProjectFolder.getProjectById(projectId);

        //surrounding Layout
        RelativeLayout newView = new RelativeLayout(main.getActivity());
        newView.setId(View.generateViewId());
        newView.setOnClickListener(onClickToProject());

        //Textview inside the surrounding Layout
        tv = new TextView(main.getActivity());
        tv.setId(View.generateViewId());
        tv.setText(currentProject.getProjectInfo(Project.NAME));
        tv.setGravity(Gravity.CENTER);
        mlp = new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        tv.setLayoutParams(mlp);
        newView.addView(tv);

        //ImageView inside the surrounding Layout
        iv = new ImageView(main.getActivity());
        iv.setId(View.generateViewId());
        iv.setImageResource(R.drawable.delete);
        mlp = new MarginLayoutParams(HelpFunctions.dp(30), HelpFunctions.dp(30));
        mlp.setMargins(0,0,HelpFunctions.dp(10),HelpFunctions.dp(10));
        rlLp = new RelativeLayout.LayoutParams(mlp);
        rlLp.addRule(RelativeLayout.ALIGN_PARENT_END);
        rlLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        iv.setLayoutParams(rlLp);
        iv.setOnClickListener(onClickDelete());
        newView.addView(iv);

        //
        projectIds.add(projectId);
        relativeLayouts.add(newView);
        relativeLayout.addView(newView);

        sort();
    }

    public void sort(){
        int counter = 0;
        int below = 0;
        boolean right = false;

        for (RelativeLayout rl: relativeLayouts) {

            TransitionManager.beginDelayedTransition(rl);

            mlp = new MarginLayoutParams(HelpFunctions.dp(160), HelpFunctions.dp(160));

            if(!(counter % 2 == 0)){
                mlp.setMargins(0, HelpFunctions.dp(10), HelpFunctions.dp(30), HelpFunctions.dp(10));
                right = true;
            }else{
                mlp.setMargins(HelpFunctions.dp(30), HelpFunctions.dp(10), 0, HelpFunctions.dp(10));
                right = false;
            }

            rlLp = new RelativeLayout.LayoutParams(mlp);

            if(right){
                rlLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            }

            if(counter > 1){
                if(counter % 2 == 0){
                    below = counter - 1;
                }
                rlLp.addRule(RelativeLayout.BELOW, relativeLayouts.get(below).getId());
            }

            rl.setLayoutParams(rlLp);
            rl.setBackgroundColor(getResources().getColor(R.color.project_view_color));
            counter++;
        }
    }

    public void select(View view){
        int id = projectIds.remove(relativeLayouts.indexOf(view));
        relativeLayouts.remove(view);

        projectIds.add(0,id);
        relativeLayouts.add(0,(RelativeLayout) view);

        saveOrder();
        sort();
    }

    public void removeProject(View view){
        relativeLayout.removeView(view);
        int id = relativeLayouts.indexOf(view);
        relativeLayouts.remove(id);
        ProjectFolder.removeProject(projectIds.get(id));
        projectIds.remove(id);
        saveOrder();
        sort();
    }

    /*------------------------------------*/
    /*--------------Listener--------------*/
    /*------------------------------------*/

    private RelativeLayout.OnClickListener onClickToProject(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(v);
                Intent intent = new Intent(getContext(), ProjectView.class);
                intent.putExtra("ProjectId", projectIds.get(relativeLayouts.indexOf(v)));
                startActivity(intent);
            }
        };
    }

    private ImageView.OnClickListener onClickDelete(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeProject((View) v.getParent());
            }
        };
    }

    /*------------------------------------*/
    /*-------------Persistence------------*/
    /*------------------------------------*/

    public void saveOrder(){
        Save.saveOrder(projectIds);
    }

    public void readOrder(){
        int index;

        ArrayList<RelativeLayout> newLayouts = new ArrayList<RelativeLayout>();
        ArrayList<Integer> newOrder = Save.readOrder();

        if(newOrder != null){
            for(int i : newOrder){
                index = projectIds.indexOf(i);
                newLayouts.add(relativeLayouts.get(index));
            }
            relativeLayouts = newLayouts;
            projectIds = newOrder;
            sort();
        }
    }

    private void readSavedProjects() {

        ArrayList<Project> projects;

        projects = Save.readProjects();
        if(projects == null){
            return;
        }
        ProjectFolder.setProjectFolder(projects);
        for (Project p : projects) {
            if(p != null){
                addProject(p.getId());
            }
        }
        readOrder();
    }
}
