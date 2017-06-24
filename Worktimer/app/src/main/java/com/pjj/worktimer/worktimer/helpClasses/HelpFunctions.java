package com.pjj.worktimer.worktimer.helpClasses;

import android.content.Context;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import inapppurchase.util.Base64;

/**
 * Created by Jens on 07.06.2017.
 */

public class HelpFunctions {

    private static boolean isPremium = false;

    public static boolean getIsPremium(){ return isPremium; }

    public static void setIsPremium(boolean premium){ isPremium = premium; }

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

    public static String md5(String text){
        String result;
        try {

            byte[] bytes = (text).getBytes("UTF-8");
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            bytes = md5.digest(bytes);

            result = Base64.encode(bytes);

            return result;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }
}
