package com.pjj.worktimer.worktimer;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.*;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.view.View.inflate;


/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends Fragment {

    private ScrollView scrollView;
    private RelativeLayout relativeLayout;
    private MarginLayoutParams mpl;
    private ArrayList<RelativeLayout> relativeLayouts = new ArrayList<RelativeLayout>();

    private TextView tv;

    public Dashboard(){    }

    public void addProject(){
        //surrounding Layout
        RelativeLayout newView = new RelativeLayout(getContext());
        //newView.setLayoutParams(new RelativeLayout.LayoutParams(dp(160), dp(160)));
        newView.setId(View.generateViewId());
        newView.setOnClickListener(onClick());

        //Textview inside the previous Layout
        tv = new TextView(getContext());
        tv.setId(View.generateViewId());
        tv.setText("Projekt x \nDeadline \nSoll \nIst \nUmsatz");
        tv.setGravity(Gravity.CENTER);
        mpl = new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        tv.setLayoutParams(mpl);
        newView.addView(tv);

        relativeLayouts.add(newView);

        Toast.makeText(getContext(),""+relativeLayouts.get(relativeLayouts.size()-1).getId(), Toast.LENGTH_SHORT).show();

        sort();
    }

    public void sort(){
        int counter = 0;
        int below = 0;
        boolean right = false;
        MarginLayoutParams mpl;
        RelativeLayout.LayoutParams rlLp;

        relativeLayout.removeAllViews();

        for (RelativeLayout rl: relativeLayouts) {

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
            rl.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
            relativeLayout.addView(rl);
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

    private RelativeLayout.OnClickListener onClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayouts.remove(relativeLayouts.indexOf(v));
                sort();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        relativeLayout = new RelativeLayout(getContext());
        relativeLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        scrollView = new ScrollView(getContext());
        scrollView.addView(relativeLayout);
        //addProject();
        return scrollView;
    };
}
