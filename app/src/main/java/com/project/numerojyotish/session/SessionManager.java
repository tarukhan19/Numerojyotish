package com.project.numerojyotish.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SessionManager {
    // Shared Preferences
    private SharedPreferences pref;
    // Editor for Shared preferences
    private Editor editor;
    // Shared pref file name
    private static final String PREF_NAME = "salesformPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_WORKING_HOURS_ADDED = "is_working_hours_added";
    private static final String KEY_USERID = "userid";

    public static final String KEY_DOB = "dob";
    public static final String KEY_NAME = "name";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_FROMDATE= "fromdate";
    public static final String KEY_TODATE = "todate";

    public static final String KEY_RESPONSE = "response";



    // Constructor
    public SessionManager(Context context) {
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public HashMap<String, String> getResponse() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_RESPONSE, pref.getString(KEY_RESPONSE, ""));

        return user;
    }


    public void setResponse(String response) {
        editor.putString(KEY_RESPONSE, response);

        editor.commit();
    }


    public HashMap<String, String> getBasicDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_DOB, pref.getString(KEY_DOB, ""));
        user.put(KEY_NAME, pref.getString(KEY_NAME, ""));
        user.put(KEY_GENDER, pref.getString(KEY_GENDER, ""));


        return user;
    }


    public void setBasicDetails(String dob,String name,String gender) {
        editor.putString(KEY_DOB, dob);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_GENDER, gender);

        editor.commit();
    }

    public HashMap<String, String> getFromToDate() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_FROMDATE, pref.getString(KEY_FROMDATE, ""));
        user.put(KEY_TODATE, pref.getString(KEY_TODATE, ""));
        return user;
    }


    public void setFromToDate(String fromdate,String todate) {

        editor.putString(KEY_FROMDATE, fromdate);
        editor.putString(KEY_TODATE, todate);

        editor.commit();
    }


    public void setUserId() {
        editor.putBoolean(IS_LOGIN, true);
        editor.commit();
    }

    // Get stored session data
    public HashMap<String, String> getUserId() {
        HashMap<String, String> user = new HashMap<>();
        return user;
    }



    // Clear session details
    public void logoutUser() {
        editor.clear();
        editor.commit();
    }




    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }





}