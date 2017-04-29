package com.example.jarek.questtemporary.heroClasses;

/**
 * Created by Jarek on 2017-04-18.
 */
public class Hero {
    private double strengthExperience;
    private double enduranceExperience;
    private double dexterityExperience;
    private double intelligenceExperience;
    private double wisdomExperience;
    private double charismaExperience;
    private String[] ranksName;
    private double[] statsMultipliers;

    /**
     * Konstruktor klasy.
     *
     * @param strengthExperience     ilość doświadczenia statystyki siła
     * @param enduranceExperience    ilość doświadczenia statystyki wytrzymałość
     * @param dexterityExperience    ilość doświadczenia statystyki zręczność
     * @param intelligenceExperience ilość doświadczenia statystyki inteligencja
     * @param wisdomExperience       ilość doświadczenia statystyki mądrość
     * @param charismaExperience     ilość doświadczenia statystyki charyzma
     * @param ranksName              tablica nazw rang
     * @param statsMultipliers       tablica mnożników statystyk w kolejności siła, wytrzymałość, zręczność, inteligencja, mądrość, charyzma
     */
    public Hero(double strengthExperience, double enduranceExperience, double dexterityExperience, double intelligenceExperience, double wisdomExperience, double charismaExperience, String[] ranksName, double[] statsMultipliers) {
        this.strengthExperience = strengthExperience;
        this.enduranceExperience = enduranceExperience;
        this.dexterityExperience = dexterityExperience;
        this.intelligenceExperience = intelligenceExperience;
        this.wisdomExperience = wisdomExperience;
        this.charismaExperience = charismaExperience;
        this.ranksName = ranksName;
        this.statsMultipliers = statsMultipliers;
    }

    /**
     * Metoda dopasowująca rangę do aktualnego poziomu herosa.
     *
     * @return string będący nazwą rangi
     */
    public String getClassRank() {
        String heroReturnInfo = "";
        int levelMultiply = 25;

        for (int i = 0; i < ranksName.length; i++) {
            if (getHeroLVL() >= i * levelMultiply && getHeroLVL() < (i + 1) * levelMultiply) {
                heroReturnInfo = ranksName[i];
            }
        }
        if (getHeroLVL() >= ranksName.length * levelMultiply) {
            heroReturnInfo = ranksName[ranksName.length - 1];
        }
        return heroReturnInfo;
    }

    /**
     * Metoda obliczająca na podstawie aktualnego doświadczenia statystyk i mnożników klasy herosa
     * jego poziom.
     *
     * @return int będący poziomem herosa
     */
    public int getHeroLVL() {
        int lvl = (int) (getStrengthExperience() * statsMultipliers[0]
                + getEnduranceExperience() * statsMultipliers[1]
                + getDexterityExperience() * statsMultipliers[2]
                + getIntelligenceExperience() * statsMultipliers[3]
                + getWisdomExperience() * statsMultipliers[4]
                + getCharismaExperience() * statsMultipliers[5]);
        return lvl / 100;
    }

    /**
     * Metoda obliczająca sumę doświadczenia herosa wykorzystując poziom doświadczenia statystyk i
     * ich mnożniki.
     *
     * @return double będące obliczoną sumą doświadczenia
     */
    public double getHeroEXP() {
        return (getStrengthExperience() * statsMultipliers[0]
                + getEnduranceExperience() * statsMultipliers[1]
                + getDexterityExperience() * statsMultipliers[2]
                + getIntelligenceExperience() * statsMultipliers[3]
                + getWisdomExperience() * statsMultipliers[4]
                + getCharismaExperience() * statsMultipliers[5]);
    }

    /*Getery statystyk standardowe. Setery statystyk zamienione na metody dodające ilość
     * doświadczenia dostarczoną w parametrze do puli doświadczenia herosa. */

    public double getStrengthExperience() {
        return strengthExperience;
    }

    public void addStrengthExperience(double strengthExperience) {
        this.strengthExperience += strengthExperience;
    }

    public double getEnduranceExperience() {
        return enduranceExperience;
    }

    public void addEnduranceExperience(double enduranceExperience) {
        this.enduranceExperience += enduranceExperience;
    }

    public double getDexterityExperience() {
        return dexterityExperience;
    }

    public void addDexterityExperience(double dexterityExperience) {
        this.dexterityExperience += dexterityExperience;
    }

    public double getIntelligenceExperience() {
        return intelligenceExperience;
    }

    public void addIntelligenceExperience(double intelligenceExperience) {
        this.intelligenceExperience += intelligenceExperience;
    }

    public double getWisdomExperience() {
        return wisdomExperience;
    }

    public void addWisdomExperience(double wisdomExperience) {
        this.wisdomExperience += wisdomExperience;
    }

    public double getCharismaExperience() {
        return charismaExperience;
    }

    public void addCharismaExperience(double charismaExperience) {
        this.charismaExperience += charismaExperience;
    }
}
