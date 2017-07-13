package com.example.jarek.questtemporary.dataClasses;

/**
 * Created by Jarek on 9 lip 2017.
 */

public class Achievement {
    private String name;
    private String description;
    private boolean gain;

    public Achievement(String name, String description, boolean gain) {
        this.name = name;
        this.description = description;
        this.gain = gain;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isGain() {
        return gain;
    }
}
