package com.example.jarek.questtemporary.dataClasses;

import com.example.jarek.questtemporary.activityClasses.QuestPanelMain;

/**
 * Created by Jarek on 2017-04-21.
 */

public interface Watched {
    public void addObserver(QuestPanelMain o);
    public void delObserver(QuestPanelMain o);
    public void notifyObservers();
}
