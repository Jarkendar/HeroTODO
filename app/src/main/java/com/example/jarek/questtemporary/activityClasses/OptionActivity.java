package com.example.jarek.questtemporary.activityClasses;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jarek.questtemporary.R;

public class OptionActivity extends AppCompatActivity {

    private int heroClass;
    private SharedPreferences sharedPreferences;

    private TextView tvClass;
    private Button buttonselectClass, buttonaccept;
    private Spinner spinnerlistClass;

    private final String heroShared = "heroShared";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(getText(R.string.text_option));
        actionBar.setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences(heroShared, MODE_PRIVATE);
        heroClass = sharedPreferences.getInt("heroClass", R.string.class_native);

        joinComponentsWithVariable();

        tvClass.setText(getString(heroClass));
        enableSelectComponent();
    }

    private void enableSelectComponent() {
        if (heroClass == R.string.class_native) {
            buttonselectClass.setEnabled(true);
            tvClass.setVisibility(View.GONE);
        } else {
            buttonselectClass.setEnabled(false);
            tvClass.setVisibility(View.VISIBLE);
        }
        spinnerlistClass.setVisibility(View.GONE);
        buttonaccept.setVisibility(View.GONE);
    }

    private void joinComponentsWithVariable() {
        tvClass = (TextView) findViewById(R.id.textView_selectClass);

        buttonselectClass = (Button) findViewById(R.id.button_selectClass);
        buttonaccept = (Button) findViewById(R.id.button_accept);

        spinnerlistClass = (Spinner) findViewById(R.id.spinner_Class);
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
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void enableSelectClass(View view) {
        spinnerlistClass.setVisibility(View.VISIBLE);
        buttonaccept.setVisibility(View.VISIBLE);
    }

    private void saveChooseClass(String newHeroClass, int newHeroClassID) {
        tvClass.setText(newHeroClass);
        buttonselectClass.setEnabled(false);
        buttonaccept.setVisibility(View.GONE);
        spinnerlistClass.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), getResources().getText(R.string.text_selectedClass), Toast.LENGTH_LONG).show();
        if (newHeroClass.equals(getString(R.string.class_bard)))
            newHeroClassID = R.string.class_bard;
        else if (newHeroClass.equals(getString(R.string.class_hunter)))
            newHeroClassID = R.string.class_hunter;
        else if (newHeroClass.equals(getString(R.string.class_lord)))
            newHeroClassID = R.string.class_lord;
        else if (newHeroClass.equals(getString(R.string.class_mage)))
            newHeroClassID = R.string.class_mage;
        else if (newHeroClass.equals(getString(R.string.class_merchant)))
            newHeroClassID = R.string.class_merchant;
        else if (newHeroClass.equals(getString(R.string.class_warrior)))
            newHeroClassID = R.string.class_warrior;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("heroClass", newHeroClassID);
        editor.apply();
    }

    public void selectClass(View view) {
        final String newHeroClass = spinnerlistClass.getSelectedItem().toString();
        final int newHeroClassID = R.string.class_native;
        new AlertDialog.Builder(this)
                .setTitle(getText(R.string.text_confirm))//tytuł
                .setMessage(getString(R.string.text_areYouSureThisClass) + " " + newHeroClass + " ? " +
                        getString(R.string.text_laterChangeIsNotPossible))
                //opis
                .setPositiveButton(getText(R.string.text_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveChooseClass(newHeroClass, newHeroClassID);
                        tvClass.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton(getText(R.string.text_no), null)//kliknięcie NIE nic nie robi
                .show();//pokaż

    }

    private void deleteAllProgress() {
        SharedPreferences sharedPreferences = getSharedPreferences(heroShared, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("heroClass", R.string.class_native);
        editor.putFloat("strength", 0);
        editor.putFloat("endurance", 0);
        editor.putFloat("dexterity", 0);
        editor.putFloat("intelligence", 0);
        editor.putFloat("wisdom", 0);
        editor.putFloat("charisma", 0);
        editor.apply();
        heroClass = sharedPreferences.getInt("heroClass", R.string.class_native);
        enableSelectComponent();
    }

    public void restartAll(View view) {
        new AlertDialog.Builder(this)
                .setTitle(getText(R.string.text_confirm))//tytuł
                .setMessage(getString(R.string.text_areYouSureRestart))
                //opis
                .setPositiveButton(getText(R.string.text_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAllProgress();
                    }
                })
                .setNegativeButton(getText(R.string.text_no), null)//kliknięcie NIE nic nie robi
                .show();//pokaż
    }
}
