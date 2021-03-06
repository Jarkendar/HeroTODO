package com.example.jarek.questtemporary.activityClasses;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jarek.questtemporary.R;
import com.example.jarek.questtemporary.dataClasses.ColorManager;

public class StatisticActivity extends AppCompatActivity {

    private TextView textViewStrength, textViewEndurance, textViewDexterity, textViewIntelligence, textViewWisdom, textViewCharisma, textViewSuccess, textViewSeries, textViewFailed, textViewMaxSeries;
    private ProgressBar progressBarStrenght, progressBarEndurance, progressBarDexterity, progressBarIntelligence, progressBarWisdom, progressBarCharisma;
    private ImageView imageView;

    private double strength, endurance, dexterity, intelligence, wisdom, charisma;

    private final String sharedAchievement = "achievements";
    private final String successEndQuestsKey = "successEndQuests";
    private final String failedEndQuestsKey = "failedEndQuests";
    private final String seriesQuestsKey = "seriesQuest";
    private final String maxseriesQuestsKey = "maxSeriesQuest";

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
        setComponentsColor();
        setTextStatistic();

    }

    private void setTextStatistic(){
        SharedPreferences sharedPreferences = getSharedPreferences(sharedAchievement, MODE_PRIVATE);
        int success = sharedPreferences.getInt(successEndQuestsKey,0);
        int series = sharedPreferences.getInt(seriesQuestsKey,0);
        int maxSeries = sharedPreferences.getInt(maxseriesQuestsKey,0);
        int failed = sharedPreferences.getInt(failedEndQuestsKey,0);
        textViewSuccess.setText(getString(R.string.text_successQuest)+" "+success);
        textViewSeries.setText(getString(R.string.text_actual_seriesQuest)+" "+series);
        textViewMaxSeries.setText(getString(R.string.text_maxSeries)+" "+maxSeries);
        textViewFailed.setText(getString(R.string.text_failedQuest)+" "+failed);
    }

    /**
     * Metoda rysująca wykres sześciokątny na bitmapie. Najpierw wyliczane są tablice wierzchołków
     * za pomocą odpowiednich metod, następnie rysowane są linie. Wykres składa się z tła złożonego
     * z 3 sześciokątów o  3 różnych rozmiarach, przekątnych największego z sześciokątów. Drugim
     * elementem wykresu jest wielokąt reprezentujący odpowiednie statystyki. Ostatnim elementem
     * wykresu są oznaczenia konkretnych słupków, złożone z pierwszej litery stastystyki.
     */
    private void drawHexStats() {

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int widthDiagramArea = displayMetrics.widthPixels;
        int heightDiagramArea = (int) (widthDiagramArea * 0.625);
        int centerX = widthDiagramArea / 2;//X środka
        int centerY = heightDiagramArea / 2;//Y środka
        int radiusOutsideHex = centerX / 2;//promień największego hexa
        int radiudBeetwenHex = centerX / 3;//promień średniego hexa
        int radiusInsideHex = centerX / 6;//promień najmniejszego hexa

        Bitmap bitmap = Bitmap.createBitmap(widthDiagramArea, heightDiagramArea, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint painter = new Paint();

        painter.setColor(new ColorManager(getApplicationContext()).getPainterColor());

        painter.setAntiAlias(true);//włączenie wygładzania krawędzi
        //przekątne
        float[] vertexToLine = createHexVertexTable(centerX, centerY, radiusOutsideHex);
        canvas.drawLine(vertexToLine[0], vertexToLine[1], vertexToLine[12], vertexToLine[13], painter);
        canvas.drawLine(vertexToLine[2], vertexToLine[3], vertexToLine[16], vertexToLine[17], painter);
        canvas.drawLine(vertexToLine[6], vertexToLine[7], vertexToLine[20], vertexToLine[21], painter);
        //rysowanie hexów tła
        canvas.drawLines(createHexVertexTable(centerX, centerY, radiusOutsideHex), painter);
        canvas.drawLines(createHexVertexTable(centerX, centerY, radiudBeetwenHex), painter);
        canvas.drawLines(createHexVertexTable(centerX, centerY, radiusInsideHex), painter);
        //rysowanie linii wykresu statystyk, kropki służą polepszeniu efektu wizualnego
        float[] sections = getLenghtStatsLine();//pobiera stosunki długości
        painter.setStrokeWidth(3);
        canvas.drawPoints(createPointsTable(centerX, centerY, radiusOutsideHex, sections), painter);
        painter.setStrokeWidth(4);
        canvas.drawLines(createStatsVertexTable(centerX, centerY, radiusOutsideHex, sections), painter);
        //rysowanie liter skrótów statystyk
        painter.setTextSize(widthDiagramArea/40);
        painter.setColor(getResources().getColor(R.color.color_strength));
        canvas.drawText(getText(R.string.shortcut_strength).toString(), (float) centerX, (float) (centerY - radiusOutsideHex * 1.05), painter);
        painter.setColor(getResources().getColor(R.color.color_endurance));
        canvas.drawText(getText(R.string.shortcut_endurance).toString(), centerX + (float) (radiusOutsideHex * 1.05 * Math.sqrt(3) / 2), (float) (centerY - (radiusOutsideHex / 2) * 1.05), painter);
        painter.setColor(getResources().getColor(R.color.color_dexterity));
        canvas.drawText(getText(R.string.shortcut_dexterity).toString(), centerX + (float) (radiusOutsideHex * 1.05 * Math.sqrt(3) / 2), (float) (centerY + (radiusOutsideHex / 2) * 1.05), painter);
        painter.setColor(getResources().getColor(R.color.color_intelligence));
        canvas.drawText(getText(R.string.shortcut_intelligence).toString(), (float) centerX, (float) (centerY + radiusOutsideHex * 1.13), painter);
        painter.setColor(getResources().getColor(R.color.color_wisdom));
        canvas.drawText(getText(R.string.shortcut_wisdom).toString(), centerX - (float) (radiusOutsideHex * 1.15 * Math.sqrt(3) / 2), (float) (centerY + (radiusOutsideHex / 2) * 1.15), painter);
        painter.setColor(getResources().getColor(R.color.color_charisma));
        canvas.drawText(getText(R.string.shortcut_charisma).toString(), centerX - (float) (radiusOutsideHex * 1.1 * Math.sqrt(3) / 2), (float) (centerY - (radiusOutsideHex / 2) * 1.1), painter);
        //wgranie stworzonego obrazka(wykresu) do obiektu imageView
        imageView.setImageBitmap(bitmap);
    }

    /**
     * Metoda obliczająca tablicę wierzchołków dla obiektu canvas metody drawLines.
     *
     * @param centerX szerokość środka wykresu
     * @param centerY wysokość środka wykresu
     * @param lenght  długość największego sześciąkąta tła
     * @return tablica wierzchołków, rozmiar 24 (2 na każdy wierzchołek) wartości są zdublowane dla
     * metody canvas.drawLines
     */
    private float[] createHexVertexTable(float centerX, float centerY, float lenght) {
        return new float[]{centerX, centerY - lenght,
                centerX + (float) (lenght * Math.sqrt(3) / 2), centerY - lenght / 2,
                centerX + (float) (lenght * Math.sqrt(3) / 2), centerY - lenght / 2,
                centerX + (float) (lenght * Math.sqrt(3) / 2), centerY + lenght / 2,
                centerX + (float) (lenght * Math.sqrt(3) / 2), centerY + lenght / 2,
                centerX, centerY + lenght,
                centerX, centerY + lenght,
                centerX - (float) (lenght * Math.sqrt(3) / 2), centerY + lenght / 2,
                centerX - (float) (lenght * Math.sqrt(3) / 2), centerY + lenght / 2,
                centerX - (float) (lenght * Math.sqrt(3) / 2), centerY - lenght / 2,
                centerX - (float) (lenght * Math.sqrt(3) / 2), centerY - lenght / 2,
                centerX, centerY - lenght};
    }

    /**
     * Metoda obliczająca położenie wierzchołków wielokąta przedstawiającego wykres.
     *
     * @param centerX  szerokość środka wykresu
     * @param centerY  wysokość środka wykresu
     * @param lenght   długość największego sześciąkąta tła
     * @param sections stosunki poszczególnych statystyk
     * @return tablica wierzchołków, rozmiar 24 (2 na każdy wierzchołek) wartości są zdublowane dla
     * metody canvas.drawLines
     */
    private float[] createStatsVertexTable(float centerX, float centerY, float lenght, float[] sections) {
        return new float[]{centerX, centerY - lenght * sections[0],
                (centerX + (float) (lenght * sections[1] * Math.sqrt(3) / 2)), (centerY - lenght * sections[1] / 2),
                (centerX + (float) (lenght * sections[1] * Math.sqrt(3) / 2)), (centerY - lenght * sections[1] / 2),
                (centerX + (float) (lenght * sections[2] * Math.sqrt(3) / 2)), (centerY + lenght * sections[2] / 2),
                (centerX + (float) (lenght * sections[2] * Math.sqrt(3) / 2)), (centerY + lenght * sections[2] / 2),
                centerX, centerY + lenght * sections[3],
                centerX, centerY + lenght * sections[3],
                (centerX - (float) (lenght * sections[4] * Math.sqrt(3) / 2)), (centerY + lenght * sections[4] / 2),
                (centerX - (float) (lenght * sections[4] * Math.sqrt(3) / 2)), (centerY + lenght * sections[4] / 2),
                (centerX - (float) (lenght * sections[5] * Math.sqrt(3) / 2)), (centerY - lenght * sections[5] / 2),
                (centerX - (float) (lenght * sections[5] * Math.sqrt(3) / 2)), (centerY - lenght * sections[5] / 2),
                centerX, centerY - lenght * sections[0],};
    }

    /**
     * Metoda rysuje punkty w miejscu zakończenia się słupków statystyk. Służą one ładniejszemu
     * przedstawieniu odcinków.
     *
     * @param centerX  szerokość środka wykresu
     * @param centerY  wysokość środka wykresu
     * @param lenght   długość największego sześciąkąta tła
     * @param sections stosunki poszczególnych statystyk
     * @return tablica wierzchołków, rozmiar 12 (2 na każdy wierzchołek)
     */
    private float[] createPointsTable(float centerX, float centerY, float lenght, float[] sections) {
        return new float[]{centerX, centerY - lenght * sections[0],
                (centerX + (float) (lenght * sections[1] * Math.sqrt(3) / 2)), (centerY - lenght * sections[1] / 2),
                (centerX + (float) (lenght * sections[2] * Math.sqrt(3) / 2)), (centerY + lenght * sections[2] / 2),
                centerX, centerY + lenght * sections[3],
                (centerX - (float) (lenght * sections[4] * Math.sqrt(3) / 2)), (centerY + lenght * sections[4] / 2),
                (centerX - (float) (lenght * sections[5] * Math.sqrt(3) / 2)), (centerY - lenght * sections[5] / 2)};
    }


    /**
     * Metoda obliczająca stosunki między doświadczeniem różnych stastyk. Najpierw zliczana jest
     * suma doświadczenia. Następnie wyliczane są poszczególne stastyki. Mnożone są one przez 3,
     * aby zwiększyć widoczność statystyk na wykresie. W razie przekroczenia wartości 1 wymnożonych
     * statystyk, wartość jest ustawiana na 1.
     *
     * @return tablice stosunków doświadczenia w kolejności siła, wytrzymałość, zręczność,
     * iteligencja, mądrość, charyzma, wartości z przedziału <0;1>
     */
    private float[] getLenghtStatsLine() {
        float sum = (float) (strength + endurance + dexterity + intelligence + wisdom + charisma);
        float[] relations = {0, 0, 0, 0, 0, 0};
        if (sum == 0) {
            return relations;
        } else {
            relations[0] = (float) (3 * strength / sum);
            relations[1] = (float) (3 * endurance / sum);
            relations[2] = (float) (3 * dexterity / sum);
            relations[3] = (float) (3 * intelligence / sum);
            relations[4] = (float) (3 * wisdom / sum);
            relations[5] = (float) (3 * charisma / sum);
            for (int i = 0; i < relations.length; i++) {
                if (relations[i] > sum / sum) {
                    relations[i] = sum / sum;
                }
            }
        }
        return relations;
    }

    /**
     * Metoda cyklu życiowego activity. Odpala metody wczytujące dane do kompontentów interfejsu.
     */
    @Override
    protected void onResume() {
        super.onResume();
        setLevelTextViews();
        setProgressBars();
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
            case android.R.id.home: {
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

    /**
     * Metoda ustawiająca progress bar'y. Progres ustawiany na resztę z dzielenia przez 100
     * całkowitego doświadczenia, odpowiada to wypełnieniu paska doświadczenia na aktualny poziom.
     */
    private void setProgressBars() {
        progressBarStrenght.setProgress((int) strength % 100);
        progressBarEndurance.setProgress((int) endurance % 100);
        progressBarDexterity.setProgress((int) dexterity % 100);
        progressBarIntelligence.setProgress((int) intelligence % 100);
        progressBarWisdom.setProgress((int) wisdom % 100);
        progressBarCharisma.setProgress((int) charisma % 100);
    }

    /**
     * Metoda ustawiająca tekst reprezentujący poziom poszczególnych statystyk. Poziom wyliczany poprzez
     * podzielenie całkowitego doświadczenia przez 100.
     */
    private void setLevelTextViews() {
        textViewStrength.setText(((int) strength / 100) + " " + getText(R.string.text_level));
        textViewEndurance.setText(((int) endurance / 100) + " " + getText(R.string.text_level));
        textViewDexterity.setText(((int) dexterity / 100) + " " + getText(R.string.text_level));
        textViewIntelligence.setText(((int) intelligence / 100) + " " + getText(R.string.text_level));
        textViewWisdom.setText(((int) wisdom / 100) + " " + getText(R.string.text_level));
        textViewCharisma.setText(((int) charisma / 100) + " " + getText(R.string.text_level));
    }

    /**
     * Metoda pobierająca wszystkie przekazane wartości z activity wywołującej.
     */
    private void getBundleExtras() {
        Bundle bundle = getIntent().getExtras();
        strength = (double) bundle.getFloat("strength");
        endurance = (double) bundle.getFloat("endurance");
        dexterity = (double) bundle.getFloat("dexterity");
        intelligence = (double) bundle.getFloat("intelligence");
        wisdom = (double) bundle.getFloat("wisdom");
        charisma = (double) bundle.getFloat("charisma");
    }

    /**
     * Metoda wiążąca elementy interfejsu użytkownika ze zmiennymi wykorzystywanymi w kodzie.
     */
    private void joinComponentsWithVariable() {
        textViewStrength = (TextView) findViewById(R.id.textView_StatisticStrengthLevel);
        textViewEndurance = (TextView) findViewById(R.id.textView_StatisticEnduranceLevel);
        textViewDexterity = (TextView) findViewById(R.id.textView_StatisticDexterityLevel);
        textViewIntelligence = (TextView) findViewById(R.id.textView_StatisticIntelligenceLevel);
        textViewWisdom = (TextView) findViewById(R.id.textView_StatisticWisdomLevel);
        textViewCharisma = (TextView) findViewById(R.id.textView_StatisticCharismaLevel);
        textViewSuccess = (TextView) findViewById(R.id.textView_success);
        textViewSeries = (TextView) findViewById(R.id.textView_series);
        textViewFailed = (TextView) findViewById(R.id.textView_failed);
        textViewMaxSeries = (TextView) findViewById(R.id.textView_maxSeries);

        progressBarStrenght = (ProgressBar) findViewById(R.id.progressBar_StatisticStrength);
        progressBarEndurance = (ProgressBar) findViewById(R.id.progressBar_StatisticEndurance);
        progressBarDexterity = (ProgressBar) findViewById(R.id.progressBar_StatisticDexterity);
        progressBarIntelligence = (ProgressBar) findViewById(R.id.progressBar_StatisticIntelligence);
        progressBarWisdom = (ProgressBar) findViewById(R.id.progressBar_StatisticWisdom);
        progressBarCharisma = (ProgressBar) findViewById(R.id.progressBar_StatisticCharisma);

        imageView = (ImageView) findViewById(R.id.imageView_StatHex);
    }

    private void setComponentsColor() {
        ColorManager colorManager = new ColorManager(getApplicationContext());
        findViewById(R.id.ScrollViewStatistic).setBackgroundColor(colorManager.getBackgroundColor());
        textViewStrength.setTextColor(colorManager.getTextColor());
        textViewEndurance.setTextColor(colorManager.getTextColor());
        textViewDexterity.setTextColor(colorManager.getTextColor());
        textViewIntelligence.setTextColor(colorManager.getTextColor());
        textViewWisdom.setTextColor(colorManager.getTextColor());
        textViewCharisma.setTextColor(colorManager.getTextColor());
        textViewSuccess.setTextColor(colorManager.getTextColor());
        textViewSeries.setTextColor(colorManager.getTextColor());
        textViewFailed.setTextColor(colorManager.getTextColor());
        textViewMaxSeries.setTextColor(colorManager.getTextColor());
    }

}
