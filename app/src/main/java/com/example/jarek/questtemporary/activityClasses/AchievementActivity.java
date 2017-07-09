package com.example.jarek.questtemporary.activityClasses;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jarek.questtemporary.R;
import com.example.jarek.questtemporary.dataClasses.Achievement;
import com.example.jarek.questtemporary.dataClasses.ColorManager;

import java.util.LinkedList;

public class AchievementActivity extends AppCompatActivity {

    private LinkedList<Achievement> achievements;

    private TextView header;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        joinComponentsWithVariable();
        setComponentsColors();
        achievements = new LinkedList<>();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {//kliknięcie strzałki back w actionBar
                onBackPressed();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void joinComponentsWithVariable(){
        header = (TextView) findViewById(R.id.textView_achievementHeader);
        listView = (ListView) findViewById(R.id.listView_achievement);
    }

    private void setComponentsColors(){
        ColorManager colorManager = new ColorManager(getApplicationContext());
        header.setTextColor(colorManager.getTextColor());
        findViewById(R.id.RelativeLayoutAchievement).setBackgroundColor(colorManager.getBackgroundColor());
    }



}
