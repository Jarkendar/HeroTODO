package com.example.jarek.questtemporary.dataClasses;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jarek.questtemporary.R;
import com.example.jarek.questtemporary.activityClasses.QuestPanel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;

/**
 * Created by Jarek on 2017-04-15.
 */

//TODO zrobić opis

public class RowAdapter extends ArrayAdapter<Quest> implements Watched {
    Context context;
    int layoutResourceID;
    LinkedList<Quest> data = null;
    int clickedRow = -1;
    ArrayList<QuestPanel> observers;
    String order;

    public RowAdapter(Context context, int layoutResourceID, LinkedList<Quest> data) {
        super(context, layoutResourceID, data);
        this.context = context;
        this.layoutResourceID = layoutResourceID;
        this.data = data;
        observers = new ArrayList<>();
    }

    public void setData(LinkedList<Quest> data) {
        this.data = data;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        RowQuestHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceID, parent, false);

            holder = new RowQuestHolder();
            holder.confirm = (Button) row.findViewById(R.id.button_Confirm);
            holder.cancel = (Button) row.findViewById(R.id.button_Cancel);
            holder.description = (TextView) row.findViewById(R.id.textView_Description);
            holder.reward = (TextView) row.findViewById(R.id.textView_Reward);
            holder.dateField = (TextView) row.findViewById(R.id.textView_TimeToLive);

            row.setTag(holder);
        } else {
            holder = (RowQuestHolder) row.getTag();
        }

        final Quest quest = data.get(position);
        holder.description.setText(quest.getDescription());
        holder.dateField.setText(quest.getDateFormatString());

        String dateText = quest.getDateFormatString() + "\n";

        if (quest.isRepeatable()) {
            dateText = dateText + getContext().getText(R.string.text_repeatEvery)
                    + " " + quest.getRepeatInterval()
                    + " " + getContext().getText(R.string.text_days);
        }

        holder.dateField.setText(dateText);


        String prize = getContext().getString(R.string.text_reward) + "\n";
        for (String x : quest.getAtributes()) {
            prize = prize + "\t\t+" + x + "\n";
        }
        prize = prize + getContext().getText(R.string.text_together)
                + " " + quest.getExperiencePoints()
                + "% " + getContext().getText(R.string.text_sufix_experiencePoints);
        holder.reward.setText(prize);

        if (!quest.parseQuestDateWithCurrDate()) {
            holder.confirm.setEnabled(false);
        } else {
            holder.confirm.setEnabled(true);
        }

        row.setOnClickListener(new View.OnClickListener() {//kliknięcie w wiersz
            @Override
            public void onClick(View view) {
                clickedRow = position;
                Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                order = "clickRow;" + position;
                notifyObservers();
            }
        });

        //nasłuchiwacz do akcepta
        holder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "ID number " + quest.getIdNumber(), Toast.LENGTH_SHORT).show();
                clickedRow = position;
                order = "succeed;" + position;
                notifyObservers();
            }
        });

        //nasłuchiwacz do cancela
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Cancel " + position, Toast.LENGTH_SHORT).show();
                clickedRow = position;
                order = "failed;" + position;
                notifyObservers();
            }
        });

        return row;
    }

    @Override
    public void addObserver(QuestPanel o) {
        observers.add(o);
    }

    @Override
    public void delObserver(QuestPanel o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (QuestPanel o : observers) {
            o.update(new Observable(), order);
        }
    }

    static class RowQuestHolder {
        TextView description;
        Button confirm;
        Button cancel;
        TextView reward;
        TextView dateField;
    }


}
