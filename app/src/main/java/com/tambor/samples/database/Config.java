package com.tambor.samples.database;

import android.content.Context;

public class Config {
    public static final String DATABASE_NAME = "db.sqlite";
    public static final int DATABASE_VERSION = 1;

    public static String getDBPath(Context context){
        return  "/data/data/"+context.getPackageName()+"/databases/";
    }
}

