// Created by Eric B. 04.12.2022 17:14
package de.ericzones.permissionsystem.group;

import de.ericzones.permissionsystem.PermissionSystem;
import de.ericzones.permissionsystem.global.database.Pair;
import de.ericzones.permissionsystem.global.database.SqlDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SqlGroup {

    private final PermissionSystem instance;

    private final String tableNameGroups = "Groups";
    private final String[] sqlKeysGroups = new String[]{"id", "name", "prefix", "sortingId", "defaultGroup"};
    private final SqlDataType[] sqlTypesGroups = new SqlDataType[]{SqlDataType.BIGINT, SqlDataType.VARCHAR, SqlDataType.VARCHAR, SqlDataType.BIGINT, SqlDataType.BOOLEAN};
    
    private final String tableNamePermissions = "Groups_Permissions";
    private final String[] sqlKeysPermissions = new String[]{"id", "permission"};
    private final SqlDataType[] sqlTypesPermissions = new SqlDataType[]{SqlDataType.BIGINT, SqlDataType.VARCHAR};

    private final String tableNamePlayers = "Groups_Players";
    private final String[] sqlKeysPlayers = new String[]{"uuid", "username", "id", "duration"};
    private final SqlDataType[] sqlTypesPlayers = new SqlDataType[]{SqlDataType.VARCHAR, SqlDataType.VARCHAR, SqlDataType.BIGINT, SqlDataType.BIGINT};

    /*
    Sql methods for group management
     */

    public SqlGroup(PermissionSystem instance) {
        this.instance = instance;
        instance.getDatabaseHandler().getSqlAdapter().createTable(tableNameGroups, getTableInformationGroups(), sqlKeysGroups[0], true);
        instance.getDatabaseHandler().getSqlAdapter().createTable(tableNamePermissions, getTableInformationPermissions());
        instance.getDatabaseHandler().getSqlAdapter().createTable(tableNamePlayers, getTableInformationPlayers(), sqlKeysPlayers[0]);
        if(!defaultGroupExists())
            createDefaultGroup();
    }

    private Pair<String, SqlDataType>[] getTableInformationGroups() {
        Pair<String, SqlDataType>[] content = new Pair[sqlKeysGroups.length];
        for(int i = 0; i < sqlKeysGroups.length; i++) content[i] = new Pair(sqlKeysGroups[i], sqlTypesGroups[i]);
        return content;
    }

    private Pair<String, SqlDataType>[] getTableInformationPermissions() {
        Pair<String, SqlDataType>[] content = new Pair[sqlKeysPermissions.length];
        for(int i = 0; i < sqlKeysPermissions.length; i++) content[i] = new Pair(sqlKeysPermissions[i], sqlTypesPermissions[i]);
        return content;
    }

    private Pair<String, SqlDataType>[] getTableInformationPlayers() {
        Pair<String, SqlDataType>[] content = new Pair[sqlKeysPlayers.length];
        for(int i = 0; i < sqlKeysPlayers.length; i++) content[i] = new Pair(sqlKeysPlayers[i], sqlTypesPlayers[i]);
        return content;
    }

    public boolean groupExists(String name) {
        return instance.getDatabaseHandler().getSqlAdapter().existsInTable(tableNameGroups, new Pair[]{new Pair(sqlKeysGroups[1], name)});
    }

    public boolean groupExists(int id) {
        return instance.getDatabaseHandler().getSqlAdapter().existsInTable(tableNameGroups, new Pair[]{new Pair(sqlKeysGroups[0], id)});
    }

    protected boolean defaultGroupExists() {
        return instance.getDatabaseHandler().getSqlAdapter().existsInTable(tableNameGroups, new Pair[]{new Pair(sqlKeysGroups[4], true)});
    }

    protected void createGroup(String name, String prefix, int sortingId, boolean defaultGroup) {
        List<String> list = new ArrayList<>(Arrays.asList(sqlKeysGroups));
        list.remove(sqlKeysGroups[0]);
        instance.getDatabaseHandler().getSqlAdapter().addToTable(tableNameGroups, list, Arrays.asList(name, prefix, sortingId, defaultGroup));
    }

    protected void deleteGroup(String name) {
        int id = getGroupId(name);
        instance.getDatabaseHandler().getSqlAdapter().removeFromTable(tableNameGroups, new Pair[]{new Pair(sqlKeysGroups[0], id)});
        instance.getDatabaseHandler().getSqlAdapter().removeFromTable(tableNamePermissions, new Pair[]{new Pair(sqlKeysPermissions[0], id)});
        instance.getDatabaseHandler().getSqlAdapter().removeFromTable(tableNamePlayers, new Pair[]{new Pair(sqlKeysPlayers[1], id)});
    }

    protected void updateGroup(String name, GroupProperty groupProperty, Object value) {
        instance.getDatabaseHandler().getSqlAdapter().updateInTable(tableNameGroups, new Pair[]{new Pair(sqlKeysGroups[1], name)}, sqlKeysGroups[groupProperty.getId()], value);
    }

    protected Object[] getGroupInformation(String name) {
        Object[] groupInformation = new Object[sqlKeysGroups.length];
        for(int i = 0; i < sqlKeysGroups.length; i++) {
            groupInformation[i] = instance.getDatabaseHandler().getSqlAdapter().getObjectFromTable(tableNameGroups, new Pair[]{new Pair(sqlKeysGroups[1], name)}, sqlKeysGroups[i]);
        }
        return groupInformation;
    }

    protected Object[] getGroupInformation(int id) {
        Object[] groupInformation = new Object[sqlKeysGroups.length];
        for(int i = 0; i < sqlKeysGroups.length; i++) {
            groupInformation[i] = instance.getDatabaseHandler().getSqlAdapter().getObjectFromTable(tableNameGroups, new Pair[]{new Pair(sqlKeysGroups[0], id)}, sqlKeysGroups[i]);
        }
        return groupInformation;
    }

    protected int getDefaultGroupId() {
        return Integer.parseInt(String.valueOf(instance.getDatabaseHandler().getSqlAdapter().getObjectFromTable(tableNameGroups, new Pair[]{new Pair(sqlKeysGroups[4], true)}, sqlKeysGroups[0])));
    }

    private void createDefaultGroup() {
        List<String> list = new ArrayList<>(Arrays.asList(sqlKeysGroups));
        list.remove(sqlKeysGroups[0]);
        instance.getDatabaseHandler().getSqlAdapter().addToTable(tableNameGroups, list, Arrays.asList("default", "§7Player ", 9, true));
    }

    // Get every groupID
    protected List<Integer> getGroupIds() {
        List<Integer> groupIds = new ArrayList<>();
        List<Object> list = instance.getDatabaseHandler().getSqlAdapter().getDataFromTable(tableNameGroups, null, sqlKeysGroups[0]);
        list.forEach(current -> groupIds.add(Integer.parseInt(String.valueOf(current))));
        return groupIds;
    }


    public boolean groupPermissionExists(String name, String permission) {
        int id = getGroupId(name);
        return instance.getDatabaseHandler().getSqlAdapter().existsInTable(tableNamePermissions, new Pair[]{new Pair(sqlKeysPermissions[0], id), new Pair(sqlKeysPermissions[1], permission)});
    }

    protected void addGroupPermission(String name, String permission) {
        int id = getGroupId(name);
        instance.getDatabaseHandler().getSqlAdapter().addToTable(tableNamePermissions, new Pair[]{new Pair(sqlKeysPermissions[0], id), new Pair(sqlKeysPermissions[1], permission)});
    }

    protected void removeGroupPermission(String name, String permission) {
        int id = getGroupId(name);
        instance.getDatabaseHandler().getSqlAdapter().removeFromTable(tableNamePermissions, new Pair[]{new Pair(sqlKeysPermissions[0], id), new Pair(sqlKeysPermissions[1], permission)});
    }

    protected List<String> getGroupPermissions(String name) {
        List<String> groupPermissions = new ArrayList<>();
        int id = getGroupId(name);
        List<Object> list = instance.getDatabaseHandler().getSqlAdapter().getDataFromTable(tableNamePermissions, new Pair[]{new Pair(sqlKeysPermissions[0], id)}, sqlKeysPermissions[1]);
        list.forEach(current -> groupPermissions.add((String) current));
        return groupPermissions;
    }


    protected boolean groupPlayerExists(String uuid) {
        return instance.getDatabaseHandler().getSqlAdapter().existsInTable(tableNamePlayers, new Pair[]{new Pair(sqlKeysPlayers[0], uuid)});
    }

    protected boolean groupPlayerExistsByNickname(String nickname) {
        return instance.getDatabaseHandler().getSqlAdapter().existsInTable(tableNamePlayers, new Pair[]{new Pair(sqlKeysPlayers[1], nickname)});
    }

    protected void addGroupPlayer(String uuid, String nickname, String groupName, long duration) {
        int id = getGroupId(groupName);
        instance.getDatabaseHandler().getSqlAdapter().addToTable(tableNamePlayers, new Pair[]{new Pair(sqlKeysPlayers[0], uuid), new Pair(sqlKeysPlayers[1], nickname), new Pair(sqlKeysPlayers[2], id), new Pair(sqlKeysPlayers[3], duration)});
    }

    protected void removeGroupPlayer(String uuid) {
        instance.getDatabaseHandler().getSqlAdapter().removeFromTable(tableNamePlayers, new Pair[]{new Pair(sqlKeysPlayers[0], uuid)});
    }

    // Get the duration of the group assignment
    protected long getGroupPlayerDuration(String uuid) {
        return Long.parseLong(String.valueOf(instance.getDatabaseHandler().getSqlAdapter().getObjectFromTable(tableNamePlayers, new Pair[]{new Pair(sqlKeysPlayers[0], uuid)}, sqlKeysPlayers[3])));
    }

    // Get the group of a player
    protected int getPlayerGroupId(String uuid) {
        return Integer.parseInt(String.valueOf(instance.getDatabaseHandler().getSqlAdapter().getObjectFromTable(tableNamePlayers, new Pair[]{new Pair(sqlKeysPlayers[0], uuid)}, sqlKeysPlayers[2])));
    }

    // Update player nickname in database
    protected void updatePlayerGroupNickname(String uuid, String nickname) {
        instance.getDatabaseHandler().getSqlAdapter().updateInTable(tableNamePlayers, new Pair[]{new Pair(sqlKeysPlayers[0], uuid)}, sqlKeysPlayers[1], nickname);
    }

    public String getPlayerNickname(String uuid) {
        return (String) instance.getDatabaseHandler().getSqlAdapter().getObjectFromTable(tableNamePlayers, new Pair[]{new Pair(sqlKeysPlayers[0], uuid)}, sqlKeysPlayers[1]);
    }

    public String getPlayerUniqueId(String nickname) {
        return (String) instance.getDatabaseHandler().getSqlAdapter().getObjectFromTable(tableNamePlayers, new Pair[]{new Pair(sqlKeysPlayers[1], nickname)}, sqlKeysPlayers[0]);
    }

    // Get GroupID by group name
    private int getGroupId(String name) {
        return Integer.parseInt(String.valueOf(instance.getDatabaseHandler().getSqlAdapter().getObjectFromTable(tableNameGroups, new Pair[]{new Pair(sqlKeysGroups[1], name)}, sqlKeysGroups[0])));
    }

}
