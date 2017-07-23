package com.example.jarek.questtemporary.dataClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;

/**
 * Created by Jarek on 23 lip 2017.
 */

public class QuestHistoryDatabaseManager extends SQLiteOpenHelper {

    private static final String DB_NAME = "HeroTODO";
    private static final int DB_VERSION = 1;

    public QuestHistoryDatabaseManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        upgradeDatabase(sqLiteDatabase, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        upgradeDatabase(sqLiteDatabase, oldVersion, newVersion);
    }

    private void upgradeDatabase(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){
        if(oldVersion < 1){
            sqLiteDatabase.execSQL("CREATE TABLE QUESTHISTORY (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "DESCRIPTION TEXT, " +
                    "EXPERIENCE REAL, " +
                    "DATE_END NUMERIC," +
                    "SUCCEED NUMERIC);");
        }
    }

    public void insertQuest(SQLiteDatabase sqLiteDatabase, String description, double experience, Calendar dateEnd, boolean succeed){
        ContentValues contentValues = new ContentValues();
        contentValues.put("DESCRIPTION", description);
        contentValues.put("EXPERIENCE", experience);
        contentValues.put("DATE_END", dateEnd.getTimeInMillis());
        contentValues.put("SUCCEED", succeed);

        sqLiteDatabase.insert("QUESTHISTORY", null, contentValues);
    }
}
