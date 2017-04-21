package com.example.jarek.questtemporary.heroClasses;

/**
 * Created by Jarek on 2017-04-21.
 */

public class Mage extends Hero {

    public Mage(double strengthExperience, double endurancePoints, double dexterityPoints, double intelligencePoints, double wisdomPoints, double charismaPoints) {
        super(strengthExperience, endurancePoints, dexterityPoints, intelligencePoints, wisdomPoints, charismaPoints);
    }

    @Override
    public String getClassRank() {
        return null;
    }

    @Override
    public String getRankDescription() {
        return null;
    }

    @Override
    public int getHeroLVL() {
        return 0;
    }

    @Override
    public double getHeroEXP() {
        return 0;
    }
}
