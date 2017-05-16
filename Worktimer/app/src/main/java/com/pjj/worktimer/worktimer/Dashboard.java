package com.pjj.worktimer.worktimer;


import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.v4.app.Fragment;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.*;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import static android.transition.TransitionManager.beginDelayedTransition;
import static android.view.View.inflate;


/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends Fragment {

    private ScrollView scrollView;
    private RelativeLayout relativeLayout;
    private MarginLayoutParams mpl;
    private RelativeLayout.LayoutParams rlLp;
    private ArrayList<RelativeLayout> relativeLayouts = new ArrayList<RelativeLayout>();
    private ArrayList<Integer> projectIds = new ArrayList<Integer>();

    private Project currentProject;

    private TextView tv;
    private ImageView iv;

    /*
    Dashboard ist ein von Hand programmiertes Fragment.
    Im eigentlichen Sinne wird in der "addProject()"-Methode nur ein Layout generiert.
     */
    public void addProject(int projectId){
        currentProject = ProjectFolder.getProjectById(projectId);

        //surrounding Layout
        RelativeLayout newView = new RelativeLayout(getContext());
        newView.setId(View.generateViewId());
        newView.setOnClickListener(onClickToProject());

        //Textview inside the surrounding Layout
        tv = new TextView(getContext());
        tv.setId(View.generateViewId());
        tv.setText(currentProject.getProjectInfo(Project.NAME));
        tv.setGravity(Gravity.CENTER);
        mpl = new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        tv.setLayoutParams(mpl);
        newView.addView(tv);

        //ImageView inside the surrounding Layout
        iv = new ImageView(getContext());
        iv.setId(View.generateViewId());
        iv.setImageResource(R.drawable.delete);
        mpl = new MarginLayoutParams(dp(30), dp(30));
        mpl.setMargins(0,0,dp(10),dp(10));
        rlLp = new RelativeLayout.LayoutParams(mpl);
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

        //relativeLayout.removeAllViews();

        for (RelativeLayout rl: relativeLayouts) {

            TransitionManager.beginDelayedTransition(rl);

            mpl = new MarginLayoutParams(dp(160), dp(160));

            if(!(counter % 2 == 0)){
                mpl.setMargins(0, dp(10), dp(30), dp(10));
                right = true;
            }else{
                mpl.setMargins(dp(30), dp(10), 0, dp(10));
                right = false;
            }

            rlLp = new RelativeLayout.LayoutParams(mpl);

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

    public int getId(int position){
        return relativeLayouts.get(position).getId();
    }

    public int dp(int i) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int)((i * density) + 0.5);
    }

    public void select(View view){
        int id = projectIds.remove(relativeLayouts.indexOf(view));
        relativeLayouts.remove(view);

        projectIds.add(0,id);
        relativeLayouts.add(0,(RelativeLayout) view);

        sort();
    }

    private RelativeLayout.OnClickListener onClickToProject(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProjectView.class);
                intent.putExtra("ProjectId", projectIds.get(relativeLayouts.indexOf(v)));
                startActivity(intent);
                select(v);
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

    public void removeProject(View view){
        relativeLayout.removeView(view);
        int id = relativeLayouts.indexOf(view);
        relativeLayouts.remove(id);
        ProjectFolder.removeProject(projectIds.get(id));
        projectIds.remove(id);
        sort();
    }

    public void saveOrder(){
        try {
            Save.saveOrder(getContext(), projectIds);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readOrder(){
        try {

            int index;

            ArrayList<RelativeLayout> newLayouts = new ArrayList<RelativeLayout>();
            ArrayList<Integer> newOrder = Save.readOrder(getContext());

            if(newOrder != null){
                for(int i : newOrder){
                    index = projectIds.indexOf(i);
                    newLayouts.add(relativeLayouts.get(index));
                }
                relativeLayouts = newLayouts;
                projectIds = newOrder;
                sort();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void readSavedProjects() {

        ArrayList<Project> projects;

        try {

            projects = Save.readProjects(getContext());
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

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        relativeLayout = new RelativeLayout(getContext());
        relativeLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        scrollView = new ScrollView(getContext());
        scrollView.addView(relativeLayout);
        readSavedProjects();
        return scrollView;
    };
}
