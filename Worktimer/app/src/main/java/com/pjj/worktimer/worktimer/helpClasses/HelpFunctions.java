package com.pjj.worktimer.worktimer.helpClasses;

import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Jens on 07.06.2017.
 */

public class HelpFunctions {

    public static int dp(Context c, int i) {
        float density = c.getResources().getDisplayMetrics().density;
        return (int)((i * density) + 0.5);
    }

    public static String localURLEncoder(String text){
        try {
            return(URLEncoder.encode(text, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
