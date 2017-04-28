package com.example.jarek.questtemporary.activityClasses;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jarek.questtemporary.R;
import com.example.jarek.questtemporary.dataClasses.FileManager;
import com.example.jarek.questtemporary.dataClasses.RowAdapter;
import com.example.jarek.questtemporary.dataClasses.Quest;
import com.example.jarek.questtemporary.heroClasses.Hero;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

public class QuestPanel extends AppCompatActivity implements Observer {

    private ProgressBar pBExperience;
    private LinkedList<Quest> quests;
    private ListView listView;
    private TextView tClassName, tClassLevel, tExperience;
    private RowAdapter rowAdapter;
    private int iposition;
    private Button buttonModify, buttonDelete;

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
        buttonModify.setEnabled(false);
        buttonDelete.setEnabled(false);

        adapterRefresh();
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
        rowAdapter.setData(quests);
        listView.setAdapter(rowAdapter);
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

    /**
     * Metoda wczytuje postać hero z sharedPreferences.
     */
    private void readHeroFromShared() {
        SharedPreferences sharedPreferences1 = getSharedPreferences(heroShared, MODE_PRIVATE);
        int heroClassID = sharedPreferences1.getInt("heroClass", R.string.class_native);
        tClassName.setText(getString(heroClassID));
        if (heroClassID != R.string.class_native) {
            double strength = (double) sharedPreferences1.getFloat("strength", 0);
            double endurance = (double) sharedPreferences1.getFloat("endurance", 0);
            double dexterity = (double) sharedPreferences1.getFloat("dexterity", 0);
            double intelligence = (double) sharedPreferences1.getFloat("intelligence", 0);
            double wisdom = (double) sharedPreferences1.getFloat("wisdom", 0);
            double charisma = (double) sharedPreferences1.getFloat("charisma", 0);
            switch (heroClassID) {
                case R.string.class_bard: {
                    double[] multipliers = {3, 2, 2, 1, 0.5, 1};
                    userHero = new Hero(strength, endurance, dexterity, intelligence, wisdom, charisma, getResources().getStringArray(R.array.bard_ranks), multipliers);
                    break;
                }
                case R.string.class_hunter: {
                    double[] multipliers = {3, 2, 2, 1, 0.5, 1};
                    userHero = new Hero(strength, endurance, dexterity, intelligence, wisdom, charisma, getResources().getStringArray(R.array.hunter_ranks), multipliers);
                    break;
                }
                case R.string.class_merchant: {
                    double[] multipliers = {3, 2, 2, 1, 0.5, 1};
                    userHero = new Hero(strength, endurance, dexterity, intelligence, wisdom, charisma, getResources().getStringArray(R.array.merchant_ranks), multipliers);
                    break;
                }
                case R.string.class_mage: {
                    double[] multipliers = {3, 2, 2, 1, 0.5, 1};
                    userHero = new Hero(strength, endurance, dexterity, intelligence, wisdom, charisma, getResources().getStringArray(R.array.mage_ranks), multipliers);
                    break;
                }
                case R.string.class_lord: {
                    double[] multipliers = {3, 2, 2, 1, 0.5, 1};
                    userHero = new Hero(strength, endurance, dexterity, intelligence, wisdom, charisma, getResources().getStringArray(R.array.lord_ranks), multipliers);
                    break;
                }
                case R.string.class_warrior: {
                    double[] multipliers = {3, 2, 2, 1, 0.5, 1};
                    userHero = new Hero(strength, endurance, dexterity, intelligence, wisdom, charisma, getResources().getStringArray(R.array.warrior_ranks), multipliers);
                    break;
                }
            }
            refreshHeroInfo();
        }
    }

    /**
     * Metoda odświeżająca komponenty Activity zwiazane z Hero.
     */
    private void refreshHeroInfo() {
        tClassName.setText(userHero.getClassRank());
        tClassLevel.setText(String.valueOf(userHero.getHeroLVL()).concat(" " + getString(R.string.text_level)));
        pBExperience.setProgress((int) userHero.getHeroEXP() % 100);
        tExperience.setText(String.valueOf((int) userHero.getHeroEXP() % 100).concat(getString(R.string.text_experienceEpmty)));
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
            editor.putFloat("intelligence", (float) userHero.getDexterityExperience());
            editor.putFloat("wisdom", (float) userHero.getWisdomExperience());
            editor.putFloat("charisma", (float) userHero.getCharismaExperience());
            editor.apply();
        }
    }


//TODO graficzna obróbka wierszy

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
        // show menu when menu button is pressed
        //TODO opisać
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
                Intent intent = new Intent(this, QuestAdding.class);
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
            case R.id.app_bar_option: {
                Intent intent = new Intent(this, OptionActivity.class);
                startActivity(intent);
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
                if (quests.get(position).isRepeatable()) {
                    Calendar calendar = quests.get(position).getTimeToLiveDate();
                    calendar.add(Calendar.DAY_OF_MONTH, quests.get(position).getRepeatInterval());
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

    public void clickModifyQuest(View view) {
        String attributes = "";
        for (String x : quests.get(iposition).getAtributes()) {
            attributes = attributes.concat(x + ";");
        }
        attributes = attributes.substring(0, attributes.length() - 1);
        Intent intent = new Intent(this, QuestAdding.class);
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
}
