package com.guftgue.others;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MyPreferences {

    private static MyPreferences preferences = null;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor editor;

    private Context context;
    private String isLogedIn = "isLogedIn";
    private String isFirstTimeLaunch = "isFirstTimeLaunch";
    private String FirstName = "FirstName";
    private String LastName = "LastName";

    public MyPreferences(Context context) {
        setmPreferences(PreferenceManager.getDefaultSharedPreferences(context));
    }

    public SharedPreferences getmPreferences() {
        return mPreferences;
    }

    public void setmPreferences(SharedPreferences mPreferences) {
        this.mPreferences = mPreferences;
        editor = mPreferences.edit();

    }


    public static MyPreferences getInstance(Context context) {
        if (preferences == null) {
            preferences = new MyPreferences(context);
        }
        return preferences;
    }

    public boolean isLoggedIn() {
        return mPreferences.getBoolean(AppConstants.IS_LOGGED_IN, false);
    }

    public void setLoggedIn(boolean isLoggedin) {
        editor.putBoolean(AppConstants.IS_LOGGED_IN, isLoggedin);
        editor.commit();
    }

    public boolean getisFirstTimeLaunch() {
        return mPreferences.getBoolean(this.isFirstTimeLaunch, false);
    }

    public void setisFirstTimeLaunch(boolean isFirstTimeLaunch) {
        editor.putBoolean(this.isFirstTimeLaunch, isFirstTimeLaunch);
        editor.commit();
    }



    public void logout() {
        editor.clear();
        editor.commit();

    }

    public void clearSharePrefrences() {
        editor = mPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public void setAccessToken(String accessToken) {
        editor.putString(AppConstants.ACCESS_TOKEN, accessToken);
        editor.commit();
    }

    public String getAccessToken() {
        return mPreferences.getString(AppConstants.ACCESS_TOKEN, "");
    }


    public void setEmail(String email) {
        editor.putString(AppConstants.EMAIL, email);
        editor.commit();
    }

    public String getEmail() {
        return mPreferences.getString(AppConstants.EMAIL, "");
    }

    public void setName(String name) {
        editor.putString(AppConstants.NAME, name);
        editor.commit();
    }

    public String getName() {
        return mPreferences.getString(AppConstants.NAME, "");
    }

    public void setMobile(String phone) {
        editor.putString(AppConstants.MOBILE, phone);
        editor.commit();
    }

    public String getMobile() {
        return mPreferences.getString(AppConstants.MOBILE, "");
    }


    public void setGradeClass(String gradeClass) {
        editor.putString(AppConstants.GRADE_CLASS, gradeClass);
        editor.commit();
    }

    public String getGradeClass() {
        return mPreferences.getString(AppConstants.GRADE_CLASS, "");
    }


    public void setDOB(String dob) {
        editor.putString(AppConstants.DOB, dob);
        editor.commit();

    }

    public String getDOB() {
        return mPreferences.getString(AppConstants.DOB, "");
    }


    public void setCity(String city) {
        editor.putString(AppConstants.CITY, city);
        editor.commit();
    }

    public String getCity() {
        return mPreferences.getString(AppConstants.CITY, "");
    }

    public void setArea(String area) {
        editor.putString(AppConstants.AREA, area);
        editor.commit();
    }

    public String getArea() {
        return mPreferences.getString(AppConstants.AREA, "");
    }

    public void setState(String state) {
        editor.putString(AppConstants.STATE, state);
        editor.commit();
    }

    public String getState() {
        return mPreferences.getString(AppConstants.STATE, "");
    }

    public void setSchoolName(String schoolName) {
        editor.putString(AppConstants.SCHOOL_NAME, schoolName);
        editor.commit();
    }

    public String getSchoolName() {
        return mPreferences.getString(AppConstants.SCHOOL_NAME, "");
    }

    public void setUserType(String userType) {
        editor.putString(AppConstants.USER_TYPE, userType);
        editor.commit();
    }

    public String getUserType() {
        return mPreferences.getString(AppConstants.USER_TYPE, "");
    }

    public void setPincode(String pincode) {
        editor.putString(AppConstants.PINCODE, pincode);
        editor.commit();
    }

    public String getPincode() {
        return mPreferences.getString(AppConstants.PINCODE, "");
    }

    public void setAdditionalGrade(String additionalGrade) {
        editor.putString(AppConstants.ADDITIONAL_GRADE, additionalGrade);
        editor.commit();
    }

    public String getAdditionalGrade() {
        return mPreferences.getString(AppConstants.ADDITIONAL_GRADE, "");
    }

    public void setUserDp(String image) {
        editor.putString(AppConstants.PROFILE_PICTURE, image);
        editor.commit();
    }

    public String getUserDp() {
        return mPreferences.getString(AppConstants.PROFILE_PICTURE, "");
    }


    public void setGender(String gender) {
        editor.putString(AppConstants.GENDER, gender);
        editor.commit();
    }


    public String getGender() {
        return mPreferences.getString(AppConstants.GENDER, "");
    }

}
