package com.example.jarek.questtemporary.dataClasses;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.jarek.questtemporary.R;

import java.util.Calendar;

/**
 * Created by Jarek on 23 lip 2017.
 */

public class HistoryRowAdapter extends CursorAdapter {

    private LayoutInflater cursorInflater;
    private int textColor;
    private int succeedQuestColor;
    private int failedQuestColor;


    public HistoryRowAdapter(Context context, Cursor cursor, int[] colors) {
        super(context, cursor, 0);
        textColor = colors[0];
        failedQuestColor = colors[1];
        succeedQuestColor = colors[2];
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return cursorInflater.inflate(R.layout.row_history_layout, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String description = cursor.getString(cursor.getColumnIndex("DESCRIPTION"));
        double experience = cursor.getDouble(cursor.getColumnIndex("EXPERIENCE"));
        Calendar dateEnd = Calendar.getInstance();
        dateEnd.setTimeInMillis(cursor.getLong(cursor.getColumnIndex("DATE_END")));
        Calendar dateQuestEnd = Calendar.getInstance();
        dateQuestEnd.setTimeInMillis(cursor.getLong(cursor.getColumnIndex("DATE_QUEST_END")));
        boolean succeed = (cursor.getInt(cursor.getColumnIndex("SUCCEED")) == 1);

        if (succeed) {
            view.setBackgroundColor(succeedQuestColor);
        }else {
            view.setBackgroundColor(failedQuestColor);
        }
        TextView descriptionTextView = (TextView)view.findViewById(R.id.textView_descriptionHistory);
        TextView experienceTextView = (TextView)view.findViewById(R.id.textView_experienceHistory);
        TextView dateTextView = (TextView)view.findViewById(R.id.textView_dateHistory);
        descriptionTextView.setTextColor(textColor);
        descriptionTextView.setText(description);
        experienceTextView.setTextColor(textColor);
        experienceTextView.setText(context.getString(R.string.text_experienceTogether) + " "+Double.toString(experience));
        dateTextView.setTextColor(textColor);
        dateTextView.setText(context.getString(R.string.text_dateEnd)+" "+getDateFormatString(dateQuestEnd)+
                    "  "+context.getString(R.string.text_dateQuestEnd)+" "+getDateFormatString(dateEnd));
    }

    private String getDateFormatString(Calendar calendar) {
        String date;
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
            date = "0" + calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            date = "" + calendar.get(Calendar.DAY_OF_MONTH);
        }
        if (calendar.get(Calendar.MONTH) + 1 < 10) {
            date = date.concat("-0" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR));
        } else {
            date = date.concat("-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR));
        }
        return date;
    }
}
