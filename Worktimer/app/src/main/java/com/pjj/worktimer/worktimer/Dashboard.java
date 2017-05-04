package com.pjj.worktimer.worktimer;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends Fragment {

    private ProjectLayout pl;

    public Dashboard(){

    }

    public ProjectLayout getProjectLayout(){
        return pl;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        pl = new ProjectLayout(getContext());
        return pl;
    };
}
