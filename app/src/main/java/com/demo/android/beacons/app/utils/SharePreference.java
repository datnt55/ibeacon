package com.demo.android.beacons.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class SharePreference {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public static final String BEACON_1 = "BEACON_1";
    public static final String BEACON_2 = "BEACON_2";
    public static final String BEACON_3 = "BEACON_3";
    // constructor
    public SharePreference(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sp.edit();
    }

    public void removeKey(String key) {
        editor.remove(key);
        editor.commit();
    }

    public String getBeacon1() {
        return sp.getString(BEACON_1, "");
    }

    public void saveBeacon1(String beacon) {
        editor.putString(BEACON_1, beacon);
        editor.commit();
    }

    public String getBeacon2() {
        return sp.getString(BEACON_2, "");
    }

    public void saveBeacon2(String beacon) {
        editor.putString(BEACON_2, beacon);
        editor.commit();
    }

    public String getBeacon3() {
        return sp.getString(BEACON_3, "");
    }

    public void saveBeacon3(String beacon) {
        editor.putString(BEACON_3, beacon);
        editor.commit();
    }

    public void saveBeacon(String beacon){
        if (getBeacon1().equals("")) {
            saveBeacon1(beacon);
            return;
        }

        if (getBeacon2().equals("")) {
            saveBeacon2(beacon);
            return;
        }

        if (getBeacon3().equals("")) {
            saveBeacon3(beacon);
            return;
        }
    }

    public String getBeacon(String beacon){
        if (getBeacon1().equals(beacon)) {
            return BEACON_1;
        }

        if (getBeacon2().equals(beacon)) {
            return BEACON_2;
        }

        if (getBeacon3().equals(beacon)) {
            return BEACON_3;
        }
        return null;
    }

    public boolean isSaved(String beacon){
        if (getBeacon1().equals(beacon)) {
            return true;
        }

        if (getBeacon2().equals(beacon)) {
            return true;
        }

        if (getBeacon3().equals(beacon)) {
            return true;
        }
        return false;
    }
}
