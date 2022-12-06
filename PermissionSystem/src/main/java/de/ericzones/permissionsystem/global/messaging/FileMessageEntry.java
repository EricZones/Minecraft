// Created by Eric B. 02.12.2022 10:27
package de.ericzones.permissionsystem.global.messaging;

public enum FileMessageEntry {

    MESSAGE_JOIN("Message_Join", new String[]{"%REPLACE% §7hat den Server betreten", "%REPLACE% §7joined the game"}),
    MESSAGE_QUIT("Message_Quit", new String[]{"%REPLACE% §7hat den Server verlassen", "%REPLACE% §7left the game"}),

    PLUGIN_INFO("Plugin_Info", new String[]{"§7Dieser Server verwendet §b%REPLACE%", "§7This server uses §b%REPLACE%"}),
    NO_PERMS("No_Permission", new String[]{"§7Fehlende §cRechte §7für diesen Befehl", "§7Missing §cpermissions §7for this command"}),
    WRONG_SYNTAX("Wrong_Syntax", new String[]{"§7Diese §cSyntax §7ist falsch. §8(§7/perm help§8)", "§7This §csyntax §7is wrong. §8(§7/perm help§8)"}),
    PLAYER_NOT_FOUND("Player_Not_Found", new String[]{"§7Dieser §cSpieler §7wurde nicht gefunden", "§7This §cplayer §7was not found"}),

    LANGUAGE_NOT_FOUND("Language_Not_Found", new String[]{"§7Diese §cSprache §7wurde nicht gefunden", "§7This §cgroup §7was not found"}),
    LANGUAGE_CHANGED("Language_Changed", new String[]{"§7Die Nachrichten wurden auf §b%REPLACE% §7gesetzt", "§7The messages have been set to §b%REPLACE%"}),

    GROUP_NOT_FOUND("Group_Not_Found", new String[]{"§7Diese §cGruppe §7wurde nicht gefunden", "§7This §cgroup §7was not found"}),
    GROUP_REMOVE_ERROR("Group_Remove_Error", new String[]{"§7Diese §cGruppe §7kann nicht entfernt werden", "§7This §cgroup §7can not be removed"}),
    GROUP_REMOVED("Group_Removed", new String[]{"§7Gruppe §b%REPLACE% §7wurde entfernt", "§7Group §b%REPLACE% §7has been removed"}),
    GROUP_PREFIX_TOO_LONG("Group_Prefix_Too_Long", new String[]{"§7Der §cPräfix §7ist auf 16 Zeichen beschränkt", "§7The §cprefix §7is limited to 16 characters"}),
    GROUP_SORTINGID_NUMBERS_ERROR("Group_SortingId_Numbers_Error", new String[]{"§7Die §cSortingId §7muss eine Zahl sein", "§7The §csortingId §7must be a number"}),
    GROUP_CREATE_ERROR("Group_Create_Error", new String[]{"§7Der Name §c%REPLACE% §7existiert bereits", "§7The name §c%REPLACE% §7already exists"}),
    GROUP_CREATED("Group_Created", new String[]{"§7Gruppe §b%REPLACE% §7wurde erstellt", "§7Group §b%REPLACE% §7has been created"}),
    GROUP_PERMS_NONE("Group_Permissions_None", new String[]{"§7Diese §cGruppe §7hat keine Rechte", "§7This §cgroup §7has no permissions"}),
    GROUP_PERMS_ADD_ERROR("Group_Permissions_Add_Error", new String[]{"§7Diese §cGruppe §7hat dieses Recht bereits", "§7This §cgroup §7already has the given permission"}),
    GROUP_PERMS_ADDED("Group_Permissions_Added", new String[]{"§7Das §bRecht §7wurde der Gruppe hinzugefügt", "§7The §bpermission §7has been added to the group"}),
    GROUP_PERMS_REMOVE_ERROR("Group_Permissions_Remove_Error", new String[]{"§7Diese §cGruppe §7hat dieses Recht nicht", "§7This §cgroup §7does not have this permission"}),
    GROUP_PERMS_REMOVED("Group_Permissions_Removed", new String[]{"§7Das §bRecht §7wurde von der Gruppe entfernt", "§7The §bpermission §7has been removed from the group"}),
    GROUP_UPDATED("Group_Updated", new String[]{"§7Gruppe §b%REPLACE% §7wurde bearbeitet", "§7Group §b%REPLACE% §7has been edited"}),

    USER_GROUP_REMOVE_ERROR("User_Group_Remove_Error", new String[]{"§7Dieser §cSpieler §7ist in keiner Gruppe", "§7This §cplayer §7is not member of any group"}),
    USER_GROUP_NOT_MEMBER("User_Group_Not_Member", new String[]{"§7Dieser §cSpieler §7ist nicht in dieser Gruppe", "§7This §cplayer §7is not member of this group"}),
    USER_GROUP_REMOVED("User_Group_Removed", new String[]{"§7Spieler §b%REPLACE% §7wurde aus der Gruppe §b%GROUP% §7entfernt", "§7Player §b%REPLACE% §7has been removed from §b%GROUP% §7group"}),
    USER_GROUP_ADDED("User_Group_Added", new String[]{"§7Spieler §b%REPLACE% §7in Gruppe §b%GROUP% §7hinzugefügt", "§7Player §b%REPLACE% §7has been added to §b%GROUP% §7group"}),
    USER_GROUP_ADD_ERROR("User_Group_Add_Error", new String[]{"§7Diese §cGruppe §7ist die standard Gruppe", "§7This §cgroup §7is the default group"}),
    USER_GROUP_ALREADY_MEMBER("User_Group_Already_Member", new String[]{"§7Dieser §cSpieler §7ist bereits in dieser Gruppe", "§7This §cplayer §7is already in this group"}),
    USER_GROUP_DURATION_INVALID("User_Group_Duration_Invalid", new String[]{"§7Die §cDauer §7muss eine positive Zahl sein", "§7The §cduration §7must be a positive number"}),
    USER_GROUP_ADDED_DURATION("User_Group_Added_Duration", new String[]{"§7Spieler §b%REPLACE% §7zeitweise in Gruppe §b%GROUP% §7hinzugefügt", "§7Player §b%REPLACE% §7has been added to §b%GROUP% §7group intermittently"}),

    HELP_LANGUAGE("Help_Language", new String[]{"§7Verändern der Sprache", "§7Change the language"}),
    HELP_USER_MANAGE("Help_User_Manage", new String[]{"§7Verwalten der Spielergruppe", "§7Manage player group"}),
    HELP_USER_INFO("Help_User_Info", new String[]{"§7Anzeigen von Spielerinformationen", "§7Display player information"}),
    HELP_GROUP_PERMS("Help_Group_Permissions", new String[]{"§7Verwalten der Rechte einer Gruppe", "§7Manage permissions of a group"}),
    HELP_GROUP_MANAGE("Help_Group_Manage", new String[]{"§7Verwalten der Gruppen", "§7Manage groups"}),
    HELP_GROUP_UPDATE("Help_Group_Update", new String[]{"§7Konfigurieren der Gruppen", "§7Configure groups"}),
    HELP_GROUP_LIST("Help_Group_List", new String[]{"§7Anzeigen der Gruppen", "§7Display all groups"});

    private FileMessageEntry(String name, String[] translations) {
        this.name = name;
        this.translations = translations;
    }

    private final String name;
    private final String[] translations;

    public String getName() {
        return name;
    }

    public String[] getTranslations() {
        return translations;
    }

    public String getTranslation(Language language) {
        return translations[language.getId()];
    }

}
