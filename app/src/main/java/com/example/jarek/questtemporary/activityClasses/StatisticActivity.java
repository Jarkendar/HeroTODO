package com.example.jarek.questtemporary.activityClasses;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jarek.questtemporary.R;

public class StatisticActivity extends AppCompatActivity {

    private TextView textViewStrength, textViewEndurance, textViewDexterity, textViewIntelligence
            ,textViewWisdom, textViewCharisma;
    private ProgressBar progressBarStrenght, progressBarEndurance, progressBarDexterity
            ,progressBarIntelligence, progressBarWisdom, progressBarCharisma;

    private double strength, endurance, dexterity, intelligence, wisdom, charisma;
    private long firstClickBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(R.string.text_statistics);
        actionBar.setDisplayHomeAsUpEnabled(true);

        joinComponentsWithVariable();
        getBundleExtras();
        setLevelTextViews();
        setProgressBars();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                onBackPressed();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), getText(R.string.text_allert_back), Toast.LENGTH_SHORT).show();
        if (System.currentTimeMillis() - firstClickBack <= 3000) {
            super.onBackPressed();
        } else {
            firstClickBack = System.currentTimeMillis();
        }
        super.onBackPressed();
    }

    private void setProgressBars(){
        progressBarStrenght.setProgress((int)strength%100);
        progressBarEndurance.setProgress((int)endurance%100);
        progressBarDexterity.setProgress((int)dexterity%100);
        progressBarIntelligence.setProgress((int)intelligence%100);
        progressBarWisdom.setProgress((int)wisdom%100);
        progressBarCharisma.setProgress((int)charisma%100);
    }

    private void setLevelTextViews(){
        textViewStrength.setText(((int)strength/100)+" "+getText(R.string.text_level));
        textViewEndurance.setText(((int)endurance/100)+" "+getText(R.string.text_level));
        textViewDexterity.setText(((int)dexterity/100)+" "+getText(R.string.text_level));
        textViewIntelligence.setText(((int)intelligence/100)+" "+getText(R.string.text_level));
        textViewWisdom.setText(((int)wisdom/100)+" "+getText(R.string.text_level));
        textViewCharisma.setText(((int)charisma/100)+" "+getText(R.string.text_level));
    }

    private void getBundleExtras() {
        Bundle bundle = getIntent().getExtras();
        strength = (double)bundle.getFloat("strength");
        endurance = (double)bundle.getFloat("endurance");
        dexterity = (double)bundle.getFloat("dexterity");
        intelligence = (double)bundle.getFloat("intelligence");
        wisdom = (double)bundle.getFloat("wisdom");
        charisma = (double)bundle.getFloat("charisma");
    }

    private void joinComponentsWithVariable(){
        textViewStrength = (TextView)findViewById(R.id.textView_StatisticStrengthLevel);
        textViewEndurance = (TextView)findViewById(R.id.textView_StatisticEnduranceLevel);
        textViewDexterity = (TextView)findViewById(R.id.textView_StatisticDexterityLevel);
        textViewIntelligence = (TextView)findViewById(R.id.textView_StatisticIntelligenceLevel);
        textViewWisdom = (TextView)findViewById(R.id.textView_StatisticWisdomLevel);
        textViewCharisma = (TextView)findViewById(R.id.textView_StatisticCharismaLevel);

        progressBarStrenght = (ProgressBar)findViewById(R.id.progressBar_StatisticStrength);
        progressBarEndurance = (ProgressBar)findViewById(R.id.progressBar_StatisticEndurance);
        progressBarDexterity = (ProgressBar)findViewById(R.id.progressBar_StatisticDexterity);
        progressBarIntelligence = (ProgressBar)findViewById(R.id.progressBar_StatisticIntelligence);
        progressBarWisdom = (ProgressBar)findViewById(R.id.progressBar_StatisticWisdom);
        progressBarCharisma = (ProgressBar)findViewById(R.id.progressBar_StatisticCharisma);
    }
}
