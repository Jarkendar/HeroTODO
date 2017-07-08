package com.example.jarek.questtemporary.activityClasses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jarek.questtemporary.R;
import com.example.jarek.questtemporary.dataClasses.ColorManager;

public class TutorialActivity extends AppCompatActivity {

    private TextView header, selectedClass, optionInfo, helpInfo;
    private Spinner classList;
    private String heroClasses;

    private final String heroShared = "heroShared";
    private final String firstRunKey = "firstRun";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        joinComponentsWithVariable();
        setComponentsColor();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void joinComponentsWithVariable() {
        header = (TextView) findViewById(R.id.textView_header);
        selectedClass = (TextView) findViewById(R.id.textView_classInfo);
        optionInfo = (TextView) findViewById(R.id.textView_optionInfo);
        helpInfo = (TextView) findViewById(R.id.textView_helpInfo);

        classList = (Spinner) findViewById(R.id.spinner_class);
    }

    private void setComponentsColor() {
        final ColorManager colorManager = new ColorManager(getApplicationContext());
        header.setTextColor(colorManager.getTextColor());
        selectedClass.setTextColor(colorManager.getTextColor());
        optionInfo.setTextColor(colorManager.getTextColor());
        helpInfo.setTextColor(colorManager.getTextColor());
    }

    public void selectClass(View view) {
        heroClasses = classList.getSelectedItem().toString();
        selectedClass.setText(getString(R.string.text_classInfo) + " " + heroClasses);
    }

    public void acceptAndContinue(View view) {
        int heroClassID = R.string.class_native;
        if (heroClasses.equals(getString(R.string.class_bard)))
            heroClassID = R.string.class_bard;
        else if (heroClasses.equals(getString(R.string.class_hunter)))
            heroClassID = R.string.class_hunter;
        else if (heroClasses.equals(getString(R.string.class_lord)))
            heroClassID = R.string.class_lord;
        else if (heroClasses.equals(getString(R.string.class_mage)))
            heroClassID = R.string.class_mage;
        else if (heroClasses.equals(getString(R.string.class_merchant)))
            heroClassID = R.string.class_merchant;
        else if (heroClasses.equals(getString(R.string.class_warrior)))
            heroClassID = R.string.class_warrior;
        SharedPreferences sharedPreferences = getSharedPreferences(heroShared, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("heroClass", heroClassID);
        editor.putInt(firstRunKey, 1);
        editor.apply();
        launchMainActivity();
    }

    private void launchMainActivity() {
        Intent intent = new Intent(getApplicationContext(), QuestPanelMain.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
