package com.example.jarek.questtemporary.activityClasses;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jarek.questtemporary.R;
import com.example.jarek.questtemporary.dataClasses.FileManager;
import com.example.jarek.questtemporary.dataClasses.Quest;

import java.util.Calendar;
import java.util.LinkedList;

public class QuestForm extends AppCompatActivity {

    private LinkedList<Quest> quests = new LinkedList<>();

    private String mode = "";

    private TextView tvdescription, tvendDate, tvinterval, tvattributes, tvcorrectField;
    private CheckBox checkBoxRepeatable, checkBoxstrength, checkBoxendurance, checkBoxdexterity,
            checkBoxintelligence, checkBoxwisdom, checkBoxcharisma;
    private EditText editTextdescription, editTextinterval;
    private Button buttonaddQuest, buttonDate;
    private Spinner spinnerexperienceMultiplier;

    private long firstClickBack = 0;
    private final String sharedName = "userInfoShared";
    private String userAddFileAddress;//adres do serializacji obiektu
    private Calendar chooseDate;
    private DatePickerDialog datePickerDialog;

    private int yearDate, monthDate, dayDate;
    static final int DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_form);

        joinComponentsWithVariable();
        getBundleExtras();
        makeDatePickerDialog();

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        if (mode.equals("modifyQuest")) {
            actionBar.setTitle(getText(R.string.text_modify));
            buttonaddQuest.setText(getText(R.string.text_modify));
        } else {
            actionBar.setTitle(getText(R.string.text_add));
            buttonaddQuest.setText(getText(R.string.text_add));
        }
        actionBar.setDisplayHomeAsUpEnabled(true);

        setListeners();

        buttonDate.setText(dayDate+"-"+(monthDate+1)+"-"+yearDate);
    }

    private void setListeners(){
        buttonDate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog(DIALOG_ID);
                    }
                }
        );
        checkBoxRepeatable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBoxRepeatable.isChecked()) {
                    checkBoxRepeatable.setText(getText(R.string.text_yes));
                    editTextinterval.setHint(getText(R.string.hint_canWriteDayNumber));
                    editTextinterval.setEnabled(true);
                } else {
                    checkBoxRepeatable.setText(getText(R.string.text_no));
                    editTextinterval.setHint(getText(R.string.hint_noEnable));
                    editTextinterval.setEnabled(false);
                }
            }
        });
        tvendDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        spinnerexperienceMultiplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] params = getResources().getStringArray(R.array.level_Hierarchy);
                if (spinnerexperienceMultiplier.getSelectedItem().toString().equals(params[5])){
                    checkBoxRepeatable.setText(getText(R.string.text_no));
                    checkBoxRepeatable.setChecked(false);
                    checkBoxRepeatable.setEnabled(false);
                }else {
                    checkBoxRepeatable.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setDefaultDate(){
        Calendar calendar = Calendar.getInstance();
        yearDate = calendar.get(Calendar.YEAR);
        monthDate = calendar.get(Calendar.MONTH);
        dayDate = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID){
            return new DatePickerDialog(this,datePickerListener,yearDate,monthDate,dayDate);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            yearDate = year;
            monthDate = month;
            dayDate = day;
            buttonDate.setText(dayDate+"-"+(monthDate+1)+"-"+yearDate);
        }
    };

    private void makeDatePickerDialog (){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                chooseDate = Calendar.getInstance();
                chooseDate.set(Calendar.YEAR, i);
                chooseDate.set(Calendar.MONTH,i1);
                chooseDate.set(Calendar.DAY_OF_MONTH,i2);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int a = calendar.get(Calendar.YEAR);
        int b =calendar.get(Calendar.MONTH);
        int c =calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(getApplicationContext(),dateSetListener,a,b,c);
    }

    /**
     * Metoda pobierająca wszystkie przekazane wartości z activity wywołującej.
     */
    private void getBundleExtras() {
        Bundle bundle = getIntent().getExtras();
        userAddFileAddress = bundle.getString("fileAddress");
        mode = bundle.getString("whatdo");
        setDefaultDate();
        if (mode != null && mode.equals("modifyQuest")) {
            editTextdescription.setText(bundle.getString("description"));
            double mExperience = bundle.getDouble("experience");
            int pos = 0;
            if (mExperience == 5.0) pos = 0;
            if (mExperience == 3.0) pos = 1;
            if (mExperience == 2.0) pos = 2;
            if (mExperience == 1.0) pos = 3;
            if (mExperience == 0.5) pos = 4;
            if (mExperience == 100.0) pos = 5;
            spinnerexperienceMultiplier.setSelection(pos);
            String attributes = bundle.getString("attributes");
            assert attributes != null;
            String[] mAttributes = attributes.split(";");
            for (String x : mAttributes) {
                if (x.equals(getString(R.string.attribute_strength)))
                    checkBoxstrength.setChecked(true);
                if (x.equals(getString(R.string.attribute_endurance)))
                    checkBoxendurance.setChecked(true);
                if (x.equals(getString(R.string.attribute_dexterity)))
                    checkBoxdexterity.setChecked(true);
                if (x.equals(getString(R.string.attribute_intelligence)))
                    checkBoxintelligence.setChecked(true);
                if (x.equals(getString(R.string.attribute_wisdom))) checkBoxwisdom.setChecked(true);
                if (x.equals(getString(R.string.attribute_charisma)))
                    checkBoxcharisma.setChecked(true);
            }
            yearDate = bundle.getInt("yearDate");
            monthDate = bundle.getInt("monthDate");
            dayDate = bundle.getInt("dayDate");
            buttonDate.setText(dayDate+"-"+(monthDate+1)+"-"+yearDate);
            checkBoxRepeatable.setChecked(bundle.getBoolean("repeatable"));
            if (checkBoxRepeatable.isChecked()) {
                checkBoxRepeatable.setText(getText(R.string.text_yes));
                editTextinterval.setEnabled(true);
            }
            editTextinterval.setText(String.valueOf(bundle.getInt("interval")));
        }

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
     * Metoda łączy elementy interfejsu użytkownika ze zmiennymi wykorzystywanymi w kodzie.
     */
    private void joinComponentsWithVariable() {
        tvdescription = (TextView) findViewById(R.id.textView_Description);
        tvendDate = (TextView) findViewById(R.id.textView_Date);
        tvcorrectField = (TextView) findViewById(R.id.textView_CorrectField);
        tvinterval = (TextView) findViewById(R.id.textView_Interval);
        tvattributes = (TextView) findViewById(R.id.textView_Attribute);

        checkBoxstrength = (CheckBox) findViewById(R.id.checkBox_Strength);
        checkBoxendurance = (CheckBox) findViewById(R.id.checkBox_Endurance);
        checkBoxdexterity = (CheckBox) findViewById(R.id.checkBox_DeXterity);
        checkBoxintelligence = (CheckBox) findViewById(R.id.checkBox_Intelligence);
        checkBoxwisdom = (CheckBox) findViewById(R.id.checkBox_Wisdom);
        checkBoxcharisma = (CheckBox) findViewById(R.id.checkBox_Charisma);
        checkBoxRepeatable = (CheckBox) findViewById(R.id.checkBox_Repeatable);

        editTextdescription = (EditText) findViewById(R.id.editText_Description);
        editTextinterval = (EditText) findViewById(R.id.editText_Interval);

        buttonDate = (Button)findViewById(R.id.button_DateDialog);
        buttonaddQuest = (Button) findViewById(R.id.button_AddQuest);

        spinnerexperienceMultiplier = (Spinner) findViewById(R.id.spinner_Level);

    }

    /**
     * Metoda sprawdzająca wypełnienie pola opisującego zadanie.
     *
     * @return true jeśli jest wypełnione, false jeśli jest puste
     */
    private boolean checkDescriptionField() {
        if (editTextdescription.getText().toString().length() == 0) {
            tvdescription.setTextColor(getResources().getColor(R.color.color_Red));
            editTextdescription.setText("");
            editTextdescription.setHint(R.string.hint_fieldMustBeFill);
            return false;
        } else {
            tvdescription.setTextColor(getResources().getColor(R.color.color_Black));
            return true;
        }
    }

    /**
     * Metoda sprawdzająca poprawność formatu pola interwał, jeśli checkBox checkBoxRepeatable jest odznaczony.
     *
     * @return true jeśli nie jest puste oraz jest większe od zera, false w przeciwnym razie
     */
    private boolean checkIntervalField() {
        if (checkBoxRepeatable.isChecked() && (editTextinterval.getText().toString().length() == 0 || Integer.parseInt(editTextinterval.getText().toString()) <= 0)) {
            tvinterval.setTextColor(getResources().getColor(R.color.color_Red));
            editTextinterval.setText("");
            editTextinterval.setHint(R.string.hint_failedData);
            return false;
        } else {
            tvinterval.setTextColor(getResources().getColor(R.color.color_Black));
            return true;
        }
    }

    private boolean checkAttributesCheckBox() {
        int countActivateCheckBox = 0;

        if (checkBoxstrength.isChecked()) countActivateCheckBox++;
        if (checkBoxendurance.isChecked()) countActivateCheckBox++;
        if (checkBoxdexterity.isChecked()) countActivateCheckBox++;
        if (checkBoxintelligence.isChecked()) countActivateCheckBox++;
        if (checkBoxwisdom.isChecked()) countActivateCheckBox++;
        if (checkBoxcharisma.isChecked()) countActivateCheckBox++;

        if (countActivateCheckBox == 0) {
            tvattributes.setTextColor(getResources().getColor(R.color.color_Red));
            return false;
        } else {
            tvattributes.setTextColor(getResources().getColor(R.color.color_Black));
            return true;
        }
    }

    private String[] getAttributeFromCheckBox() {
        LinkedList<String> attribute = new LinkedList<>();
        if (checkBoxstrength.isChecked())
            attribute.addLast(getResources().getText(R.string.attribute_strength).toString());
        if (checkBoxendurance.isChecked())
            attribute.addLast(getResources().getText(R.string.attribute_endurance).toString());
        if (checkBoxdexterity.isChecked())
            attribute.addLast(getResources().getText(R.string.attribute_dexterity).toString());
        if (checkBoxintelligence.isChecked())
            attribute.addLast(getResources().getText(R.string.attribute_intelligence).toString());
        if (checkBoxwisdom.isChecked())
            attribute.addLast(getResources().getText(R.string.attribute_wisdom).toString());
        if (checkBoxcharisma.isChecked())
            attribute.addLast(getResources().getText(R.string.attribute_charisma).toString());
        return attribute.toArray(new String[0]);
    }

    /**
     * Metoda obsługująca sprawdzanie wszsytkich pól.
     *
     * @return true jeśli wszystkie pola są prawidłowe, false jeśli któregolwiek pole jest źle wypełnione
     */
    private boolean checkCorrectAllData() {
        boolean check[] = new boolean[3];
        check[0] = checkDescriptionField();
        check[1] = checkIntervalField();
        check[2] = checkAttributesCheckBox();

        for (boolean x : check) {
            if (!x) return false;
        }
        return true;
    }

    private Calendar getCalendarDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, yearDate);
        calendar.set(Calendar.MONTH, monthDate);
        calendar.set(Calendar.DAY_OF_MONTH, dayDate);
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar;
    }

    private double getExperience(String nameMul) {
        String[] params = getResources().getStringArray(R.array.level_Hierarchy);
        if (nameMul.equals(params[0]))
            return getNumberFromDifficultyLevel(params[0]);
        if (nameMul.equals(params[1]))
            return getNumberFromDifficultyLevel(params[1]);
        if (nameMul.equals(params[2]))
            return getNumberFromDifficultyLevel(params[2]);
        if (nameMul.equals(params[3]))
            return getNumberFromDifficultyLevel(params[3]);
        if (nameMul.equals(params[4]))
            return getNumberFromDifficultyLevel(params[4]);
        if (nameMul.equals(params[5]))
            return getNumberFromDifficultyLevel(params[5]);
        return 0.0;
    }


    private double getNumberFromDifficultyLevel(String difficultyLevel){
        String[] tmp = difficultyLevel.split("\\+");
        return Double.parseDouble(tmp[1].substring(0,tmp[1].length()-1));
    }


    /**
     * Metoda czyszcząca pola, powrót do ustawień domyślnych.
     */
    private void cleanField() {
        editTextdescription.setText("");
        editTextdescription.setHint("");
        setDefaultDate();
        buttonDate.setText(dayDate+"-"+(monthDate+1)+"-"+yearDate);
        checkBoxRepeatable.setChecked(false);
        editTextinterval.setText("");
        editTextinterval.setHint(R.string.hint_noEnable);
        checkBoxstrength.setChecked(false);
        checkBoxendurance.setChecked(false);
        checkBoxdexterity.setChecked(false);
        checkBoxintelligence.setChecked(false);
        checkBoxwisdom.setChecked(false);
        checkBoxcharisma.setChecked(false);
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
            if (editTextinterval.getText().toString().length() != 0) {
                intervalNumber = Integer.parseInt(editTextinterval.getText().toString());
            } else {
                intervalNumber = 0;
            }
            Quest quest = new Quest(editTextdescription.getText().toString()
                    , getExperience(spinnerexperienceMultiplier.getSelectedItem().toString())
                    , getCalendarDate()
                    , getAttributeFromCheckBox()
                    , checkBoxRepeatable.isChecked()
                    , intervalNumber, this);
            quests.addLast(quest);
            FileManager filemanager = new FileManager();
            filemanager.serializationQuests(quests, userAddFileAddress, getApplicationContext());
            SharedPreferences shared = getSharedPreferences(sharedName, MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();
            if (mode.equals("modifyQuest")) {
                editor.putBoolean("addQuest", false);
                editor.putBoolean("modify", true);
                editor.apply();
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.text_questWasModify), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                editor.putBoolean("addQuests", true);
                editor.apply();
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.text_questWasAdd), Toast.LENGTH_SHORT).show();
                cleanField();
            }
        }
    }
}
