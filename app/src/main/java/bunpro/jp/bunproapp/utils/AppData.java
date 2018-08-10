package bunpro.jp.bunproapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppData {

    public static AppData instance = null;
    private Context mContext;

    private static String APP_DATA_PREF = "app_data_pref";
    private static String FURIGANA = "furigana";
    private static String HIDE_ENGLISH = "hide_english";
    private static String BUNNY_MODE = "bunny_mode";
    private static String LIGHT_MODE = "light_mode";

    private static String SUBSCRIBE = "subscribe";


    private SharedPreferences mPref;

    private AppData(Context context) {
        mContext = context;
        mPref = mContext.getSharedPreferences(APP_DATA_PREF, Context.MODE_PRIVATE);
    }

    public static AppData getInstance(Context context) {
        if (instance == null) {
            instance = new AppData(context);
        }

        return instance;
    }

    public void setFurigana(int type) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt(FURIGANA, type);
        editor.apply();
    }

    public int getFurigana() {
        return mPref.getInt(FURIGANA, Constants.SETTING_FURIGANA_ALWAYS);
    }

    public void setHideEnglish(int type) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt(HIDE_ENGLISH, type);
        editor.apply();
    }

    public int getHideEnglish() {
        return mPref.getInt(HIDE_ENGLISH, Constants.SETTING_HIDE_ENGLISH_YES);
    }

    public void setBunnyMode(int type) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt(BUNNY_MODE, type);
        editor.apply();
    }


    public void setLightMode(int type) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt(LIGHT_MODE, type);
        editor.apply();
    }

    public int getLightMode() {
        return mPref.getInt(LIGHT_MODE, Constants.SETTING_LIGHT_MODE_OFF);
    }

    public int getBunnyMode() {
        return mPref.getInt(BUNNY_MODE, Constants.SETTING_BUNNY_MODE_ON);
    }

    public void setSubscription(int type) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt(SUBSCRIBE, type);
        editor.apply();
    }

    public int getSubscription() {
        return mPref.getInt(SUBSCRIBE, Constants.SETTING_SUBSCRIPTION_YES);
    }
}
