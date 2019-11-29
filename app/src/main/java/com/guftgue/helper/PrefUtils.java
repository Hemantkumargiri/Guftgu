package com.guftgue.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefUtils {

    private static final String TAG = PrefUtils.class.getSimpleName();
    private static SharedPreferences mPreference;
    private static PrefUtils mInstance;


    // # preference
    public static final String NOTIFICATION_SETTING = "pref_notification";  // contains notification setting
    public static final String DEVICE_TOKEN = "firebase_token";
    Context context;

    private PrefUtils() {
        mInstance = this;
    }

    public static PrefUtils getInstance(Context context) {
        if (mInstance == null)
            mInstance = new PrefUtils();
        return mInstance;
    }

    public static SharedPreferences getPreference(Context context) {
        if (mPreference == null)
            mPreference = PreferenceManager.getDefaultSharedPreferences(context);
        return mPreference;
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
            String auth  = getPreference(context).getString(USER_ACCESS_TOKEN,Const.NOT_FOUND_STRING);
            String token_type = getPreference(context).getString(USER_TOKEN_TYPE,Const.NOT_FOUND_STRING);
            return token_type+" "+auth;
        }


        public static String getString(Context context, String dataKey) {
            return getPreference(context).getString(dataKey, Const.NOT_FOUND_STRING);
        }
    }
}
