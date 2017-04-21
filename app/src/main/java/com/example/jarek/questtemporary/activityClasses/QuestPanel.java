package com.example.jarek.questtemporary.activityClasses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jarek.questtemporary.R;
import com.example.jarek.questtemporary.dataClasses.FileManager;
import com.example.jarek.questtemporary.dataClasses.RowAdapter;
import com.example.jarek.questtemporary.dataClasses.Quest;
import com.example.jarek.questtemporary.heroClasses.Bard;
import com.example.jarek.questtemporary.heroClasses.Hero;
import com.example.jarek.questtemporary.heroClasses.Hunter;
import com.example.jarek.questtemporary.heroClasses.Lord;
import com.example.jarek.questtemporary.heroClasses.Mage;
import com.example.jarek.questtemporary.heroClasses.Merchant;
import com.example.jarek.questtemporary.heroClasses.Warrior;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

public class QuestPanel extends AppCompatActivity implements Observer{

    private ProgressBar pBExperience;
    private LinkedList<Quest> quests;
    private ListView listView;
    private TextView tClassName, tClassLevel, tExperience;
    private int heroClassID;
    private RowAdapter rowAdapter;

    private final String userQuestFile = "userQuestFile";
    private final String userAddQuest = "userAddQuest";
    private final String heroShared = "heroShared";

