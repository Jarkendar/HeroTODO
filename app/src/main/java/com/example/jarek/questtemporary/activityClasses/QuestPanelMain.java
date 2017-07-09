package com.example.jarek.questtemporary.activityClasses;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jarek.questtemporary.R;
import com.example.jarek.questtemporary.dataClasses.Achievement;
import com.example.jarek.questtemporary.dataClasses.ColorManager;
import com.example.jarek.questtemporary.dataClasses.FileManager;
import com.example.jarek.questtemporary.dataClasses.Quest;
import com.example.jarek.questtemporary.dataClasses.QuestRowAdapter;
import com.example.jarek.questtemporary.heroClasses.Hero;
import com.example.jarek.questtemporary.heroClasses.StatsMultiplier;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

public class QuestPanelMain extends AppCompatActivity implements Observer {

    private ProgressBar pBExperience;
    private LinkedList<Quest> quests;
    private ListView listView;
    private TextView tClassName, tClassLevel, tExperience;
    private QuestRowAdapter questRowAdapter;
    private int iposition;
    private Button buttonModify, buttonDelete;
    private int rankClassID;

    private final String userQuestFile = "userQuestFile";
    private final String userAddQuest = "userAddQuest";
    private final String heroShared = "heroShared";
    private final String sharedAchievement = "achievements";
    private final String successEndQuestsKey = "successEndQuests";
    private final String failedEndQuestsKey = "failedEndQuests";
    private final String seriesQuestsKey = "seriesQuest";

