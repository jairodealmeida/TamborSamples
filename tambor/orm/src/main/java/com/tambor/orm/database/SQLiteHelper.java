package com.tambor.orm.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    private final String scriptCreate;
    private final String scriptUpdate;

    public SQLiteHelper(Context ctx, String nomeBd,
                        int versaoBanco, String scriptCreate,
                        String scriptUpdate) {
        super(ctx, nomeBd, null, versaoBanco);
        this.scriptCreate = scriptCreate;
        this.scriptUpdate = scriptUpdate;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(scriptCreate);
    }

    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {
        //db.execSQL(scriptCreate);
        //db.execSQL(scriptUpdate);  
        onCreate(db);
    }
}  