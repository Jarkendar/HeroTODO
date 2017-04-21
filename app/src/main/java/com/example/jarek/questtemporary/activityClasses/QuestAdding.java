package com.example.jarek.questtemporary.activityClasses;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jarek.questtemporary.R;
import com.example.jarek.questtemporary.dataClasses.FileManager;
import com.example.jarek.questtemporary.dataClasses.Quest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class QuestAdding extends AppCompatActivity {

    private LinkedList<Quest> quests = new LinkedList<>();

    private TextView tvdescription,  tvendDate, tvinterval, tvattributes, tvcorrectField;
    private CheckBox repeatable, strength, endurance, dexterity, intelligence, wisdom, charisma;
    private EditText description,  endDate, interval;
    private Button addQuest;
    private Spinner experienceMultiplier;

    private long firstClickBack = 0;
    private final String sharedName = "userInfoShared";
    private String userAddFileAddress;//adres do serializacji obiektu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_adding);

        getBundleExtras();

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(getText(R.string.text_add));
        actionBar.setDisplayHomeAsUpEnabled(true);

        joinComponentsWithVariable();

        repeatable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (repeatable.isChecked()) {
                    repeatable.setText(getText(R.string.text_yes));
                    interval.setHint(getText(R.string.hint_canWrite));
                    interval.setEnabled(true);
                } else {
                    repeatable.setText(getText(R.string.text_no));
                    interval.setHint(getText(R.string.hint_noEnable));
                    interval.setEnabled(false);
                }
            }
        });
    }

    /**
     * Metoda pobierająca wszystkie przekazane wartości z activity wywołującej.
     */
    private void getBundleExtras() {
        Bundle bundle = getIntent().getExtras();
        userAddFileAddress = bundle.getString("fileAddress");
    }

    /**
     * Metoda włączana automatycznie gdy użytkownik kliknie w przycisk w actionBar.
     *
     * @param item id przycisku, w który użytkownik kliknął
     * @return ?
     */
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

    /**
     * Metoda automatycznie włączana gdy użytkownik naciśnie przycisk back telefonu.
     * W tej aplikacji dodatkowo sprawdzam czy użytkownik na pewno chce się cofnąć czy może przez
     * przypadek kliknął w przycisk back. Użytkownik na potwierdzenie wyboru ma 3s. Po pierwszym
     * kliknięciu użytkownik dostaje infromacje o dalszym kroku, gdy chce na prawdę się cofnąć.
     */
    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), getText(R.string.text_allert_back), Toast.LENGTH_SHORT).show();
        if (System.currentTimeMillis() - firstClickBack <= 3000) {
            super.onBackPressed();
        } else {
            firstClickBack = System.currentTimeMillis();
        }
    }

    /**
     * Metoda łączy elementy interfejsu użytkownika ze zmiennymi wykorzystywanymi w kodzie.
     */
    private void joinComponentsWithVariable() {
        tvdescription = (TextView) findViewById(R.id.textView_Description);
        tvendDate = (TextView) findViewById(R.id.textView_Date);
        tvcorrectField = (TextView) findViewById(R.id.textView_CorrectField);
        tvinterval = (TextView) findViewById(R.id.textView_Interval);
        tvattributes = (TextView) findViewById(R.id.textView_Attribute);

        strength = (CheckBox) findViewById(R.id.checkBox_Strength);
        endurance = (CheckBox) findViewById(R.id.checkBox_Endurance);
        dexterity = (CheckBox) findViewById(R.id.checkBox_DeXterity);
        intelligence = (CheckBox) findViewById(R.id.checkBox_Intelligence);
        wisdom = (CheckBox) findViewById(R.id.checkBox_Wisdom);
        charisma = (CheckBox) findViewById(R.id.checkBox_Charisma);
        repeatable = (CheckBox) findViewById(R.id.checkBox_Repeatable);

        description = (EditText) findViewById(R.id.editText_Description);
        endDate = (EditText) findViewById(R.id.editText_Date);
        interval = (EditText) findViewById(R.id.editText_Interval);

        addQuest = (Button) findViewById(R.id.button_AddQuest);

        experienceMultiplier = (Spinner) findViewById(R.id.spinner_Level);
    }

    /**
     * Metoda sprawdzająca wypełnienie pola opisującego zadanie.
     *
     * @return true jeśli jest wypełnione, false jeśli jest puste
     */
    private boolean checkDescriptionField() {
        if (description.getText().toString().length() == 0) {
            tvdescription.setTextColor(getResources().getColor(R.color.color_Red));
            description.setText("");
            description.setHint(R.string.hint_fieldMustBeFill);
            return false;
        } else {
            tvdescription.setTextColor(getResources().getColor(R.color.color_Black));
            return true;
        }
    }

