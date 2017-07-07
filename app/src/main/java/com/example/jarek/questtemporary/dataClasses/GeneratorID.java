package com.example.jarek.questtemporary.dataClasses;

/**
 * Created by Jarek on 2017-04-18.
 */

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Klasa generująca unikalne identyfikatory.
 */
class GeneratorID {
    private static int ID;
    private static final String sharedID = "sharedID";

    static int getID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedID, Context.MODE_PRIVATE);
        ID = sharedPreferences.getInt("ID", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("ID", (ID + 1));
        editor.apply();
        return ID;
    }
}
