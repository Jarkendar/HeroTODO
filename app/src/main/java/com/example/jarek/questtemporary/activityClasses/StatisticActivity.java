package com.example.jarek.questtemporary.activityClasses;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jarek.questtemporary.R;

public class StatisticActivity extends AppCompatActivity {

    private TextView textViewStrength, textViewEndurance, textViewDexterity, textViewIntelligence
            ,textViewWisdom, textViewCharisma;
    private ProgressBar progressBarStrenght, progressBarEndurance, progressBarDexterity
            ,progressBarIntelligence, progressBarWisdom, progressBarCharisma;
    private ImageView imageView;

    private double strength, endurance, dexterity, intelligence, wisdom, charisma;

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
        drawHexStats();
    }

    private void drawHexStats(){
        int x = 200;//X środka
        int y = 120;//Y środka
        int a = 100;//promień największego hexa
        int b = 66;//promień średniego hexa
        int c = 33;//promień najmniejszego hexa

        Bitmap bitmap = Bitmap.createBitmap(400,250, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint painter = new Paint();
        //przekątne
        canvas.drawLine(x,y-a,x,y+a,painter);
        canvas.drawLine(x-(float)(a *Math.sqrt(3)/2),y-a/2,x+(float)(a *Math.sqrt(3)/2),y+a/2,painter);
        canvas.drawLine(x-(float)(a *Math.sqrt(3)/2),y+a/2,x+(float)(a *Math.sqrt(3)/2),y-a/2,painter);
        //największy hex
        canvas.drawLine(x,y-a,x+(float)(a *Math.sqrt(3)/2),y-a/2,painter);
        canvas.drawLine(x+(float)(a *Math.sqrt(3)/2),y-a/2,x+(float)(a *Math.sqrt(3)/2),y+a/2,painter);
        canvas.drawLine(x+(float)(a *Math.sqrt(3)/2),y+a/2,x,y+a,painter);
        canvas.drawLine(x,y+a,x-(float)(a *Math.sqrt(3)/2),y+a/2,painter);
        canvas.drawLine(x-(float)(a *Math.sqrt(3)/2),y+a/2,x-(float)(a *Math.sqrt(3)/2),y-a/2,painter);
        canvas.drawLine(x-(float)(a *Math.sqrt(3)/2),y-a/2,x,y-a,painter);
        //średni hex
        canvas.drawLine(x,y-b,x+(float)(b *Math.sqrt(3)/2),y-b/2,painter);
        canvas.drawLine(x+(float)(b *Math.sqrt(3)/2),y-b/2,x+(float)(b *Math.sqrt(3)/2),y+b/2,painter);
        canvas.drawLine(x+(float)(b *Math.sqrt(3)/2),y+b/2,x,y+b,painter);
        canvas.drawLine(x,y+b,x-(float)(b *Math.sqrt(3)/2),y+b/2,painter);
        canvas.drawLine(x-(float)(b *Math.sqrt(3)/2),y+b/2,x-(float)(b *Math.sqrt(3)/2),y-b/2,painter);
        canvas.drawLine(x-(float)(b *Math.sqrt(3)/2),y-b/2,x,y-b,painter);
        //najmniejszy hex
        canvas.drawLine(x,y-c,x+(float)(c *Math.sqrt(3)/2),y-c/2,painter);
        canvas.drawLine(x+(float)(c *Math.sqrt(3)/2),y-c/2,x+(float)(c *Math.sqrt(3)/2),y+c/2,painter);
        canvas.drawLine(x+(float)(c *Math.sqrt(3)/2),y+c/2,x,y+c,painter);
        canvas.drawLine(x,y+c,x-(float)(c *Math.sqrt(3)/2),y+c/2,painter);
        canvas.drawLine(x-(float)(c *Math.sqrt(3)/2),y+c/2,x-(float)(c *Math.sqrt(3)/2),y-c/2,painter);
        canvas.drawLine(x-(float)(c *Math.sqrt(3)/2),y-c/2,x,y-c,painter);

        float[] sections = getLenghtStatsLine();//pobiera stosunki długości
        //ustawienie punktów odpowiednich statystyk
        float[] sPoint = {x, y-a*sections[0]};
        float[] ePoint = {(x+(float)(a*sections[1]*Math.sqrt(3)/2)),(y-a*sections[1]/2)};
        float[] dPoint = {(x+(float)(a*sections[2]*Math.sqrt(3)/2)),(y+a*sections[2]/2)};
        float[] iPoint = {x, y+a*sections[3]};
        float[] wPoint = {(x-(float)(a*sections[4]*Math.sqrt(3)/2)),(y+a*sections[4]/2)};
        float[] cPoint = {(x-(float)(a*sections[5]*Math.sqrt(3)/2)),(y-a*sections[5]/2)};

        painter.setStrokeWidth(3);
        //narysowanie punktów, kwestia estetyczna (ładniej wygląda)
        canvas.drawPoint(sPoint[0],sPoint[1],painter);
        canvas.drawPoint(ePoint[0],ePoint[1],painter);
        canvas.drawPoint(dPoint[0],dPoint[1],painter);
        canvas.drawPoint(iPoint[0],iPoint[1],painter);
        canvas.drawPoint(wPoint[0],wPoint[1],painter);
        canvas.drawPoint(cPoint[0],cPoint[1],painter);

        painter.setStrokeWidth(4);
        //narysowanie odcinków między punktami poszczególnych punktów statystycznych
        canvas.drawLine(sPoint[0],sPoint[1],ePoint[0],ePoint[1],painter);
        canvas.drawLine(ePoint[0],ePoint[1],dPoint[0],dPoint[1],painter);
        canvas.drawLine(dPoint[0],dPoint[1],iPoint[0],iPoint[1],painter);
        canvas.drawLine(iPoint[0],iPoint[1],wPoint[0],wPoint[1],painter);
        canvas.drawLine(wPoint[0],wPoint[1],cPoint[0],cPoint[1],painter);
        canvas.drawLine(cPoint[0],cPoint[1],sPoint[0],sPoint[1],painter);

        painter.setColor(getResources().getColor(R.color.color_strength));
        painter.setTextSize(12);
        canvas.drawText(getText(R.string.shortcut_strength).toString(),(float)x,(float)(y-a*1.05),painter);
        painter.setColor(getResources().getColor(R.color.color_endurance));
        canvas.drawText(getText(R.string.shortcut_endurance).toString(),x+(float)(a*1.05*Math.sqrt(3)/2 ),(float)(y-(a/2)*1.05),painter);
        painter.setColor(getResources().getColor(R.color.color_dexterity));
        canvas.drawText(getText(R.string.shortcut_dexterity).toString(),x+(float)(a*1.05*Math.sqrt(3)/2),(float)(y+(a/2)*1.05),painter);
        painter.setColor(getResources().getColor(R.color.color_intelligence));
        canvas.drawText(getText(R.string.shortcut_intelligence).toString(),(float)x,(float)(y+a*1.13),painter);
        painter.setColor(getResources().getColor(R.color.color_wisdom));
        canvas.drawText(getText(R.string.shortcut_wisdom).toString(),x-(float)(a*1.15*Math.sqrt(3)/2),(float)(y+(a/2)*1.15),painter);
        painter.setColor(getResources().getColor(R.color.color_charisma));
        canvas.drawText(getText(R.string.shortcut_charisma).toString(),x-(float)(a*1.1*Math.sqrt(3)/2),(float)(y-(a/2)*1.1),painter);

        //wgranie stworzonego obrazka(wykresu) do obiektu imageView
        imageView.setImageBitmap(bitmap);
    }

    private float[] getLenghtStatsLine(){
        float sum = (float)(strength+endurance+dexterity+intelligence+wisdom+charisma);
        float[] relations = {0,0,0,0,0,0};
        if (sum == 0){
            return relations;
        }else {
            relations[0] = (float) (3*strength/sum);
            relations[1] = (float) (3*endurance/sum);
            relations[2] = (float) (3*dexterity/sum);
            relations[3] = (float) (3*intelligence/sum);
            relations[4] = (float) (3*wisdom/sum);
            relations[5] = (float) (3*charisma/sum);
            for (int i = 0; i<relations.length; i++){
                if (relations[i]>sum){
                    relations[i] = sum/sum;
                }
            }
        }
        return relations;
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            super.onBackPressed();
    }

    private void setProgressBars(){
        progressBarStrenght.setProgress((int)strength%100);
        Log.d("+++++=", String.valueOf((int)strength%100));
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
        Log.d("++++++++++",String.valueOf(strength));
        Log.d("++++++++++",String.valueOf(endurance));
        Log.d("++++++++++",String.valueOf(dexterity));
        Log.d("++++++++++",String.valueOf(intelligence));
        Log.d("++++++++++",String.valueOf(wisdom));
        Log.d("++++++++++",String.valueOf(charisma));
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

        imageView = (ImageView) findViewById(R.id.imageView_StatHex);
    }

}