//    /**
//     * Metoda sprawdzająca zawartość pola zawierającego informacje o średniej ilości doświadczenia
//     * którą możę otrzymać użytkownik.
//     *
//     * @return true jeśli pole nie jest puste oraz zawartość pola jest większa od zera, false w przeciwnym razie
//     */
//    private boolean checkExperienceField() {
//        if (avgExperience.getText().toString().length() == 0 || Integer.parseInt(avgExperience.getText().toString()) <= 0) {
//            tvavgExperience.setTextColor(getResources().getColor(R.color.color_Red));
//            avgExperience.setText("");
//            avgExperience.setHint(R.string.hint_failedData);
//            return false;
//        } else {
//            tvavgExperience.setTextColor(getResources().getColor(R.color.color_Black));
//            return true;
//        }
//    }

    /**
     * Metoda sprawdzająca czy data jest w poprawnym formacie dd-MM-yyyy.
     *
     * @return true jeśli data jest w odpowiednim formaci, false jeśli zawiera błąd
     */
    private boolean checkDateField() {
        if (endDate.getText().toString().length() == 0 || endDate.getText().toString().length() != 10) {
            return false;
        } else {
            String[] dateTab = endDate.getText().toString().split("-");
            if (dateTab.length < 3 || dateTab.length > 3) {
                return false;
            } else {
                if (Integer.parseInt(dateTab[2]) < 2000 || Integer.parseInt(dateTab[2]) > 2100) {
                    return false;
                } else {
                    int valueMonth = Integer.parseInt(dateTab[1]);
                    int modulo2Month = Integer.parseInt(dateTab[1]) % 2;
                    if (valueMonth > 12 || valueMonth <= 0 || Integer.parseInt(dateTab[0]) == 0) {
                        return false;
                    } else if (valueMonth == 2) {
                        return !((Integer.parseInt(dateTab[0]) > 28 && Integer.parseInt(dateTab[2]) % 4 != 0)
                                || (Integer.parseInt(dateTab[0]) > 29 && Integer.parseInt(dateTab[2]) % 4 == 0));
                    } else if (valueMonth <= 7 && modulo2Month == 1) {
                        return Integer.parseInt(dateTab[0]) <= 31;
                    } else if (valueMonth <= 7 && modulo2Month == 0) {
                        return Integer.parseInt(dateTab[0]) <= 30;
                    } else if (valueMonth > 7 && modulo2Month == 1) {
                        return Integer.parseInt(dateTab[0]) <= 30;
                    } else {
                        return Integer.parseInt(dateTab[0]) <= 31;
                    }
                }
            }
        }
    }

    /**
     * Metoda obsługująca błędny format daty. Pełni też rolę pośrednika w przekazywaniu informacji
     * o poprawności danych.
     *
     * @return true jeśli data jest w odpowiednim formaci, false jeśli zawiera błąd
     */
    private boolean dateFieldOperating() {
        if (checkDateField()) {
            tvendDate.setTextColor(getResources().getColor(R.color.color_Black));
            return true;
        } else {
            tvendDate.setTextColor(getResources().getColor(R.color.color_Red));
            endDate.setText("");
            endDate.setHint(R.string.hint_dateFormat);
            return false;
        }
    }

    /**
     * Metoda sprawdzająca poprawność formatu pola interwał, jeśli checkBox repeatable jest odznaczony.
     *
     * @return true jeśli nie jest puste oraz jest większe od zera, false w przeciwnym razie
     */
    private boolean checkIntervalField() {
        if (repeatable.isChecked() && (interval.getText().toString().length() == 0 || Integer.parseInt(interval.getText().toString()) <= 0)) {
            tvinterval.setTextColor(getResources().getColor(R.color.color_Red));
            interval.setText("");
            interval.setHint(R.string.hint_failedData);
            return false;
        } else {
            tvinterval.setTextColor(getResources().getColor(R.color.color_Black));
            return true;
        }
    }

    private boolean checkAttributesCheckBox(){
        int countActivateCheckBox = 0;

        if (strength.isChecked()) countActivateCheckBox++;
        if (endurance.isChecked()) countActivateCheckBox++;
        if (dexterity.isChecked()) countActivateCheckBox++;
        if (intelligence.isChecked()) countActivateCheckBox++;
        if (wisdom.isChecked()) countActivateCheckBox++;
        if (charisma.isChecked()) countActivateCheckBox++;

        if (countActivateCheckBox == 0){
            tvattributes.setTextColor(getResources().getColor(R.color.color_Red));
            return false;
        }else{
            tvattributes.setTextColor(getResources().getColor(R.color.color_Black));
            return true;
        }
    }

    private String[] getAttributeFromCheckBox(){
        LinkedList<String> attribute = new LinkedList<>();
        if (strength.isChecked()) attribute.addLast(getResources().getText(R.string.attribute_strength).toString());
        if (endurance.isChecked()) attribute.addLast(getResources().getText(R.string.attribute_endurance).toString());
        if (dexterity.isChecked()) attribute.addLast(getResources().getText(R.string.attribute_dexterity).toString());
        if (intelligence.isChecked()) attribute.addLast(getResources().getText(R.string.attribute_intelligence).toString());
        if (wisdom.isChecked()) attribute.addLast(getResources().getText(R.string.attribute_wisdom).toString());
        if (charisma.isChecked()) attribute.addLast(getResources().getText(R.string.attribute_charisma).toString());
        return attribute.toArray(new String[0]);
    }

    /**
     * Metoda obsługująca sprawdzanie wszsytkich pól.
     *
     * @return true jeśli wszystkie pola są prawidłowe, false jeśli któregolwiek pole jest źle wypełnione
     */
    private boolean checkCorrectAllData() {
        boolean check[] = new boolean[4];
        check[0] = checkDescriptionField();
        check[1] = dateFieldOperating();
        check[2] = checkIntervalField();
        check[3] = checkAttributesCheckBox();

        for (boolean x : check) {
            if (!x) return false;
        }
        return true;
    }

    private Calendar getDateFromString(String date){
        String[] fieldOfDate = date.split("-");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,Integer.parseInt(fieldOfDate[2]));
        calendar.set(Calendar.MONTH,Integer.parseInt(fieldOfDate[1])-1);
        calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(fieldOfDate[0]));
        return calendar;
    }

    private double getExperience(String nameMul){
        String[] params = getResources().getStringArray(R.array.level_Hierarchy);
        if (nameMul.equals(params[0])) return Double.parseDouble(params[0].substring(params[0].length()-4,params[0].length()-1));
        if (nameMul.equals(params[1])) return Double.parseDouble(params[1].substring(params[1].length()-4,params[1].length()-1));
        if (nameMul.equals(params[2])) return Double.parseDouble(params[2].substring(params[2].length()-4,params[2].length()-1));
        if (nameMul.equals(params[3])) return Double.parseDouble(params[3].substring(params[3].length()-4,params[3].length()-1));
        if (nameMul.equals(params[4])) return Double.parseDouble(params[4].substring(params[4].length()-4,params[4].length()-1));
        return 0.0;
    }

    /**
     * Metoda czyszcząca pola, powrót do ustawień domyślnych.
     */
    private void cleanField(){
        description.setText("");
        description.setHint("");
        endDate.setText("");
        endDate.setHint(R.string.hint_dateFormat);
        repeatable.setChecked(false);
        interval.setText("");
        interval.setHint(R.string.hint_noEnable);
        strength.setChecked(false);
        endurance.setChecked(false);
        dexterity.setChecked(false);
        intelligence.setChecked(false);
        wisdom.setChecked(false);
        charisma.setChecked(false);
    }

    /**
     * Metoda obsługująca kliknięcie w przycisk dodawania nowego zadani.
     *
     * @param view element wywołujący metodę
     */
    public void clickAddQuest(View view) {
        if (!checkCorrectAllData()) {
            tvcorrectField.setVisibility(View.VISIBLE);
        } else {
            tvcorrectField.setVisibility(View.INVISIBLE);
            int intervalNumber;
            if (interval.getText().toString().length() != 0) {
                intervalNumber = Integer.parseInt(interval.getText().toString());
            }else {
                intervalNumber = 0;
            }
            Quest quest = new Quest(description.getText().toString()
                    , getExperience(experienceMultiplier.getSelectedItem().toString())
                    , getDateFromString(endDate.getText().toString())
                    , getAttributeFromCheckBox()
                    , repeatable.isChecked()
                    , intervalNumber,this);
            quests.addLast(quest);
            FileManager filemanager = new FileManager();
            filemanager.serializationQuests(quests, userAddFileAddress, getApplicationContext());
            SharedPreferences shared = getSharedPreferences(sharedName,MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();
            editor.putBoolean("addQuests",true);
            editor.apply();
            Toast.makeText(getApplicationContext(),getResources().getText(R.string.text_questWasAdd),Toast.LENGTH_SHORT).show();
            cleanField();
        }
    }
}
