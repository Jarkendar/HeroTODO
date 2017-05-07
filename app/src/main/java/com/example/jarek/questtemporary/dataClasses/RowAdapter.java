package com.example.jarek.questtemporary.dataClasses;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.jarek.questtemporary.R;
import com.example.jarek.questtemporary.activityClasses.QuestPanelMain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Observable;

/**
 * Created by Jarek on 2017-04-15.
 */
public class RowAdapter extends ArrayAdapter<Quest> implements Watched {
    private Context context;
    private int layoutResourceID;
    private LinkedList<Quest> data = null;
    private ArrayList<QuestPanelMain> observers;//tablica obserwatorów, których trzeba informować o zmianach
    private String order;//rozkaz przekazywany obserwatorom, przy zmianie parametru

    /**
     * Konstruktor klasy RowAdapter.
     * @param context obiekt łącznika pomiędzy plikami xml, a kodem java
     * @param layoutResourceID id layoutu podłączonego do adaptera
     * @param data lista zadań
     */
    public RowAdapter(Context context, int layoutResourceID, LinkedList<Quest> data) {
        super(context, layoutResourceID, data);
        this.context = context;
        this.layoutResourceID = layoutResourceID;
        this.data = data;
        observers = new ArrayList<>();
    }

    /**
     * Setter listy zadań.
     * @param data lista zadań
     */
    public void setData(LinkedList<Quest> data) {
        this.data = data;
    }


    /**
     * Metoda obsługująca każdy wiersz list view do którego adapter jest podłączony
     * @param position pozycja wiersza, int reprezentujący numer wiersza od 0
     * @param convertView widok wiersza
     * @param parent rodzic wiersza
     * @return View wiersza
     */
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        RowQuestHolder holder;

        if (row == null) {//jeśli wiersz jest pusty
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();//obiekt pośredniczący w podłączaniu layoutu
            row = inflater.inflate(layoutResourceID, parent, false);//podłącza pod wiersz layoutu layoutResourceID, dodaje mu rodzica parent, oraz nie podłącza go pod korzeń

            holder = new RowQuestHolder(); //tworzy nowy uchwyt danych przechowywujący i obsługujący komponenty wiersza
            //podłączenie komponentów wiersza
            holder.confirm = (Button) row.findViewById(R.id.button_Confirm);
            holder.cancel = (Button) row.findViewById(R.id.button_Cancel);
            holder.description = (TextView) row.findViewById(R.id.textView_Description);
            holder.reward = (TextView) row.findViewById(R.id.textView_Reward);
            holder.dateField = (TextView) row.findViewById(R.id.textView_TimeToLive);

            row.setTag(holder);//połączenie wiersza z uchwytem danych
        } else {
            holder = (RowQuestHolder) row.getTag();//pobranie uchwytu z wiersza
        }

        final Quest quest = data.get(position);//pobranie z listy zadań zadania tego konkretnego wiersza

        //wyświetlanie danych
        holder.description.setText(quest.getDescription());
        holder.dateField.setText(quest.getDateFormatString());

        String dateText = quest.getDateFormatString() + "\n";

        if (quest.isRepeatable()) {
            if (quest.getRepeatInterval()==1) {
                dateText = dateText + getContext().getText(R.string.text_repeatEvery)
                        + " " + quest.getRepeatInterval()
                        + " " + getContext().getText(R.string.text_day);
            }else{
                dateText = dateText + getContext().getText(R.string.text_repeatEvery)
                        + " " + quest.getRepeatInterval()
                        + " " + getContext().getText(R.string.text_days);
            }
        }

        holder.dateField.setText(dateText);


        String prize = getContext().getString(R.string.text_reward) + "\n";
        for (String x : quest.getAtributes()) {
            prize = prize + "\t\t+" + x + "\n";
        }
        prize = prize + getContext().getText(R.string.text_experienceTogether)
                + " " + quest.getExperiencePoints()
                + "% " + getContext().getText(R.string.text_sufix_experiencePoints);
        holder.reward.setText(prize);

        if (!quest.parseQuestDateWithCurrDate()) {
            holder.confirm.setEnabled(false);
            holder.confirm.setBackground(getContext().getResources().getDrawable(R.drawable.block_done_button));
        } else {
            holder.confirm.setEnabled(true);
            holder.confirm.setBackground(getContext().getResources().getDrawable(R.drawable.done_button));
        }

        /**
         * Listener kliknięcia wiersza. Tworzy rozkaz zawierający polecienie "clickRow;pozycja", i
         * wywołuje powiadomienie obserwatorów.
         */
        row.setOnClickListener(new View.OnClickListener() {//kliknięcie w wiersz
            @Override
            public void onClick(View view) {
                order = "clickRow;" + position;
                notifyObservers();
            }
        });

        /**
         * Listener kliknięcia przycisku accept. Tworzy rozkaz zawierający polecenie "succeed;pozycja", i
         * wywołuje powiadomienie obserwatorów.
         */
        holder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeAnimClick(view);
                Toast.makeText(context,context.getString(R.string.text_success),Toast.LENGTH_SHORT).show();
                order = "succeed;" + position;
                notifyObservers();
            }
        });

        /**
         * Listener kliknięcia przycisku cancel. Tworzy rozkaz zawierający polecenie "failed;pozycja", i
         * wywołuje powiadomienie obserwatorów.
         */
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeAnimClick(view);
                Toast.makeText(context,context.getString(R.string.text_fail),Toast.LENGTH_SHORT).show();
                order = "failed;" + position;
                notifyObservers();
            }
        });

        //kolorowanie wierszy parzystych na biało, nieparzystych na szaro
        if (position%2 == 0){
            row.setBackgroundColor(getContext().getResources().getColor(R.color.color_backgroundWhite));
        }else{
            row.setBackgroundColor(getContext().getResources().getColor(R.color.color_backgroundGray));
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.MILLISECOND,0);

        if(quest.getTimeToLiveDate().compareTo(calendar) < 0){
            row.setBackgroundColor(getContext().getResources().getColor(R.color.color_backgroundOrange));
        }


        return row;
    }

    /**
     * Metoda dodająca obserwatora.
     * @param o obiekt obserwatora
     */
    @Override
    public void addObserver(QuestPanelMain o) {
        observers.add(o);
    }

    /**
     * Metoda usuwająca obserwatora.
     * @param o obiekt obserwatora
     */
    @Override
    public void delObserver(QuestPanelMain o) {
        observers.remove(o);
    }

    /**
     * Metoda powiadamiająca wszystkich obserwatorów. Przekazuje obserwatorom rozkaz, który muszą obsłużyć.
     */
    @Override
    public void notifyObservers() {
        for (QuestPanelMain o : observers) {
            o.update(new Observable(), order);
        }
    }

    /**
     * Metoda odtwarzająca animacje zmiejsznia się okienka View o 10% w czasie 100ms.
     * @param view obiekt na którym ma być wykonana animacja
     */
    private void makeAnimClick(View view){
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.anim_click);
        view.startAnimation(animation);
    }

    /**
     * Klasa wewnętrza przechowywująca komponenty View.
     */
    private static class RowQuestHolder {
        TextView description;
        Button confirm;
        Button cancel;
        TextView reward;
        TextView dateField;
    }
}
