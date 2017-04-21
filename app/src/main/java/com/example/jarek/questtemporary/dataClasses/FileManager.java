package com.example.jarek.questtemporary.dataClasses;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

/**
 * Created by Jarek on 2017-04-17.
 */

public class FileManager {


    /**
     * Metoda serializująca zadania użytkownika w pliku "userQuestFile".
     */
    public void serializationQuests(LinkedList<Quest> questLinkedList, String userQuestFile, Context context) {
        try {
            File file = new  File(context.getFilesDir(), userQuestFile);
            ObjectOutputStream out = new ObjectOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(file)));
            for (int i = 0; i<questLinkedList.size(); i++) {
                out.writeObject(questLinkedList.get(i));
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LinkedList<Quest> deserializationQuests(String userQuestFile, Context context) {
        LinkedList<Quest> tmpQuest = new LinkedList<>();
        try {
            ObjectInputStream in = new ObjectInputStream(
                    new BufferedInputStream(
                            new FileInputStream(new File(context.getFilesDir(), userQuestFile))));
            while (true) {
                Quest tmp = (Quest) in.readObject();
                tmpQuest.addLast(tmp);
            }
        } catch (IOException | ClassNotFoundException e) {
            File file = new File(context.getFilesDir(), userQuestFile);
            file.deleteOnExit();
            e.printStackTrace();
        }
        return tmpQuest;
    }
}
