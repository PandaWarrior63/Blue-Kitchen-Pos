package com.lectus.blue.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "BluePrefs";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_TOKEN = "TOKEN";
    private static final String KEY_OPEN_POS = "OPEN_POS";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }
    public String getKeyToken(){
        return pref.getString(KEY_TOKEN, null);
    }
    public String getKeyOpenPos() {return pref.getString(KEY_OPEN_POS,"");}
    public void setKeyToken(String token)
    {
        editor.putBoolean(KEY_IS_LOGGEDIN, true);
        editor.putString(KEY_TOKEN, token);

        // commit changes
        editor.commit();

        Log.d(TAG, "User Token session modified!");
    }
    public void setKeyOpenPos(String entry)
    {
        editor.putString(KEY_OPEN_POS, entry);
        // commit changes
        editor.commit();
        Log.d(TAG, "Open Pos Entry modified!");
    }
}