package com.pjj.worktimer.worktimer;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.*;
import static android.content.res.Resources.*;
import static android.view.ViewGroup.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectLayout extends Fragment {

    private RelativeLayout rl;
    private TextView tv;

    public ProjectLayout() {

        rl.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addProject();
        //setBackgroundColor(getResources().getColor(color.holo_purple));
    }

    public void addProject(){
        /*
        MarginLayoutParams mpl;
        RelativeLayout rl = new RelativeLayout(context);
        rl.setId(View.generateViewId());
        mpl = new MarginLayoutParams(dp(160), dp(160));
        mpl.setMargins(dp(20), dp(10), dp(10), dp(10));
        rl.setLayoutParams(mpl);
        rl.setBackgroundColor(getResources().getColor(color.holo_purple));

        tv = new TextView(context);
        tv.setId(View.generateViewId());
        tv.setText("Projekt x \nDeadline \nSoll \nIst \nUmsatz");
        tv.setGravity(Gravity.CENTER);
        mpl = new MarginLayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tv.setLayoutParams(mpl);
        rl.addView(tv);
        */
        View newView = inflate(context, R.layout.project_layout, null);

        addView(newView);
    }

    public int dp(int i) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int)((i * density) + 0.5);
    }

}