    private Hero userHero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_panel_main);
        changeActionBar();
        joinComponentsWithVariable();

        quests = new LinkedList<>();
    }

    private void changeActionBar(){
        Calendar calendar = Calendar.getInstance();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        String title = getApplicationContext().getText(R.string.text_welcome).toString();
        Format formatter = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

        title = title + " " + formatter.format(calendar.getTime());

        actionBar.setTitle(title);
    }

    /**
     * Metoda odświeżania zawartości listView. Sortuje zadania wg daty zakończenia zadania rosnąco
     * (zadania wcześniejsze są wyżej w listView). Ustawia adapterowi listę zadań. Na koniec
     * podłącza adapter pod listView.
     */
    private void adapterRefresh() {
        Collections.sort(quests, new Comparator<Quest>() {
            @Override
            public int compare(Quest quest1, Quest quest2) {
                return quest1.getTimeToLiveDate().compareTo(quest2.getTimeToLiveDate());
            }
        });
        questRowAdapter.setData(quests);
        listView.setAdapter(questRowAdapter);
        new CheckerAchievement().execute();
    }

    /**
     * Nadpisana metoda onResume. Czyści listę zadań. Tworzy obiekt zarządzający plikami i karze
     * mu wczytać wszystkie zadania z pliku. Dodatkowo sprawdza czy flaga nowego zadania jest
     * podniesiona, jeśli tak to wczytuje dodane zadania i opuszcza flagę oraz łączy wczytaną
     * listę z resztą zadań, jeśli nie to żadne nowe zadanie nie zostało dodane. Następnie
     * wywołuje metodę wczytującą Hero z SharedPreference. Na koniec odświeża listView.
     */
    @Override
    protected void onResume() {
        super.onResume();

        ColorManager colorManager = new ColorManager(getApplicationContext());

        int textColor = colorManager.getTextColor();
        int todayQuestColor = colorManager.getTodayQuestColor();
        int endTimeQuestColor = colorManager.getEndTimeQuestColor();
        int evenQuestColor = colorManager.getEvenQuestColor();
        int notEvenQuestColor = colorManager.getNotEvenQuestColor();

        questRowAdapter = new QuestRowAdapter(this, R.layout.row_quest_layout, quests, textColor, todayQuestColor, endTimeQuestColor, evenQuestColor, notEvenQuestColor);
        questRowAdapter.addObserver(this);
        buttonModify.setEnabled(false);
        buttonDelete.setEnabled(false);

        setComponentsColor(colorManager);
        quests.clear();
        FileManager fileManager = new FileManager();
        quests.addAll(fileManager.deserializationQuests(userQuestFile, getApplicationContext()));
        String userInfoShared = "userInfoShared";
        SharedPreferences sharedPreferences = getSharedPreferences(userInfoShared, MODE_PRIVATE);
        if (sharedPreferences.getBoolean("addQuests", false)) {
            LinkedList<Quest> addQuests;
            addQuests = fileManager.deserializationQuests(userAddQuest, getApplicationContext());
            quests.addAll(addQuests);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("addQuests", false);
            editor.apply();
        } else if (sharedPreferences.getBoolean("modify", false)) {
            LinkedList<Quest> addQuests;
            addQuests = fileManager.deserializationQuests(userAddQuest, getApplicationContext());
            quests.addAll(addQuests);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("modify", false);
            deleteListElement();
            editor.apply();
        }

        readHeroFromShared();
        adapterRefresh();
    }

    private void setComponentsColor(ColorManager colorManager) {
        tClassLevel.setTextColor(colorManager.getTextColor());
        tClassName.setTextColor(colorManager.getTextColor());
        tExperience.setTextColor(colorManager.getTextColor());
        findViewById(R.id.RelativeLayoutMain).setBackgroundColor(colorManager.getBackgroundColor());
    }

    /**
     * Metoda wczytuje postać hero z sharedPreferences.
     */
    private void readHeroFromShared() {
        SharedPreferences sharedPreferences1 = getSharedPreferences(heroShared, MODE_PRIVATE);
        int heroClassID = sharedPreferences1.getInt("heroClass", R.string.class_native);
        tClassName.setText(getString(heroClassID));
        tClassLevel.setText(getString(R.string.native_zeroLevel));
        pBExperience.setProgress(0);
        tExperience.setText(getString(R.string.text_experience));
        if (heroClassID != R.string.class_native) {
            double strength = (double) sharedPreferences1.getFloat("strength", 0);
            double endurance = (double) sharedPreferences1.getFloat("endurance", 0);
            double dexterity = (double) sharedPreferences1.getFloat("dexterity", 0);
            double intelligence = (double) sharedPreferences1.getFloat("intelligence", 0);
            double wisdom = (double) sharedPreferences1.getFloat("wisdom", 0);
            double charisma = (double) sharedPreferences1.getFloat("charisma", 0);
            StatsMultiplier statsMultiplier = new StatsMultiplier();
            double[] multiplier = new double[0];
            String[] ranksArray = new String[0];
            switch (heroClassID) {
                case R.string.class_bard: {
                    ranksArray = getResources().getStringArray(R.array.bard_ranks);
                    multiplier = statsMultiplier.getBardMultiplier();
                    rankClassID = R.array.bard_ranks;
                    break;
                }
                case R.string.class_hunter: {
                    ranksArray = getResources().getStringArray(R.array.hunter_ranks);
                    multiplier = statsMultiplier.getHunterMultiplier();
                    rankClassID = R.array.hunter_ranks;
                    break;
                }
                case R.string.class_merchant: {
                    ranksArray = getResources().getStringArray(R.array.merchant_ranks);
                    multiplier = statsMultiplier.getMerchantMultiplier();
                    rankClassID = R.array.merchant_ranks;
                    break;
                }
                case R.string.class_mage: {
                    ranksArray = getResources().getStringArray(R.array.mage_ranks);
                    multiplier = statsMultiplier.getMageMultiplier();
                    rankClassID = R.array.mage_ranks;
                    break;
                }
                case R.string.class_lord: {
                    ranksArray = getResources().getStringArray(R.array.lord_ranks);
                    multiplier = statsMultiplier.getLordMultiplier();
                    rankClassID = R.array.lord_ranks;
                    break;
                }
                case R.string.class_warrior: {
                    ranksArray = getResources().getStringArray(R.array.warrior_ranks);
                    multiplier = statsMultiplier.getWarriorMultiplier();
                    rankClassID = R.array.warrior_ranks;
                    break;
                }
            }
            userHero =
                    new Hero(strength, endurance, dexterity, intelligence, wisdom, charisma,
                            ranksArray, multiplier);
            refreshHeroInfo();
        }
    }

    /**
     * Metoda odświeżająca komponenty Activity zwiazane z Hero.
     */
    private void refreshHeroInfo() {
        if (userHero != null) {
            tClassName.setText(userHero.getClassRank());
            tClassLevel.setText(String.valueOf(userHero.getHeroLVL()).concat(" " + getString(R.string.text_level)));
            pBExperience.setProgress((int) userHero.getHeroEXP() % 100);
            tExperience.setText(String.valueOf((int) userHero.getHeroEXP() % 100).concat(getString(R.string.text_experienceEmpty)));
        }
    }

    /**
     * Nadpisana metoda onPause. Tworzy obiekt zarządzający plikami, przekazuje listę zadań do
     * zapisu oraz wywołuje metodę zapisu statystyk Hero.
     */
    @Override
    protected void onPause() {
        super.onPause();
        FileManager fileManager = new FileManager();
        fileManager.serializationQuests(quests, userQuestFile, getApplicationContext());
        saveHeroToShared();
    }

    /**
     * Metoda zapisująca (jeśli Hero istnieje) statystyki do SharedPreferences.
     */
    private void saveHeroToShared() {
        if (userHero != null) {
            SharedPreferences.Editor editor = getSharedPreferences(heroShared, MODE_PRIVATE).edit();
            editor.putFloat("strength", (float) userHero.getStrengthExperience());
            editor.putFloat("endurance", (float) userHero.getEnduranceExperience());
            editor.putFloat("dexterity", (float) userHero.getDexterityExperience());
            editor.putFloat("intelligence", (float) userHero.getIntelligenceExperience());
            editor.putFloat("wisdom", (float) userHero.getWisdomExperience());
            editor.putFloat("charisma", (float) userHero.getCharismaExperience());
            editor.apply();
        }
    }

    /**
     * Metoda wiążąca elementy interfejsu użytkownika ze zmiennymi wykorzystywanymi w kodzie.
     */
    private void joinComponentsWithVariable() {
        tClassName = (TextView) findViewById(R.id.textView_ClassName);
        tClassLevel = (TextView) findViewById(R.id.textView_ClassLevel);
        tExperience = (TextView) findViewById(R.id.textView_Experience);
        pBExperience = (ProgressBar) findViewById(R.id.progressBar_Experience);
        listView = (ListView) findViewById(R.id.listView_QuestList);
        buttonModify = (Button) findViewById(R.id.button_Modify);
        buttonDelete = (Button) findViewById(R.id.button_Delete);
    }

    /**
     * Metoda podłączająca layout wyglądu ActionBar'a.
     *
     * @param menu obiekt reprezentujący menu
     * @return true podłączenie się powiodło
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Metoda obsługująca przyciski z rozwijanego paska opcji w ActionBar.
     *
     * @param item wybrana opcja z paska
     * @return odpowiedź z nadklasy
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_add: {
                Intent intent = new Intent(this, QuestForm.class);
                intent.putExtra("fileAddress", userAddQuest);
                intent.putExtra("whatdo", "addQuest");
                startActivity(intent);
                break;
            }
            case R.id.app_bar_statistics: {
                if (userHero != null) {
                    Intent intent = new Intent(this, StatisticActivity.class);
                    intent.putExtra("strength", (float) userHero.getStrengthExperience());
                    intent.putExtra("endurance", (float) userHero.getEnduranceExperience());
                    intent.putExtra("dexterity", (float) userHero.getDexterityExperience());
                    intent.putExtra("intelligence", (float) userHero.getIntelligenceExperience());
                    intent.putExtra("wisdom", (float) userHero.getWisdomExperience());
                    intent.putExtra("charisma", (float) userHero.getCharismaExperience());
                    startActivity(intent);
                } else {
                    Toast.makeText(this, getText(R.string.text_firstChooseClass), Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.app_bar_achievement:{
                Intent intent = new Intent(this, AchievementActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.app_bar_option: {
                Intent intent = new Intent(this, OptionActivity.class);
                startActivity(intent);
                userHero = null;
                break;
            }
            case R.id.app_bar_help: {
                Intent intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Metoda obsługująca rozkazy. W pierwszej kolejności rozdziela rozkaz na część wg ";". Następnie
     * wybiera odpowiedni if który obsługuje dany rozkaz.
     * "succeed" wypełnienie zadania, dodaje do statystyk Hero ilość punktów doświadczenia z zadania
     * "failed" nie podołanie zadaniu, usuwa dane zadanie z listy zadań
     * Powyższe rozkazy dodatkowo sprawdzajączy zadanie jest powtarzalne, jeśli tak to tworzą nowe
     * identyczne z poprzednim ze zmienioną datą zakończenia. Zmiana jest określona przez długość
     * interwału zadania.
     * "clickRow" wiersz został kliknięty, otwiera możliwość modyfikacji lub usunięcia zadania,
     * poprzez włączenie dostępności przycisków button_Modify i button_Delete.
     *
     * @param order rozkaz składający się z "polecenie;numer wiersza"
     */
    private void serviceObserveButtons(String order) {
        String[] partsOfOrder = order.split(";");
        int position = Integer.parseInt(partsOfOrder[1]);
        switch (partsOfOrder[0]) {
            case "succeed":
                buttonModify.setEnabled(false);
                buttonDelete.setEnabled(false);
                String[] attributes = quests.get(position).getAtributes();
                if (userHero != null) {
                    for (String attribute : attributes) {
                        if (attribute.equals(getText(R.string.attribute_strength)))
                            userHero.addStrengthExperience(quests.get(position).getExperiencePoints() / attributes.length);
                        if (attribute.equals(getText(R.string.attribute_endurance)))
                            userHero.addEnduranceExperience(quests.get(position).getExperiencePoints() / attributes.length);
                        if (attribute.equals(getText(R.string.attribute_dexterity)))
                            userHero.addDexterityExperience(quests.get(position).getExperiencePoints() / attributes.length);
                        if (attribute.equals(getText(R.string.attribute_intelligence)))
                            userHero.addIntelligenceExperience(quests.get(position).getExperiencePoints() / attributes.length);
                        if (attribute.equals(getText(R.string.attribute_wisdom)))
                            userHero.addWisdomExperience(quests.get(position).getExperiencePoints() / attributes.length);
                        if (attribute.equals(getText(R.string.attribute_charisma)))
                            userHero.addCharismaExperience(quests.get(position).getExperiencePoints() / attributes.length);
                    }
                }
                if (quests.get(position).isRepeatable()) {
                    Calendar calendar = quests.get(position).getTimeToLiveDate();
                    calendar.add(Calendar.DAY_OF_MONTH, quests.get(position).getRepeatInterval());
                    calendar.set(Calendar.HOUR, 0);
                    quests.addLast(new Quest(quests.get(position).getDescription(),
                            quests.get(position).getExperiencePoints(),
                            calendar,
                            quests.get(position).getAtributes(),
                            quests.get(position).isRepeatable(),
                            quests.get(position).getRepeatInterval(),
                            this));
                }

                quests.remove(position);
                break;
            case "failed":
                buttonModify.setEnabled(false);
                buttonDelete.setEnabled(false);
                if (quests.get(position).isRepeatable()) {
                    Calendar calendar = quests.get(position).getTimeToLiveDate();
                    calendar.add(Calendar.DAY_OF_MONTH, quests.get(position).getRepeatInterval());
                    calendar.set(Calendar.HOUR, 0);
                    quests.addLast(new Quest(quests.get(position).getDescription(),
                            quests.get(position).getExperiencePoints(),
                            calendar,
                            quests.get(position).getAtributes(),
                            quests.get(position).isRepeatable(),
                            quests.get(position).getRepeatInterval(),
                            this));
                }
                quests.remove(position);
                break;
            case "clickRow":
                buttonModify.setEnabled(true);
                buttonDelete.setEnabled(true);
                iposition = Integer.parseInt(partsOfOrder[1]);
                break;
        }
    }

    /**
     * Metoda zaimplementowana z interfejsu Observer, przy zgłoszeniu zmiany obsługuje zgłoszenie.
     * Przekazuje rozkaz do metody serviceObserveButtons. Odświerza listView oraz statystyki Hero.
     *
     * @param observable obiekt obserwowany
     * @param o          obiekt rozkazu w tym przypadku string wg wzoru "*;*"
     */
    @Override
    public void update(Observable observable, Object o) {
        serviceObserveButtons(o.toString());
        adapterRefresh();
        refreshHeroInfo();
    }

    /**
     * Metoda obsługi kliknięcia w przycisk modyfikacji. Zapisuje wszystkie marametry wybranego
     * zadania do wywoływanego activity.
     *
     * @param view komponent wywołujący metodę
     */
    public void clickModifyQuest(View view) {
        String attributes = "";
        for (String x : quests.get(iposition).getAtributes()) {
            attributes = attributes.concat(x + ";");
        }
        attributes = attributes.substring(0, attributes.length() - 1);
        Intent intent = new Intent(this, QuestForm.class);
        intent.putExtra("fileAddress", userAddQuest);
        intent.putExtra("whatdo", "modifyQuest");
        intent.putExtra("description", quests.get(iposition).getDescription());
        intent.putExtra("experience", quests.get(iposition).getExperiencePoints());
        intent.putExtra("timeToLive", quests.get(iposition).getDateFormatString());
        intent.putExtra("attributes", attributes);
        Calendar calendar = quests.get(iposition).getTimeToLiveDate();
        intent.putExtra("yearDate", calendar.get(Calendar.YEAR));
        intent.putExtra("monthDate", calendar.get(Calendar.MONTH));
        intent.putExtra("dayDate", calendar.get(Calendar.DAY_OF_MONTH));
        intent.putExtra("repeatable", quests.get(iposition).isRepeatable());
        intent.putExtra("interval", quests.get(iposition).getRepeatInterval());
        startActivity(intent);
    }

    /**
     * Metoda usuwa zadanie z listy zadań i odświeża listView.
     */
    private void deleteListElement() {
        quests.remove(iposition);
        adapterRefresh();
        Toast.makeText(this, getString(R.string.text_confirmDelete), Toast.LENGTH_SHORT).show();
    }

    /**
     * Metoda kliknięcia na przycisk usunięcia. Wyświetla AlertDialog potwierdzający wybór użytkownika
     * zapobiegato przypadkowemu naciśnięciu przycisku usunięcia.
     *
     * @param view obiekt komponentu wywołującego
     */
    public void clickDeleteQuest(View view) {
        new AlertDialog.Builder(this)
                .setTitle(getText(R.string.text_confirm))//tytuł
                .setMessage(getString(R.string.text_areYouSureDelete) + " " + quests.get(iposition).getDescription())
                //opis
                .setPositiveButton(getText(R.string.text_yes), new DialogInterface.OnClickListener() {
                    //kliknięcie tak usuwa element
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteListElement();
                    }
                })
                .setNegativeButton(getText(R.string.text_no), null)//kliknięcie NIE nic nie robi
                .show();//pokaż
    }


    private class CheckerAchievement extends AsyncTask < Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            String[] achievNames = getResources().getStringArray(R.array.achievement_Names);
            for (String x : achievNames){
                Log.d("++++++++++", "doInBackground: " + x);
            }
            SharedPreferences shared = getSharedPreferences(sharedAchievement,MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();
            boolean[] achievIsGained = new boolean[achievNames.length];
            for(int i = 0; i< achievNames.length ; i++){
                achievIsGained[i] = shared.getBoolean(achievNames[i],false);
            }
            int successQuest = shared.getInt(successEndQuestsKey, 0);
            int failedQuest = shared.getInt(failedEndQuestsKey, 0);
            int seriesQuest = shared.getInt(seriesQuestsKey, 0);

            if (!achievIsGained[0] && successQuest>=1){
                editor.putBoolean(achievNames[0],true);
                editor.apply();
            } else if (!achievIsGained[1] && successQuest>=128){
                editor.putBoolean(achievNames[1],true);
                editor.apply();
            } else if (!achievIsGained[2] && successQuest>=1024){
                editor.putBoolean(achievNames[2],true);
                editor.apply();
            } else if (!achievIsGained[3] && successQuest>=8192){
                editor.putBoolean(achievNames[3],true);
                editor.apply();
            }
            if (!achievIsGained[4] && seriesQuest>=2){
                editor.putBoolean(achievNames[4],true);
                editor.apply();
            }else if (!achievIsGained[5] && seriesQuest>=16){
                editor.putBoolean(achievNames[5],true);
                editor.apply();
            }else if (!achievIsGained[6] && seriesQuest>=128){
                editor.putBoolean(achievNames[6],true);
                editor.apply();
            }else if (!achievIsGained[7] && seriesQuest>=512){
                editor.putBoolean(achievNames[7],true);
                editor.apply();
            }
            String[] ranks = getResources().getStringArray(rankClassID);
            if (!achievIsGained[8] && userHero.getClassRank().equals(ranks[ranks.length-1])){
                editor.putBoolean(achievNames[8],true);
                editor.apply();
            }else if (!achievIsGained[9] && userHero.getClassRank().equals(ranks[ranks.length-1])){
                editor.putBoolean(achievNames[9],true);
                editor.apply();
            }else if (!achievIsGained[10] && userHero.getClassRank().equals(ranks[ranks.length-1])){
                editor.putBoolean(achievNames[10],true);
                editor.apply();
            }else if (!achievIsGained[11] && userHero.getClassRank().equals(ranks[ranks.length-1])){
                editor.putBoolean(achievNames[11],true);
                editor.apply();
            }else if (!achievIsGained[12] && userHero.getClassRank().equals(ranks[ranks.length-1])){
                editor.putBoolean(achievNames[12],true);
                editor.apply();
            }else if (!achievIsGained[13] && userHero.getClassRank().equals(ranks[ranks.length-1])){
                editor.putBoolean(achievNames[13],true);
                editor.apply();
            }
            if (!achievIsGained[14] && failedQuest>=1){
                editor.putBoolean(achievNames[14],true);
                editor.apply();
            }else if (!achievIsGained[15] && failedQuest>=32){
                editor.putBoolean(achievNames[15],true);
                editor.apply();
            }else if (!achievIsGained[16] && failedQuest>=128){
                editor.putBoolean(achievNames[16],true);
                editor.apply();
            }else if (!achievIsGained[17] && failedQuest>=1024){
                editor.putBoolean(achievNames[17],true);
                editor.apply();
            }
            if(!achievIsGained[18] && userHero.getHeroLVL()>= 500){
                editor.putBoolean(achievNames[18],true);
                editor.apply();
            }
            return null;
        }
    }
}
