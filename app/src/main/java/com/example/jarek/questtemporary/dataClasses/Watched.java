package com.example.jarek.questtemporary.dataClasses;

import com.example.jarek.questtemporary.activityClasses.QuestPanel;

/**
 * Created by Jarek on 2017-04-21.
 */

public interface Watched {
    public void addObserver(QuestPanel o);
    public void delObserver(QuestPanel o);
    public void notifyObservers();
}
