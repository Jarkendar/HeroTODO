package com.example.jarek.questtemporary.dataClasses;

import android.app.Activity;
import android.app.AlertDialog;
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
 * Created by Jarek on 2017-04-25.
 */

public class HelpRowAdapter extends ArrayAdapter<HelpCategory> {
    private Context context;
    private int layoutResourceID;
    private LinkedList<HelpCategory> data = null;
    private int colorID;

    public HelpRowAdapter(Context context, int layoutResourceID, LinkedList<HelpCategory> data, int colorID) {
        super(context,layoutResourceID,data);
        this.context = context;
        this.layoutResourceID = layoutResourceID;
        this.data = data;
        this.colorID = colorID;
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
            holder.description = (TextView)row.findViewById(R.id.textView_Row);
            row.setTag(holder);
        }else {
            holder = (RowHolder)row.getTag();
        }
        final String category = data.get(position).getName();
        final String categoryDescription = data.get(position).getDescription();
        holder.description.setText(category);
        holder.description.setTextColor(colorID);
        holder.description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle(category)
                        .setMessage(categoryDescription)
                        .setNegativeButton(context.getText(R.string.text_close),null)
                        .show();
            }
        });
        return row;
    }


    static class RowHolder{
        TextView description;
    }
}
