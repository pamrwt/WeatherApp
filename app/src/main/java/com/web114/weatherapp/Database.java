package com.web114.weatherapp;

/**
 * Created by user on 5/16/2018.
 */

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
public class Database extends SQLiteAssetHelper {
    private static final String DATABASE_NAMES = "sqlitedata";
    private static final int DATABASE_VERSION = 1;
    public Database(Context context) {
        super(context, DATABASE_NAMES, null, DATABASE_VERSION);
    }
}
