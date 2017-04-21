package com.example.jarek.questtemporary.dataClasses;

import android.content.Context;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Jarek on 2017-04-15.
 */

public class Quest implements Serializable {
    private int idNumber;
    private String description;//przechowuje opis całego zadania
    private double experiencePoints;//przechowuje ogólną pulę doświadczenia za zadanie
    private Calendar timeToLiveDate;//przechowuje datę zakończenia się zadania
    private String[] atributes;//przechowuje listę statystyk do których dodane zostanie doświadczenie (dzielone po równo)
    private boolean repeatable;
    private int repeatInterval;

    public Calendar getTimeToLiveDate(){
        return timeToLiveDate;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public String getDescription() {
        return description;
    }

    public double getExperiencePoints() {
        return experiencePoints;
    }

    public String[] getAtributes() {
        return atributes;
    }

    public int getRepeatInterval() {
        return repeatInterval;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public Quest() {
    }

    public Quest(String description, double experiencePoints, Calendar timeToLiveDate, String[] atributes, boolean repeatable, int repeatInterval, Context context) {
        this.idNumber = GeneratorID.getID(context);
        this.description = description;
        this.experiencePoints = experiencePoints;
        this.timeToLiveDate = timeToLiveDate;
        this.atributes = atributes;
        this.repeatable = repeatable;
        if (repeatable) {
            this.repeatInterval = repeatInterval;
        } else {
            this.repeatInterval = 0;
        }
    }

    /**
     * Metoda zamienia datę zapisaną w zadaniu na łańcuch znaków.
     *
     * @return łańcuch reprezentujący datę
     */
    public String getDateFormatString() {
        String date = timeToLiveDate.get(Calendar.DAY_OF_MONTH)+"-"+(timeToLiveDate.get(Calendar.MONTH)+1)+"-"+timeToLiveDate.get(Calendar.YEAR);
        return date;
    }

    /**
     * Metoda prównuje obecną datę z datą zapisaną w obiekcie zadania.
     *
     * @return true jeśli data zadania jest większa od obecnej daty, false jeśli odwrotnie
     */
    public boolean parseQuestDateWithCurrDate() {
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());

        if (timeToLiveDate.get(Calendar.YEAR) < currentDate.get(Calendar.YEAR)) {
            return false;
        }
        if (timeToLiveDate.get(Calendar.MONTH) < currentDate.get(Calendar.MONTH)
                && timeToLiveDate.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)) {
            return false;
        }
        if (timeToLiveDate.get(Calendar.DAY_OF_MONTH) < currentDate.get(Calendar.DAY_OF_MONTH)
                && timeToLiveDate.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)
                && timeToLiveDate.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)) {
            return false;
        }

        return true;
    }

}
