package com.example.jarek.questtemporary.activityClasses;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.jarek.questtemporary.HistoryRowAdapter;
import com.example.jarek.questtemporary.R;
import com.example.jarek.questtemporary.dataClasses.ColorManager;
import com.example.jarek.questtemporary.dataClasses.QuestHistoryDatabaseManager;
import com.example.jarek.questtemporary.dataClasses.QuestRowAdapter;

public class HistoryListActivity extends ListActivity {

    private SQLiteDatabase database;
    private Cursor cursor;
//gittest
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listQuest = getListView();
        try {
            SQLiteOpenHelper sqLiteOpenHelper = new QuestHistoryDatabaseManager(this);
            database = sqLiteOpenHelper.getReadableDatabase();
            cursor = database.query("QUESTHISTORY",
                    new String[]{"_id", "DESCRIPTION", "EXPERIENCE", "DATE_QUEST_END", "DATE_END", "SUCCEED"},
                    null, null,null, null, "DATE_QUEST_END DESC");
            ColorManager colorManager = new ColorManager(this);

            int colors[] = {colorManager.getTextColor(),colorManager.getEndTimeQuestColor(), colorManager.getTodayNormalQuestColor()};
            HistoryRowAdapter historyRowAdapter = new HistoryRowAdapter(this,cursor,colors);
            listQuest.setAdapter(historyRowAdapter);
        }catch (SQLiteException e){
            Toast.makeText(this, getString(R.string.text_errorDatabase),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!cursor.isClosed()) cursor.close();
        if (database.isOpen()) database.close();
    }
}
