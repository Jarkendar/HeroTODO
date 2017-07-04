package com.example.jarek.questtemporary.activityClasses;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.jarek.questtemporary.R;
import com.example.jarek.questtemporary.dataClasses.HelpCategory;
import com.example.jarek.questtemporary.dataClasses.HelpRowAdapter;

import java.util.LinkedList;

public class HelpActivity extends AppCompatActivity {

    private ListView listViewHelp;
    private LinkedList<HelpCategory> helpWord = new LinkedList<>();
    private HelpRowAdapter helpRowAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(getText(R.string.text_help));
        actionBar.setDisplayHomeAsUpEnabled(true);

        listViewHelp = (ListView) findViewById(R.id.listView_Help);

        loadHelpCategoryList();
        int textColor = getResources().getColor(R.color.color_Blue);

        helpRowAdapter = new HelpRowAdapter(this, R.layout.row_help_list, helpWord, textColor);
        listViewHelp.setAdapter(helpRowAdapter);
        setComponentsColor();
    }

    private void setComponentsColor(){
        findViewById(R.id.RelativeLayoutHelp).setBackgroundColor(getResources().getColor(R.color.color_backgroundWhite));
        listViewHelp.setBackgroundColor(getResources().getColor(R.color.color_backgroundWhite));
    }

    private void loadHelpCategoryList(){
        helpWord.add(new HelpCategory(getString(R.string.help_aboutApp),getString(R.string.category_description_aboutApp)));
        helpWord.add(new HelpCategory(getString(R.string.help_aboutMe),getString(R.string.category_description_aboutMe)));
        helpWord.add(new HelpCategory(getString(R.string.help_category_start),getString(R.string.category_description_start)));
        helpWord.add(new HelpCategory(getString(R.string.help_category_addQuest),getString(R.string.category_description_addQuest)));
        helpWord.add(new HelpCategory(getString(R.string.help_category_class),getString(R.string.category_description_class)));
        helpWord.add(new HelpCategory(getString(R.string.help_category_ranks),getString(R.string.category_description_ranks)));
        helpWord.add(new HelpCategory(getString(R.string.help_category_statistics),getString(R.string.category_description_statistics)));
        helpWord.add(new HelpCategory(getString(R.string.help_category_whenQuestEnd),getString(R.string.category_description_whenQuestEnd)));
        helpWord.add(new HelpCategory(getString(R.string.help_category_experience),getString(R.string.category_description_experience)));
        helpWord.add(new HelpCategory(getString(R.string.help_category_hexGraph),getString(R.string.category_description_hexGraph)));
        helpWord.add(new HelpCategory(getString(R.string.help_category_level),getString(R.string.category_description_level)));
        helpWord.add(new HelpCategory(getString(R.string.help_category_strength),getString(R.string.category_description_strength)));
        helpWord.add(new HelpCategory(getString(R.string.help_category_endurance),getString(R.string.category_description_endurance)));
        helpWord.add(new HelpCategory(getString(R.string.help_category_dexterity),getString(R.string.category_description_dexterity)));
        helpWord.add(new HelpCategory(getString(R.string.help_category_intelligence),getString(R.string.category_description_intelligence)));
        helpWord.add(new HelpCategory(getString(R.string.help_category_wisdom),getString(R.string.category_description_wisdom)));
        helpWord.add(new HelpCategory(getString(R.string.help_category_charisma),getString(R.string.category_description_charisma)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
