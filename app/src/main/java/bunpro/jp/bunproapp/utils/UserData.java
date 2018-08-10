package bunpro.jp.bunproapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserData {

    private Context mContext;
    public static UserData instance = null;

    private SharedPreferences mPref;

    private static String USER_DATA_PREF = "user_data_pref";
    private static String USER_LOGIN = "user_login";
    private static String USER_KEY = "user_key";
    private static String USER_NAME = "user_name";

    private UserData(Context context) {
        mContext = context;
        mPref = mContext.getSharedPreferences(USER_DATA_PREF, Context.MODE_PRIVATE);
    }

    public static UserData getInstance(Context context) {
        if (instance == null) {
            instance = new UserData(context);
        }

        return instance;
    }

    public void setUserLogin() {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(USER_LOGIN, true);
        editor.apply();
    }

    public boolean getUserLogin() {
        return mPref.getBoolean(USER_LOGIN, false);
    }

    public void setUserKey(String key) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(USER_KEY, key);
        editor.apply();
    }

    public void setUserName(String userName) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(USER_NAME, userName);
        editor.apply();
    }

    public String getUserName() {
        return mPref.getString(USER_NAME, "");
    }

    public String getUserKey() {
        return mPref.getString(USER_KEY, "");
    }

    public void removeUser() {
        SharedPreferences.Editor editor = mPref.edit();
        editor.remove(USER_LOGIN);
        editor.remove(USER_KEY);
        editor.apply();
    }
}
