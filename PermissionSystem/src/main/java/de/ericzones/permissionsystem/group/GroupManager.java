// Created by Eric B. 04.12.2022 16:28
package de.ericzones.permissionsystem.group;

import de.ericzones.permissionsystem.PermissionSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GroupManager extends SqlGroup {

    private final PermissionSystem instance;

    public GroupManager(PermissionSystem instance) {
        super(instance);
        this.instance = instance;
    }

    public Group getGroup(String name) {
        Object[] groupInformation = getGroupInformation(name);
        int id = -1; int sortingId = -1; String prefix = null; boolean defaultGroup = false;
        for(int i = 0; i < groupInformation.length; i++) {
            switch (i) {
                case 0 -> id = Integer.parseInt(String.valueOf(groupInformation[i]));
                case 2 -> prefix = (String) groupInformation[i];
                case 3 -> sortingId = Integer.parseInt(String.valueOf(groupInformation[i]));
                case 4 -> defaultGroup = Boolean.parseBoolean(String.valueOf(groupInformation[i]));
            }
        }
        return new Group(id, name, prefix, sortingId, defaultGroup, getGroupPermissions(name));
    }

    public Group getGroup(int id) {
        Object[] groupInformation = getGroupInformation(id);
        String name = null; int sortingId = -1; String prefix = null; boolean defaultGroup = false;
        for(int i = 0; i < groupInformation.length; i++) {
            switch (i) {
                case 1 -> name = (String) groupInformation[i];
                case 2 -> prefix = (String) groupInformation[i];
                case 3 -> sortingId = Integer.parseInt(String.valueOf(groupInformation[i]));
                case 4 -> defaultGroup = Boolean.parseBoolean(String.valueOf(groupInformation[i]));
            }
        }
        return new Group(id, name, prefix, sortingId, defaultGroup, getGroupPermissions(name));
    }

    public Group getPlayerGroup(UUID uuid) {
        Group group;
        if(isPlayerInGroup(uuid)) {
            int groupId = getPlayerGroupId(uuid.toString());
            group = getGroup(groupId);
        } else
            group = getGroup(getDefaultGroupId());
        return group;
    }

    public Group getDefaultGroup() {
        return getGroup(getDefaultGroupId());
    }

    public List<Group> getGroups() {
        List<Group> groups = new ArrayList<>();
        List<Integer> groupIds = getGroupIds();
        for(int current : groupIds)
            groups.add(getGroup(current));
        return groups;
    }

    public boolean isPlayerInGroup(UUID uuid) {
        checkPlayerGroup(uuid);
        return groupPlayerExists(uuid.toString());
    }

    public boolean isPlayerInGroup(String nickname) {
        checkPlayerGroup(nickname);
        return groupPlayerExistsByNickname(nickname);
    }

    public void addPlayerToGroup(UUID uuid, String nickname, Group group, long duration) {
        if(group.isDefaultGroup()) return;
        if(isPlayerInGroup(uuid)) removePlayerFromGroup(uuid);
        if(duration != -1)
            addGroupPlayer(uuid.toString(), nickname, group.getName(), duration+System.currentTimeMillis());
        else
            addGroupPlayer(uuid.toString(), nickname, group.getName(), duration);
    }

    public void removePlayerFromGroup(UUID uuid) {
        if(!isPlayerInGroup(uuid)) return;
        removeGroupPlayer(uuid.toString());
    }

    public void createGroup(String name, String prefix, int sortingId) {
        if(groupExists(name)) return;
        if(name.equalsIgnoreCase("default")) return;
        if(sortingId < -1) sortingId = -1;
        createGroup(name.toLowerCase(), prefix+" ", sortingId, false);
    }

    public void removeGroup(Group group) {
        if(group.isDefaultGroup()) return;
        deleteGroup(group.getName());
    }

    public void updateGroup(Group group, GroupProperty groupProperty, Object value) {
        if(groupProperty == GroupProperty.ID || groupProperty == GroupProperty.NAME || groupProperty == GroupProperty.DEFAULT_GROUP) return;
        if(groupProperty == GroupProperty.SORTING_ID && (int) value < -1) value = -1;
        if(groupProperty == GroupProperty.PREFIX) value = value+" ";
        updateGroup(group.getName(), groupProperty, value);
    }

    public void addPermissionToGroup(Group group, String permission) {
        if(group.hasPermission(permission)) return;
        addGroupPermission(group.getName(), permission);
    }

    public void removePermissionFromGroup(Group group, String permission) {
        if(!group.hasPermission(permission)) return;
        removeGroupPermission(group.getName(), permission);
    }

    public boolean isGroupExpired(UUID uuid, Group group) {
        if(group.isDefaultGroup()) return false;
        long duration = getGroupPlayerDuration(uuid.toString());
        if(duration == -1) return false;
        return getGroupPlayerDuration(uuid.toString()) < System.currentTimeMillis();
    }

    public void updatePlayerNickname(UUID uuid, String nickname) {
        if(!isPlayerInGroup(uuid)) return;
        if(nickname.equals(getPlayerNickname(uuid.toString()))) return;
        updatePlayerGroupNickname(uuid.toString(), nickname);
    }

    public String getPlayerGroupDuration(UUID uuid) {
        if(!isPlayerInGroup(uuid)) return "Lifetime";
        long playtimeMillis = getGroupPlayerDuration(uuid.toString());
        if(playtimeMillis == -1) return "Lifetime";
        playtimeMillis = playtimeMillis - System.currentTimeMillis();

        long seconds = 0, minutes = 0, hours = 0, days = 0;
        while(playtimeMillis > 1000) {
            playtimeMillis-=1000;
            seconds++;
        }
        while(seconds > 60) {
            seconds-=60;
            minutes++;
        }
        while(minutes > 60) {
            minutes-=60;
            hours++;
        }
        while(hours > 24) {
            hours-=24;
            days++;
        }
        return days + "d " + hours + "h " + minutes + "m " + seconds + "s";
    }

    // Check if group is expired
    private void checkPlayerGroup(UUID uuid) {
        if(!groupPlayerExists(uuid.toString())) return;
        long duration = getGroupPlayerDuration(uuid.toString());
        if(duration == -1 || duration > System.currentTimeMillis()) return;
        removeGroupPlayer(uuid.toString());
    }

    // Check if group is expired
    private void checkPlayerGroup(String nickname) {
        if(!groupPlayerExistsByNickname(nickname)) return;
        UUID uuid = UUID.fromString(getPlayerUniqueId(nickname));
        checkPlayerGroup(uuid);
    }

}
