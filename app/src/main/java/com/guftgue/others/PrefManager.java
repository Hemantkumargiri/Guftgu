package com.guftgue.others;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.guftgue.helper.Const;
import com.guftgue.helper.PrefUtils;

public class PrefManager {
    private static SharedPreferences pref;
    private static PrefManager mInstance;
    SharedPreferences.Editor editor;
    Context _context;


    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "androidhive-welcome";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    private PrefManager() {
        mInstance = this;
    }
    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.commit();
    }

    public static SharedPreferences getPreference(Context context) {
        if (pref == null)
            pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref;
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getPreference(context).edit();
    }

    /*Loginq data & Access token*/
    public static class Login {
        private static SharedPreferences preferences;

        // # login and User details
        private static final String PREF_LOGIN = "pref_user";
        public static final String LOGIN_STATUS = "login status";
        public static final String USER_TOKEN_TYPE = "user_token_type";
        public static final String USER_ACCESS_TOKEN = "user_access_token";
        public static final String USER_MOBILE_NO = "mobile no";


        public static SharedPreferences getPreference(Context context) {
            if (preferences == null)
                preferences = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);

            return preferences;
        }

        public static SharedPreferences.Editor getEditor(Context context) {
            return getPreference(context).edit();
        }

        public static String getAccessToken(Context context) {
            String auth  = getPreference(context).getString(USER_ACCESS_TOKEN, Const.NOT_FOUND_STRING);
            String token_type = getPreference(context).getString(USER_TOKEN_TYPE,Const.NOT_FOUND_STRING);
            return token_type+" "+auth;
        }


        public static String getString(Context context, String dataKey) {
            return getPreference(context).getString(dataKey, Const.NOT_FOUND_STRING);
        }
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

}





























/*SharedPreferences sp=getSharedPreferences("Login", 0);
SharedPreferences.Editor Ed=sp.edit();
Ed.putString("Unm",Value );
Ed.putString("Psw",Value);
Ed.commit();
Get Value from Share preference:

SharedPreferences sp1=this.getSharedPreferences("Login",null);

String unm=sp1.getString("Unm", null);
String pass = sp1.getString("Psw", null);*/