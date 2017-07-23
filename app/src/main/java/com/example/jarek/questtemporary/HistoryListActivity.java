package com.example.jarek.questtemporary;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.jarek.questtemporary.dataClasses.QuestHistoryDatabaseManager;

public class HistoryListActivity extends ListActivity {

    private SQLiteDatabase database;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listQuest = getListView();
        try {
            SQLiteOpenHelper sqLiteOpenHelper = new QuestHistoryDatabaseManager(this);
            database = sqLiteOpenHelper.getReadableDatabase();
            cursor = database.query("QUESTHISTORY",
                    new String[]{"_id", "DESCRIPTION", "EXPERIENCE", "DATE_END", "SUCCEED"},
                    null, null,null, null, "DATE_END DESC");
            CursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[] {"DESCRIPTION", "EXPERIENCE", "DATE_END", "SUCCEED"},
                    new int[] {android.R.id.text1},
                    0);
            listQuest.setAdapter(cursorAdapter);
        }catch (SQLiteException e){
            Toast.makeText(this, getString(R.string.text_errorDatabase),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        database.close();
    }
}
