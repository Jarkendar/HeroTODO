package com.example.jarek.questtemporary.dataClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.jarek.questtemporary.R;

/**
 * Created by Jarek on 4 lip 2017.
 */

public class ColorManager {
    private final String sharedColor = "colorTheme";
    private final String colorThemeKey = "colorThemeKey";
    private int textColor;
    private int todayQuestColor;
    private int endTimeQuestColor;
    private int evenQuestColor;
    private int notEvenQuestColor;
    private int painterColor;
    private int backgroundColor;
    private int helpTextColor;
    private int gainAchievColor;
    private int notgainAchievColor;
    private int selectedRowColor;
    private String imageColorName;

    public ColorManager(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedColor,Context.MODE_PRIVATE);
        readIDColors(sharedPreferences.getString(colorThemeKey, "default"),context);
    }

    private void readIDColors(String colorTheme, Context context){
        switch (colorTheme){
            case "default":{
                textColor = context.getResources().getColor(R.color.color_Write);
                todayQuestColor = context.getResources().getColor(R.color.color_backgroundBlue);
                endTimeQuestColor = context.getResources().getColor(R.color.color_backgroundOrange);
                evenQuestColor = context.getResources().getColor(R.color.color_backgroundWhite);
                notEvenQuestColor = context.getResources().getColor(R.color.color_backgroundGray);
                painterColor = context.getResources().getColor(R.color.color_Write);
                backgroundColor = context.getResources().getColor(R.color.color_backgroundWhite);
                helpTextColor = context.getResources().getColor(R.color.color_Blue);
                gainAchievColor = context.getResources().getColor(R.color.color_backgroundGold);
                notgainAchievColor = context.getResources().getColor(R.color.color_backgroundGray);
                selectedRowColor = context.getResources().getColor(R.color.color_backgroundGreen);
                imageColorName = "default";
                break;
            }
            case "dark":{
                textColor = context.getResources().getColor(R.color.dark_color_Write);
                todayQuestColor = context.getResources().getColor(R.color.dark_color_backgroundBlue);
                endTimeQuestColor = context.getResources().getColor(R.color.dark_color_backgroundOrange);
                evenQuestColor = context.getResources().getColor(R.color.dark_color_backgroundWhite);
                notEvenQuestColor = context.getResources().getColor(R.color.dark_color_backgroundGray);
                painterColor = context.getResources().getColor(R.color.dark_color_Write);
                backgroundColor = context.getResources().getColor(R.color.dark_color_backgroundWhite);
                helpTextColor = context.getResources().getColor(R.color.dark_color_Blue);
                gainAchievColor = context.getResources().getColor(R.color.dark_color_backgroundGold);
                notgainAchievColor = context.getResources().getColor(R.color.dark_color_backgroundGray);
                selectedRowColor = context.getResources().getColor(R.color.dark_color_backgroundGreen);
                imageColorName = "dark";
                break;
            }
        }
    }

    public int getTextColor() {
        return textColor;
    }

    public int getTodayQuestColor() {
        return todayQuestColor;
    }

    public int getEndTimeQuestColor() {
        return endTimeQuestColor;
    }

    public int getEvenQuestColor() {
        return evenQuestColor;
    }

    public int getNotEvenQuestColor() {
        return notEvenQuestColor;
    }

    public int getPainterColor() {
        return painterColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getHelpTextColor() {
        return helpTextColor;
    }

    public int getGainAchievColor() {
        return gainAchievColor;
    }

    public int getNotgainAchievColor() {
        return notgainAchievColor;
    }

    public String getImageColorName() {
        return imageColorName;
    }

    public int getSelectedRowColor() {
        return selectedRowColor;
    }
}
