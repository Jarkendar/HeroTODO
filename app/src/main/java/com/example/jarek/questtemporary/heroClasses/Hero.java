package com.example.jarek.questtemporary.heroClasses;

/**
 * Created by Jarek on 2017-04-18.
 */

public abstract class Hero {
    private double strengthExperience;
    private double enduranceExperience;
    private double dexterityExperience;
    private double intelligenceExperience;
    private double wisdomExperience;
    private double charismaExperience;

    public Hero(double strengthExperience, double enduranceExperience, double dexterityExperience, double intelligenceExperience, double wisdomExperience, double charismaExperience) {
        this.strengthExperience = strengthExperience;
        this.enduranceExperience = enduranceExperience;
        this.dexterityExperience = dexterityExperience;
        this.intelligenceExperience = intelligenceExperience;
        this.wisdomExperience = wisdomExperience;
        this.charismaExperience = charismaExperience;
    }

    public abstract String getClassRank();
    public abstract String getRankDescription();
    public abstract int getHeroLVL();
    public abstract double getHeroEXP();

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
