package com.pjj.worktimer.worktimer.mainScreen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pjj.worktimer.worktimer.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Einstellungen extends Fragment {


    public Einstellungen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_einstellungen, container, false);
    }

}
