// Created by Eric B. 04.12.2022 17:58
package de.ericzones.permissionsystem.group;

public enum GroupProperty {

    ID(0),
    NAME(1),
    PREFIX(2),
    SORTING_ID(3),
    DEFAULT_GROUP(4);

    private GroupProperty(int id) {
        this.id = id;
    }

    private final int id;

    public int getId() {
        return id;
    }

    public static GroupProperty getGroupProperty(int id) {
        for(GroupProperty current : GroupProperty.values()) {
            if (current.getId() == id) return current;
        }
        return null;
    }

}
