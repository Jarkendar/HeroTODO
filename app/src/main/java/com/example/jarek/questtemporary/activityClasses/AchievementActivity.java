package com.example.jarek.questtemporary.activityClasses;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jarek.questtemporary.R;
import com.example.jarek.questtemporary.dataClasses.Achievement;
import com.example.jarek.questtemporary.dataClasses.AchievementRowAdapter;
import com.example.jarek.questtemporary.dataClasses.ColorManager;

import java.util.LinkedList;

public class AchievementActivity extends AppCompatActivity {

    private LinkedList<Achievement> achievements;

    private TextView header;
    private ListView listView;

    private AchievementRowAdapter achievementRowAdapter;

    private final String sharedAchievement = "achievements";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        joinComponentsWithVariable();
        setComponentsColors();
        achievements = readAchievements();

        ColorManager colorManager = new ColorManager(getApplicationContext());
        int textColorID = colorManager.getTextColor();
        int gainColorID = colorManager.getGainAchievColor();
        int notgainColorID = colorManager.getNotgainAchievColor();

        achievementRowAdapter = new AchievementRowAdapter(getApplicationContext(),R.layout.row_achievement_layout,achievements,textColorID,gainColorID,notgainColorID);
        listView.setAdapter(achievementRowAdapter);
    }

    private LinkedList<Achievement> readAchievements(){
        LinkedList<Achievement> linkedList = new LinkedList<>();
        String[] names = getResources().getStringArray(R.array.achievement_Names);
        String[] descriptions = getResources().getStringArray(R.array.achievement_Descriptions);
        SharedPreferences sharedPreferences = getSharedPreferences(sharedAchievement, MODE_PRIVATE);
        for (int i =0; i<names.length; i++){
            linkedList.addLast(new Achievement(names[i],descriptions[i],sharedPreferences.getBoolean(names[i],false)));
        }
        return linkedList;
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
