package com.example.jarek.questtemporary.dataClasses;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jarek.questtemporary.R;

import java.util.LinkedList;

/**
 * Created by Jarek on 9 lip 2017.
 */

public class AchievementRowAdapter extends ArrayAdapter<Achievement> {
    private Context context;
    private int layoutResourceID;
    private LinkedList<Achievement> achievements = null;
    private int textColorID;
    private int gainAchievColorID;
    private int notgainAchievColorID;

    public AchievementRowAdapter(Context context, int layoutResourceID, LinkedList<Achievement> achievements, int textColorID, int gainAchievColorID, int notgainAchievColorID) {
        super(context, layoutResourceID, achievements);
        this.context = context;
        this.layoutResourceID = layoutResourceID;
        this.achievements = achievements;
        this.textColorID = textColorID;
        this.gainAchievColorID = gainAchievColorID;
        this.notgainAchievColorID = notgainAchievColorID;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        final RowHolder holder;
        if (row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceID,parent,false);
            holder = new RowHolder();
            holder.rowName = (TextView)row.findViewById(R.id.textView_Achievement_Name);
            holder.rowDescription = (TextView)row.findViewById(R.id.textView_Achievement_Desciption);
            holder.rowBeam = (TextView)row.findViewById(R.id.textView_Achievement_Bottom_line);
            row.setTag(holder);
        }else{
            holder = (RowHolder) row.getTag();
        }
        holder.rowName.setText(achievements.get(position).getName());
        holder.rowDescription.setText(achievements.get(position).getDescription());
        holder.rowName.setTextColor(textColorID);
        holder.rowDescription.setTextColor(textColorID);
        holder.rowBeam.setTextColor(textColorID);
        row.setEnabled(achievements.get(position).isGain());

        if (achievements.get(position).isGain()){
            row.setBackgroundColor(gainAchievColorID);
        }else {
            row.setBackgroundColor(notgainAchievColorID);
        }

        return row;
    }

    static class RowHolder{
        TextView rowName;
        TextView rowDescription;
        TextView rowBeam;
    }
}
