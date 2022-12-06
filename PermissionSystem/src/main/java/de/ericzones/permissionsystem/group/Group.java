// Created by Eric B. 04.12.2022 16:34
package de.ericzones.permissionsystem.group;

import java.util.List;

public class Group {

    private final int id;
    private final String name;
    private String prefix;
    private int sortingId;
    private boolean defaultGroup;

    private final List<String> permissions;

    public Group(int id, String name, String prefix, int sortingId, boolean defaultGroup, List<String> permissions) {
        this.id = id;
        this.name = name;
        this.prefix = prefix;
        this.sortingId = sortingId;
        this.defaultGroup = defaultGroup;
        this.permissions = permissions;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSortingId(int sortingId) {
        this.sortingId = sortingId;
    }

    public void setDefaultGroup(boolean defaultGroup) {
        this.defaultGroup = defaultGroup;
    }

    public void addPermission(String permission) {
        this.permissions.add(permission);
    }

    public void removePermission(String permission) {
        this.permissions.remove(permission);
    }

    public boolean hasPermission(String permission) {
        return this.permissions.contains(permission);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getSortingId() {
        return sortingId;
    }

    public boolean isDefaultGroup() {
        return defaultGroup;
    }

    public List<String> getPermissions() {
        return permissions;
    }
}
