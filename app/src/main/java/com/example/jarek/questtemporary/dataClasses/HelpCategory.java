package com.example.jarek.questtemporary.dataClasses;

/**
 * Created by Jarek on 2017-04-25.
 */

public class HelpCategory {
    private String name;
    private String description;

    public HelpCategory(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
