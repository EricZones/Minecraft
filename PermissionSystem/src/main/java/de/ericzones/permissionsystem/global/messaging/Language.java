// Created by Eric B. 02.12.2022 20:52
package de.ericzones.permissionsystem.global.messaging;

public enum Language {

    GERMAN("de", 0),
    ENGLISH("en", 1);

    private Language(String synonym, int id) {
        this.synonym = synonym;
        this.id = id;
    }

    private final String synonym;
    private final int id;

    public String getSynonym() {
        return synonym;
    }

    public int getId() {
        return id;
    }

    public static Language getLanguageBySynonym(String synonym) {
        for(Language current : Language.values()) {
            if (current.getSynonym().equalsIgnoreCase(synonym)) return current;
        }
        return null;
    }

}
