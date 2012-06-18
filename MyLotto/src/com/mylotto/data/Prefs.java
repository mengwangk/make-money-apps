package com.mylotto.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.mylotto.helper.Constants;

/**
 * Store user preferences and settings.
 * 
 * @author MEKOH
 *
 */
public class Prefs {

    private SharedPreferences prefs = null;
    private Editor editor = null;
    private String userName = "mylotto";
    private String countryId = Constants.DEFAULT_COUNTRY_ID;
    private String noOfDraw = Constants.DEFAULT_NO_OF_DRAW;

    /**
     * Constructor
     * 
     * @param context
     */
    public Prefs(Context context) {
        this.prefs = context.getSharedPreferences("PREFS_PRIVATE", Context.MODE_PRIVATE);
        this.editor = this.prefs.edit();
    }

    public String getValue(String key, String defaultvalue) {
        if (this.prefs == null) {
            return "?";
        }

        return this.prefs.getString(key, defaultvalue);
    }

    public void setValue(String key, String value) {
        if (this.editor == null) {
            return;
        }
        this.editor.putString(key, value);
    }

    public String getUserName() {
        if (this.prefs == null) {
            return "mylotto";
        }
        this.userName = this.prefs.getString("username", "mylotto");
        return this.userName;
    }

    public void setUserName(String userName) {
        if (this.editor == null) {
            return;
        }
        this.editor.putString("username", userName);
    }
    
    public String getCountryId() {
        if (this.prefs == null) {
            return Constants.DEFAULT_COUNTRY_ID;
        }
        this.countryId = this.prefs.getString("countryid", Constants.DEFAULT_COUNTRY_ID);
        return this.countryId;
    }

    public void setCountryId(String countryId) {
        if (this.editor == null) {
            return;
        }
        this.editor.putString("countryid", countryId);
    }

    public String getNoOfDraw() {
        if (this.prefs == null) {
            return Constants.DEFAULT_NO_OF_DRAW;
        }
        this.noOfDraw = this.prefs.getString("noofdraw", Constants.DEFAULT_NO_OF_DRAW);
        return this.noOfDraw;
    }

    
    public void setNoOfDraw(String noOfDraw) {
        if (this.editor == null) {
            return;
        }
        this.editor.putString("noofdraw", noOfDraw);
    }
  
    public void save() {
        if (this.editor == null) {
            return;
        }
        this.editor.commit();
    }
}
