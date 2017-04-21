package com.example.jarek.questtemporary.heroClasses;

/**
 * Created by Jarek on 2017-04-21.
 */

public class Warrior extends Hero {

    private WarriorRank warriorRank;
    private double[] multipliers = {3, 2, 2, 1, 0.5, 1};


    public Warrior(double strengthExperience, double endurancePoints, double dexterityPoints, double intelligencePoints, double wisdomPoints, double charismaPoints) {
        super(strengthExperience, endurancePoints, dexterityPoints, intelligencePoints, wisdomPoints, charismaPoints);
        warriorRank = new WarriorRank();
    }

    @Override
    public String getClassRank() {
        return warriorRank.getRank();
    }

    @Override
    public String getRankDescription() {
        return warriorRank.getRankDescription();
    }

    @Override
    public int getHeroLVL() {
        int lvl = (int) (getStrengthExperience() * multipliers[0]
                + getEnduranceExperience() * multipliers[1]
                + getDexterityExperience() * multipliers[2]
                + getIntelligenceExperience() * multipliers[3]
                + getWisdomExperience() * multipliers[4]
                + getCharismaExperience() * multipliers[5]);
        return lvl / 100;
    }

    @Override
    public double getHeroEXP() {
        return (getStrengthExperience() * multipliers[0]
                + getEnduranceExperience() * multipliers[1]
                + getDexterityExperience() * multipliers[2]
                + getIntelligenceExperience() * multipliers[3]
                + getWisdomExperience() * multipliers[4]
                + getCharismaExperience() * multipliers[5]);
    }


}
