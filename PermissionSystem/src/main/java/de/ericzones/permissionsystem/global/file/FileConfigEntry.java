// Created by Eric B. 02.12.2022 10:18
package de.ericzones.permissionsystem.global.file;

public enum FileConfigEntry {

    HOST("host", "127.0.0.1"),
    PORT("port", 3306),
    DATABASE("database", "database"),
    USERNAME("username", "username"),
    PASSWORD("password", "password"),
    LANGUAGE("language", "de");


    private FileConfigEntry(String name, Object preset) {
        this.name = name;
        this.preset = preset;
    }

    private final String name;
    private final Object preset;

    public String getName() {
        return name;
    }

    public Object getPreset() {
        return preset;
    }

}
