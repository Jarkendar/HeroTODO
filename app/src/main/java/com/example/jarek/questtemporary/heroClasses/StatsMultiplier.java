package com.example.jarek.questtemporary.heroClasses;

/**
 * Created by Jarek on 2017-04-29.
 */

/**
 * Klasa przechowywująca mnożniki statystyk do klas. Kolejność statystyk:
 * siła, wytrzymałość, zręczność, inteligencja, mądrość, charyzma. Suma mnożników = 10
 */
public class StatsMultiplier {
    private double[] warriorMultiplier = {3.0, 2.0, 2.0, 1.0, 0.5, 1.5};
    private double[] hunterMultiplier = {1.0, 1.0, 3.0, 2.5, 2.5, 1.5};
    private double[] lordMultiplier = {1.0, 1.0, 1.0, 2.0, 2.0, 3.0};
    private double[] merchantMultiplier = {0.5, 1.0, 1.0, 3.0, 1.5, 3.0};
    private double[] bardMultiplier = {0.5, 1.0, 3.0, 1.5, 1.5, 2, 5};
    private double[] mageMultiplier = {0.5, 1.0, 2.0, 2.5, 2.5, 1.5};

    public double[] getWarriorMultiplier() {
        return warriorMultiplier;
    }

    public double[] getHunterMultiplier() {
        return hunterMultiplier;
    }

    public double[] getLordMultiplier() {
        return lordMultiplier;
    }

    public double[] getMerchantMultiplier() {
        return merchantMultiplier;
    }

    public double[] getBardMultiplier() {
        return bardMultiplier;
    }

    public double[] getMageMultiplier() {
        return mageMultiplier;
    }
}
