package Unlike.tabatmie.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Preference {

    private final String PREF_NAME = "unlike.tabatmie.pref";
    private final String VER_NAME = "unlike.tabatmie.ver";

    public static final String TOKEN = "token";
    public static final String EMAIL = "email";

    public static final String EXERCISE_TIME = "exercise_time";
    public static final String EXERCISE = "exercise";
    public static final String REST = "rest";
    public static final String SET = "set";
    public static final String ROUND = "round";
    public static final String ROUND_RESET = "round_reset";
    public static final String SOUND = "sound";

    public static final String EXERCISE_PAUSE = "is_exercise_pause";

    public static final String GO_ROUTINE = "go_routine";
    public static final String SAVE_SUCCESS = "save_success";

    private static Context context;

    public Preference(Context context) {
        Unlike.tabatmie.util.Preference.context = context;
    }

    public void put(String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        //editor.commit();
        editor.apply();
    }

    public void put(String key, boolean value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        //editor.commit();
        editor.apply();
    }

    public void put(String key, int value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        //editor.commit();
        editor.apply();
    }

    public void put(String key, float value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(key, value);
        //editor.commit();
        editor.apply();
    }

    public String getValue(String key, String dftValue) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        try {
            String returnValue = pref.getString(key, dftValue);
            return returnValue;
        } catch (Exception e) {
            e.printStackTrace();
            return dftValue;
        }
    }

    public int getValue(String key, int dftValue) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        try {
            int returnValue = pref.getInt(key, dftValue);
            return returnValue;
        } catch (Exception e) {
            e.printStackTrace();
            return dftValue;
        }
    }

    public float getValue(String key, float dftValue) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        try {
            return pref.getFloat(key, dftValue);
        } catch (Exception e) {
            e.printStackTrace();
            return dftValue;
        }
    }

    public boolean getValue(String key, boolean dftValue) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        try {
            return pref.getBoolean(key, dftValue);
        } catch (Exception e) {
            e.printStackTrace();
            return dftValue;
        }
    }

    public void pclear() {
//        synchronized (syncObj) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        //editor.commit();
        editor.apply();
//        }
    }


}
