// Created by Eric B. 05.12.2022 19:09
package de.ericzones.permissionsystem.commands;

import de.ericzones.permissionsystem.PermissionSystem;
import de.ericzones.permissionsystem.global.messaging.FileMessageEntry;
import de.ericzones.permissionsystem.global.messaging.Language;
import de.ericzones.permissionsystem.global.messaging.MessageManager;
import de.ericzones.permissionsystem.group.Group;
import de.ericzones.permissionsystem.group.GroupManager;
import de.ericzones.permissionsystem.group.GroupProperty;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class PermissionCommand implements CommandExecutor {

    private final PermissionSystem instance;

    public PermissionCommand(PermissionSystem instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command c, String alias, String[] args) {
        MessageManager messageManager = instance.getMessageManager();

        if(args.length == 0) {
            sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.PLUGIN_INFO).replace("%REPLACE%", "PermissionSystem"));
            return true;
        }

        if(args[0].equalsIgnoreCase("help")) {
            sender.sendMessage("\n\n§8§m--------------§r §7● §bPermission Help §7● §8§m--------------§r\n\n" +
                    " §8» §b/perm language <de | en> §8● "+messageManager.getMessage(FileMessageEntry.HELP_LANGUAGE)+"\n \n" +
                    " §8» §b/perm group <list> §8● "+messageManager.getMessage(FileMessageEntry.HELP_GROUP_LIST)+"\n \n" +
                    " §8» §b/perm group <groupName> <add | remove> <[prefix]> <[sortingId]>§8● "+messageManager.getMessage(FileMessageEntry.HELP_GROUP_MANAGE)+"\n \n" +
                    " §8» §b/perm group <groupName> set <prefix | sortingId> <value> §8● "+messageManager.getMessage(FileMessageEntry.HELP_GROUP_UPDATE)+"\n \n" +
                    " §8» §b/perm group <groupName> perms <add | remove | list> <[permission]>§8● "+messageManager.getMessage(FileMessageEntry.HELP_GROUP_PERMS)+"\n \n" +
                    " §8» §b/perm user <player> §8● "+messageManager.getMessage(FileMessageEntry.HELP_USER_INFO)+"\n \n" +
                    " §8» §b/perm user <player> <add | remove> <groupName> <[d]> <[h]> <[m]> <[s]> §8● "+messageManager.getMessage(FileMessageEntry.HELP_USER_MANAGE));
            return true;
        }
        GroupManager groupManager = instance.getGroupManager();

        if(args[0].equalsIgnoreCase("language") && args.length == 2) {
            if(!hasPermission(sender, "permissionsystem.language")) return true;

            Language language = Language.getLanguageBySynonym(args[1]);
            if(language == null) {
                sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.LANGUAGE_NOT_FOUND));
                return true;
            }
            messageManager.setLanguage(language);
            sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.LANGUAGE_CHANGED).replace("%REPLACE%", language.getSynonym()));
            return true;

        } else if(args[0].equalsIgnoreCase("group")) {

            if(args.length == 2 && args[1].equalsIgnoreCase("list")) {
                if(!hasPermission(sender, "permissionsystem.group.list")) return true;

                List<Group> groups = groupManager.getGroups();
                StringBuilder stringBuilder = new StringBuilder("\n\n§8§m-----------§r §7● §bGroup List §7● §8§m-----------§r\n\n");
                for(Group current : groups)
                    stringBuilder.append(" §8» §b"+current.getName()+" §8● '§7"+current.getPrefix().replace("§", "&")+"§8' §8● §7"+current.getSortingId()+"\n");
                sender.sendMessage(stringBuilder.toString());
                return true;
            }

            if(args.length == 3 && args[2].equalsIgnoreCase("remove")) {
                if(!hasPermission(sender, "permissionsystem.group.remove")) return true;

                String groupName = args[1].toLowerCase();
                if(!groupManager.groupExists(groupName)) {
                    sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.GROUP_NOT_FOUND));
                    return true;
                }
                Group group = groupManager.getGroup(groupName);
                if(group.isDefaultGroup()) {
                    sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.GROUP_REMOVE_ERROR));
                    return true;
                }
                groupManager.removeGroup(group);
                sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.GROUP_REMOVED).replace("%REPLACE%", groupName));
                return true;

            }
            if(args.length == 4) {
                String groupName = args[1].toLowerCase();
                if(args[2].equalsIgnoreCase("perms") && args[3].equalsIgnoreCase("list")) {
                    if(!hasPermission(sender, "permissionsystem.perms.list")) return true;

                    if(!groupManager.groupExists(groupName)) {
                        sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.GROUP_NOT_FOUND));
                        return true;
                    }
                    Group group = groupManager.getGroup(groupName);
                    if(group.getPermissions().size() == 0) {
                        sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.GROUP_PERMS_NONE));
                        return true;
                    }
                    List<String> permissions = group.getPermissions();
                    StringBuilder stringBuilder = new StringBuilder("\n\n§8§m--------§r §7● §b"+groupName+" Perms §7● §8§m--------§r\n\n");
                    for(String current : permissions)
                        stringBuilder.append(" §8» §7"+current+"\n");
                    sender.sendMessage(stringBuilder.toString());
                    return true;
                }
            }
            if(args.length == 5) {
                String groupName = args[1].toLowerCase();
                if(args[2].equalsIgnoreCase("perms")) {
                    if (!groupManager.groupExists(groupName)) {
                        sender.sendMessage(instance.getPluginPrefix() + messageManager.getMessage(FileMessageEntry.GROUP_NOT_FOUND));
                        return true;
                    }
                    Group group = groupManager.getGroup(groupName);
                    String permission = args[4];
                    if (args[3].equalsIgnoreCase("add")) {
                        if(!hasPermission(sender, "permissionsystem.perms.add")) return true;

                        if (group.hasPermission(permission)) {
                            sender.sendMessage(instance.getPluginPrefix() + messageManager.getMessage(FileMessageEntry.GROUP_PERMS_ADD_ERROR));
                            return true;
                        }
                        groupManager.addPermissionToGroup(group, permission);
                        sender.sendMessage(instance.getPluginPrefix() + messageManager.getMessage(FileMessageEntry.GROUP_PERMS_ADDED));
                        return true;
                    } else if (args[3].equalsIgnoreCase("remove")) {
                        if(!hasPermission(sender, "permissionsystem.perms.remove")) return true;

                        if (!group.hasPermission(permission)) {
                            sender.sendMessage(instance.getPluginPrefix() + messageManager.getMessage(FileMessageEntry.GROUP_PERMS_REMOVE_ERROR));
                            return true;
                        }
                        groupManager.removePermissionFromGroup(group, permission);
                        sender.sendMessage(instance.getPluginPrefix() + messageManager.getMessage(FileMessageEntry.GROUP_PERMS_REMOVED));
                        return true;
                    }
                } else if(args[2].equalsIgnoreCase("set")) {
                    if(!hasPermission(sender, "permissionsystem.group.set")) return true;

                    if (!groupManager.groupExists(groupName)) {
                        sender.sendMessage(instance.getPluginPrefix() + messageManager.getMessage(FileMessageEntry.GROUP_NOT_FOUND));
                        return true;
                    }
                    Group group = groupManager.getGroup(groupName);
                    String value = args[4];
                    if(args[3].equalsIgnoreCase("prefix")) {
                        if(value.length() > 16) {
                            sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.GROUP_PREFIX_TOO_LONG));
                            return true;
                        }
                        value = value.replaceAll("&", "§");
                        groupManager.updateGroup(group, GroupProperty.PREFIX, value);
                        sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.GROUP_UPDATED).replace("%REPLACE%", groupName));
                        return true;
                    } else if(args[3].equalsIgnoreCase("sortingid")) {
                        int sortingId;
                        try {
                            sortingId = Integer.parseInt(value);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.GROUP_SORTINGID_NUMBERS_ERROR));
                            return true;
                        }
                        groupManager.updateGroup(group, GroupProperty.SORTING_ID, sortingId);
                        sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.GROUP_UPDATED).replace("%REPLACE%", groupName));
                        return true;
                    }
                } else if(args[2].equalsIgnoreCase("add")) {
                    if(!hasPermission(sender, "permissionsystem.group.add")) return true;

                    String prefix = args[3];
                    if(prefix.length() > 16) {
                        sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.GROUP_PREFIX_TOO_LONG));
                        return true;
                    }
                    prefix = prefix.replaceAll("&", "§");
                    int sortingId;
                    try {
                        sortingId = Integer.parseInt(args[4]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.GROUP_SORTINGID_NUMBERS_ERROR));
                        return true;
                    }
                    if(groupManager.groupExists(groupName) || groupName.equals("default")) {
                        sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.GROUP_CREATE_ERROR).replace("%REPLACE%", groupName));
                        return true;
                    }
                    groupManager.createGroup(groupName, prefix, sortingId);
                    sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.GROUP_CREATED).replace("%REPLACE%", groupName));
                    return true;
                }
            }
        } else if(args[0].equalsIgnoreCase("user")) {
            Player target = Bukkit.getPlayer(args[1]);
            if(args.length == 2) {
                if(target != null) {
                    if(sender instanceof Player && !sender.getName().equals(target.getName())) {
                        if(!hasPermission(sender, "permissionsystem.user.info")) return true;
                    }
                    Group group = groupManager.getPlayerGroup(target.getUniqueId());
                    sender.sendMessage(instance.getPluginPrefix()+"§b"+target.getName()+" §8» §7"+group.getName()+" §8● §7"+groupManager.getPlayerGroupDuration(target.getUniqueId()));

                } else if(!groupManager.isPlayerInGroup(args[1])) {
                    if(!hasPermission(sender, "permissionsystem.user.info")) return true;

                    Group group = groupManager.getDefaultGroup();
                    sender.sendMessage(instance.getPluginPrefix()+"§b"+args[1]+" §8» §7"+group.getName()+" §8● §7Lifetime");

                } else if(groupManager.isPlayerInGroup(args[1])) {
                    if(!hasPermission(sender, "permissionsystem.user.info")) return true;

                    UUID uuid = UUID.fromString(groupManager.getPlayerUniqueId(args[1]));
                    Group group = groupManager.getPlayerGroup(uuid);
                    sender.sendMessage(instance.getPluginPrefix()+"§b"+args[1]+" §8» §7"+group.getName()+" §8● §7"+groupManager.getPlayerGroupDuration(uuid));
                }
                return true;
            }
            if(args.length == 4) {
                String playerName = args[1];
                if(!groupManager.isPlayerInGroup(playerName) && target == null) {
                    sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.PLAYER_NOT_FOUND));
                    return true;
                }
                UUID uuid;
                if(target != null) {
                    playerName = target.getName();
                    uuid = target.getUniqueId();
                } else
                    uuid = UUID.fromString(groupManager.getPlayerUniqueId(playerName));

                String groupName = args[3].toLowerCase();
                if(!groupManager.groupExists(groupName)) {
                    sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.GROUP_NOT_FOUND));
                    return true;
                }
                Group group = groupManager.getPlayerGroup(uuid);

                if(args[2].equalsIgnoreCase("add")) {
                    if(!hasPermission(sender, "permissionsystem.user.add")) return true;

                    if(!isGroupAddable(sender, group, groupName)) return true;
                    groupManager.addPlayerToGroup(uuid, playerName, groupManager.getGroup(groupName), -1);
                    sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.USER_GROUP_ADDED).replace("%GROUP%", groupName).replace("%REPLACE%", playerName));
                    return true;

                } else if(args[2].equalsIgnoreCase("remove")) {
                    if(!hasPermission(sender, "permissionsystem.user.remove")) return true;

                    if(!groupManager.isPlayerInGroup(uuid)) {
                        sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.USER_GROUP_REMOVE_ERROR));
                        return true;
                    }
                    if(group.getId() != groupManager.getGroup(groupName).getId()) {
                        sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.USER_GROUP_NOT_MEMBER));
                        return true;
                    }
                    groupManager.removePlayerFromGroup(uuid);
                    sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.USER_GROUP_REMOVED).replace("%GROUP%", groupName).replace("%REPLACE%", playerName));
                    return true;
                }
            }
            if(args.length == 8 && args[2].equalsIgnoreCase("add")) {
                if(!hasPermission(sender, "permissionsystem.group.add")) return true;

                String playerName = args[1];
                if(!groupManager.isPlayerInGroup(playerName) && target == null) {
                    sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.PLAYER_NOT_FOUND));
                    return true;
                }
                UUID uuid;
                if(target != null) {
                    playerName = target.getName();
                    uuid = target.getUniqueId();
                } else
                    uuid = UUID.fromString(groupManager.getPlayerUniqueId(playerName));

                String groupName = args[3].toLowerCase();
                if(!groupManager.groupExists(groupName)) {
                    sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.GROUP_NOT_FOUND));
                    return true;
                }
                Group group = groupManager.getPlayerGroup(uuid);
                if(!isGroupAddable(sender, group, groupName)) return true;

                int days = getNumberFromString(args[4]); int hours = getNumberFromString(args[5]);
                int minutes = getNumberFromString(args[6]); int seconds = getNumberFromString(args[7]);
                if(days == -1 || hours == -1 || minutes == -1 || seconds == -1) {
                    sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.USER_GROUP_DURATION_INVALID));
                    return true;
                }

                groupManager.addPlayerToGroup(uuid, playerName, groupManager.getGroup(groupName), getDurationMillis(days, hours, minutes, seconds));
                sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.USER_GROUP_ADDED_DURATION).replace("%GROUP%", groupName).replace("%REPLACE%", playerName));
                return true;
            }
        }

        sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.WRONG_SYNTAX));
        return true;
    }

    private boolean hasPermission(CommandSender sender, String permission) {
        if(!(sender instanceof Player)) return true;
        if(sender.hasPermission(permission)) return true;
        MessageManager messageManager = instance.getMessageManager();
        sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.NO_PERMS));
        return false;
    }

    private boolean isGroupAddable(CommandSender sender, Group currentGroup, String groupName) {
        MessageManager messageManager = instance.getMessageManager();
        GroupManager groupManager = instance.getGroupManager();

        if(groupManager.getGroup(groupName).isDefaultGroup()) {
            sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.USER_GROUP_ADD_ERROR));
            return false;
        }
        if(currentGroup.getId() == groupManager.getGroup(groupName).getId()) {
            sender.sendMessage(instance.getPluginPrefix()+messageManager.getMessage(FileMessageEntry.USER_GROUP_ALREADY_MEMBER));
            return false;
        }
        return true;
    }

    private int getNumberFromString(String string) {
        int number;
        try {
            number = Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return -1;
        }
        if(number < 0) return -1;
        return number;
    }

    private long getDurationMillis(int days, int hours, int minutes, int seconds) {
        return (seconds * 1000L) + (minutes * 60 * 1000L) + (hours * 60 * 60 * 1000L) + (days * 24 * 60 * 60 * 1000L);
    }

}