    private Hero userHero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_panel);

        joinComponentsWithVariable();

        pBExperience.setProgress(50);//test

        quests = new LinkedList<>();

        rowAdapter = new RowAdapter(this, R.layout.layout_quest, quests);
        rowAdapter.addObserver(this);

        adapterRefresh();
    }

    private void adapterRefresh(){
        Collections.sort(quests, new Comparator<Quest>() {
            @Override
            public int compare(Quest quest1, Quest quest2) {
                return quest1.getTimeToLiveDate().compareTo(quest2.getTimeToLiveDate());
            }
        });
        rowAdapter.setData(quests);
        listView.setAdapter(rowAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        quests.clear();
        FileManager fileManager = new FileManager();
        quests.addAll(fileManager.deserializationQuests(userQuestFile, getApplicationContext()));
        String userInfoShared = "userInfoShared";
        SharedPreferences sharedPreferences = getSharedPreferences(userInfoShared, MODE_PRIVATE);
        if (sharedPreferences.getBoolean("addQuests", false)){
            Log.d("++++++++++++++++++", "onResume: wczytuje");
            LinkedList<Quest> addQuests;
            addQuests = fileManager.deserializationQuests(userAddQuest, getApplicationContext());
            Log.d("++++++++++++++++++", "onResume: długość" + addQuests.size());
            quests.addAll(addQuests);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("addQuests",false);
            editor.apply();
        }

        readHeroFromShared();
        adapterRefresh();
    }

    /**
     * Metoda wczytuje postać hero z sharedPreferences.
     */
    private void readHeroFromShared(){
        SharedPreferences sharedPreferences1 = getSharedPreferences(heroShared,MODE_PRIVATE);
        heroClassID = sharedPreferences1.getInt("heroClass",R.string.class_native);
        tClassName.setText(getString(heroClassID));
        if (heroClassID != R.string.class_native) {
            double strength = (double) sharedPreferences1.getFloat("strength",0);
            double endurance = (double)sharedPreferences1.getFloat("endurance",0);
            double dexterity = (double)sharedPreferences1.getFloat("dexterity",0);
            double intelligence = (double)sharedPreferences1.getFloat("intelligence",0);
            double wisdom =(double) sharedPreferences1.getFloat("wisdom",0);
            double charisma = (double)sharedPreferences1.getFloat("charisma",0);
            switch (heroClassID) {
                case R.string.class_bard: {
                    userHero = new Bard(strength,endurance,dexterity,intelligence,wisdom,charisma);
                    break;
                }
                case R.string.class_hunter:{
                    userHero = new Hunter(strength,endurance,dexterity,intelligence,wisdom,charisma);
                    break;
                }
                case R.string.class_merchant:{
                    userHero = new Merchant(strength,endurance,dexterity,intelligence,wisdom,charisma);
                    break;
                }
                case R.string.class_mage:{
                    userHero = new Mage(strength,endurance,dexterity,intelligence,wisdom,charisma);
                    break;
                }
                case R.string.class_lord:{
                    userHero = new Lord(strength,endurance,dexterity,intelligence,wisdom,charisma);
                    break;
                }
                case R.string.class_warrior:{
                    userHero = new Warrior(strength,endurance,dexterity,intelligence,wisdom,charisma);
                    break;
                }
            }
            tClassLevel.setText(String.valueOf(userHero.getHeroLVL()).concat(" "+getString(R.string.text_level)));
            pBExperience.setProgress((int)userHero.getHeroEXP()%100);
            tExperience.setText(String.valueOf((int)userHero.getHeroEXP()%100).concat(getString(R.string.text_experienceEpmty)));
        }
    }

    private void refreshHeroInfo(){
        tClassLevel.setText(String.valueOf(userHero.getHeroLVL()).concat(" "+getString(R.string.text_level)));
        pBExperience.setProgress((int)userHero.getHeroEXP()%100);
        tExperience.setText(String.valueOf((int)userHero.getHeroEXP()%100).concat(getString(R.string.text_experienceEpmty)));
    }

    @Override
    protected void onPause() {
        super.onPause();
        FileManager fileManager = new FileManager();
        fileManager.serializationQuests(quests, userQuestFile, getApplicationContext());
        saveHeroToShared();
    }

    private void saveHeroToShared(){
        if (userHero!= null) {
            SharedPreferences.Editor editor = getSharedPreferences(heroShared, MODE_PRIVATE).edit();
            editor.putFloat("strength", (float) userHero.getStrengthExperience());
            editor.putFloat("endurance", (float) userHero.getEnduranceExperience());
            editor.putFloat("dexterity", (float) userHero.getDexterityExperience());
            editor.putFloat("intelligence", (float) userHero.getDexterityExperience());
            editor.putFloat("wisdom", (float) userHero.getWisdomExperience());
            editor.putFloat("charisma", (float) userHero.getCharismaExperience());
            editor.apply();
        }
    }


//TODO graficzna obróbka wierszy
//TODO activity help, statistics
//TODO doświadczenie na poziom pomysł na doświadczenie liczone wg statystyk, każdy poziom statystyki == 100 exp w zależności od przelicznika klasy

    /**
     * Metoda wiążąca elementy interfejsu użytkownika ze zmiennymi wykorzystywanymi w kodzie.
     */
    private void joinComponentsWithVariable() {
        tClassName = (TextView) findViewById(R.id.textView_ClassName);
        tClassLevel = (TextView) findViewById(R.id.textView_ClassLevel);
        tExperience = (TextView) findViewById(R.id.textView_Experience);
        pBExperience = (ProgressBar) findViewById(R.id.progressBar_Experience);
        listView = (ListView) findViewById(R.id.listView_QuestList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // show menu when menu button is pressed
        //TODO opisać
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //TODO kod do włączania poszczególnych acticity
        int id = item.getItemId(); //pobranie id klikniętego pola

        switch (id) {
            case R.id.app_bar_add: {
                //kliknięcie w dodanie nowego zadania
                Intent intent = new Intent(this, QuestAdding.class);
                intent.putExtra("fileAddress", userAddQuest);
                startActivity(intent);
                break;
            }
            case R.id.app_bar_statistics: {
                //kliknięcie w wyświetlenie statystyk
                break;
            }
            case R.id.app_bar_option: {
                Intent intent = new Intent(this, OptionActivity.class);
                startActivity(intent);
                //kliknięcie w opcje
                break;
            }
            case R.id.app_bar_help: {
                //kliknięcie w pomoc
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void serviceObserveButtons(String order){
        String[] partsOfOrder = order.split(";");
        int position = Integer.parseInt(partsOfOrder[1]);
        if (partsOfOrder[0].equals("succeed")){
            String[] attributes = quests.get(position).getAtributes();
            for (int i = 0; i<attributes.length ; i++){
                if (attributes[i].equals(getText(R.string.attribute_strength))) userHero.addStrengthExperience(quests.get(position).getExperiencePoints()/attributes.length);
                if (attributes[i].equals(getText(R.string.attribute_endurance))) userHero.addEnduranceExperience(quests.get(position).getExperiencePoints()/attributes.length);
                if (attributes[i].equals(getText(R.string.attribute_dexterity))) userHero.addDexterityExperience(quests.get(position).getExperiencePoints()/attributes.length);
                if (attributes[i].equals(getText(R.string.attribute_intelligence))) userHero.addIntelligenceExperience(quests.get(position).getExperiencePoints()/attributes.length);
                if (attributes[i].equals(getText(R.string.attribute_wisdom))) userHero.addWisdomExperience(quests.get(position).getExperiencePoints()/attributes.length);
                if (attributes[i].equals(getText(R.string.attribute_charisma))) userHero.addCharismaExperience(quests.get(position).getExperiencePoints()/attributes.length);
            }
            if (quests.get(position).isRepeatable()){
                Calendar calendar = quests.get(position).getTimeToLiveDate();
                calendar.add(Calendar.DAY_OF_MONTH,quests.get(position).getRepeatInterval());
                quests.addLast(new Quest(quests.get(position).getDescription(),
                        quests.get(position).getExperiencePoints(),
                        calendar,
                        quests.get(position).getAtributes(),
                        quests.get(position).isRepeatable(),
                        quests.get(position).getRepeatInterval(),
                        this));
                Log.d("--------","Stworzyłem nowe zadanie");
            }

            quests.remove(position);
            Log.d("----------", "usunąłem stare zadanie");
        }else if (partsOfOrder[0].equals("failed")){
            if (quests.get(position).isRepeatable()){
                Calendar calendar = quests.get(position).getTimeToLiveDate();
                calendar.add(Calendar.DAY_OF_MONTH,quests.get(position).getRepeatInterval());
                quests.addLast(new Quest(quests.get(position).getDescription(),
                        quests.get(position).getExperiencePoints(),
                        calendar,
                        quests.get(position).getAtributes(),
                        quests.get(position).isRepeatable(),
                        quests.get(position).getRepeatInterval(),
                        this));
                Log.d("--------","Stworzyłem nowe zadanie");
            }

            quests.remove(position);
            Log.d("----------", "usunąłem stare zadanie");
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        Log.d("-------------------", o.toString());
        serviceObserveButtons(o.toString());
        adapterRefresh();
        refreshHeroInfo();
    }
}
