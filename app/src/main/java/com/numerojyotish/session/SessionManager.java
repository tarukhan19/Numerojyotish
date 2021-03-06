package com.numerojyotish.session;

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
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_WORKING_HOURS_ADDED = "is_working_hours_added";
    private static final String KEY_USERID = "userid";

    public static final String KEY_DOB = "dob";
    public static final String KEY_NAME = "name";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_YEAR= "year";

    public static final String KEY_RESPONSE = "response";
    public static final String KEY_ROLE = "role";
    public static final String KEY_MOBILE_NO = "mobileno";
    public static final String KEY_IMEI_NO = "imei";

    public static final String KEY_CUSTOMER_FNAME = "CFNAME";
    public static final String KEY_CUSTOMER_LNAME = "CLNAME";
    public static final String KEY_CUSTOMER_MOBILENO = "CMOBILENO";
    public static final String KEY_CUSTOMER_EMAILID = "CEMAILID";
    public static final String KEY_CUSTOMER_DOB = "CDOB";
    public static final String KEY_CUSTOMER_EXPIRATIONDATE = "CEXPDATE";
    public static final String KEY_CUSTOMER_GENDER = "CGENDER";


    public HashMap<String, String> getCustomerDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_CUSTOMER_FNAME, pref.getString(KEY_CUSTOMER_FNAME, ""));
        user.put(KEY_CUSTOMER_LNAME, pref.getString(KEY_CUSTOMER_LNAME, ""));
        user.put(KEY_CUSTOMER_MOBILENO, pref.getString(KEY_CUSTOMER_MOBILENO, ""));
        user.put(KEY_CUSTOMER_EMAILID, pref.getString(KEY_CUSTOMER_EMAILID, ""));
        user.put(KEY_CUSTOMER_DOB, pref.getString(KEY_CUSTOMER_DOB, ""));
        user.put(KEY_CUSTOMER_EXPIRATIONDATE, pref.getString(KEY_CUSTOMER_EXPIRATIONDATE, ""));
        user.put(KEY_CUSTOMER_GENDER, pref.getString(KEY_CUSTOMER_GENDER, ""));

        return user;
    }


    public void setCustomerDetails(String fname,String lname, String mobileno, String emailid, String dob, String expdate,
                                   String gender) {

        editor.putString(KEY_CUSTOMER_FNAME, fname);
        editor.putString(KEY_CUSTOMER_LNAME,lname );
        editor.putString(KEY_CUSTOMER_MOBILENO, mobileno);
        editor.putString(KEY_CUSTOMER_EMAILID, emailid);
        editor.putString(KEY_CUSTOMER_DOB, dob);
        editor.putString(KEY_CUSTOMER_EXPIRATIONDATE,expdate );
        editor.putString(KEY_CUSTOMER_GENDER, gender);

        editor.commit();
    }


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
        user.put(KEY_YEAR, pref.getString(KEY_YEAR, ""));
        return user;
    }


    public void setFromToDate(String year) {

        editor.putString(KEY_YEAR, year);

        editor.commit();
    }


    public void setLoginDetail(String role,String mobileNo,String imei) {
        editor.putString(KEY_ROLE, role);
        editor.putString(KEY_MOBILE_NO, mobileNo);
        editor.putString(KEY_IMEI_NO, imei);

        editor.commit();
    }

    // Get stored session data
    public HashMap<String, String> getLoginDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_ROLE, pref.getString(KEY_ROLE, ""));
        user.put(KEY_MOBILE_NO, pref.getString(KEY_MOBILE_NO, ""));
        user.put(KEY_IMEI_NO, pref.getString(KEY_IMEI_NO, ""));

        return user;
    }



    // Clear session details
    public void logoutUser() {
        editor.clear();
        editor.commit();
    }





}