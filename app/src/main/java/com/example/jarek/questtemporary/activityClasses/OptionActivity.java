package com.example.jarek.questtemporary.activityClasses;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jarek.questtemporary.R;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

public class OptionActivity extends AppCompatActivity {

    private long firstClickBack = 0;
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

        sharedPreferences =getSharedPreferences(heroShared,MODE_PRIVATE);
        heroClass = sharedPreferences.getInt("heroClass",R.string.class_native);

        joinComponentsWithVariable();

        tvClass.setText(getString(heroClass));
        enableSelectComponent();
    }

    private void enableSelectComponent(){
        if (heroClass == R.string.class_native){
            buttonselectClass.setEnabled(true);

        }else {
            buttonselectClass.setEnabled(false);
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

    public void enableSelectClass(View view) {
        spinnerlistClass.setVisibility(View.VISIBLE);
        buttonaccept.setVisibility(View.VISIBLE);
    }

    public void selectClass(View view) {
        String newHeroClass = spinnerlistClass.getSelectedItem().toString();
        int newHeroClassID = R.string.class_native;
        tvClass.setText(newHeroClass);
        buttonselectClass.setEnabled(false);
        buttonaccept.setVisibility(View.GONE);
        spinnerlistClass.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(),getResources().getText(R.string.text_selectedClass),Toast.LENGTH_LONG).show();
        if (newHeroClass.equals(getString(R.string.class_bard))) newHeroClassID = R.string.class_bard;
        else if (newHeroClass.equals(getString(R.string.class_hunter))) newHeroClassID = R.string.class_hunter;
        else if (newHeroClass.equals(getString(R.string.class_lord))) newHeroClassID = R.string.class_lord;
        else if (newHeroClass.equals(getString(R.string.class_mage))) newHeroClassID = R.string.class_mage;
        else if (newHeroClass.equals(getString(R.string.class_merchant))) newHeroClassID = R.string.class_merchant;
        else if (newHeroClass.equals(getString(R.string.class_warrior))) newHeroClassID = R.string.class_warrior;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("heroClass",newHeroClassID);
        editor.apply();
    }

}
