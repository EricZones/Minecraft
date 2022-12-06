// Created by Eric B. 05.12.2022 21:29
package de.ericzones.permissionsystem.listener;

import de.ericzones.permissionsystem.PermissionSystem;
import de.ericzones.permissionsystem.global.messaging.FileMessageEntry;
import de.ericzones.permissionsystem.global.messaging.MessageManager;
import de.ericzones.permissionsystem.group.Group;
import de.ericzones.permissionsystem.group.GroupManager;
import de.ericzones.permissionsystem.permission.PlayerPermissibleBase;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.reflect.Field;

public class PlayerConnectListener implements Listener {

    private final PermissionSystem instance;

    public PlayerConnectListener(PermissionSystem instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent e) {
        GroupManager groupManager = instance.getGroupManager();
        if(!groupManager.isPlayerInGroup(e.getUniqueId())) return;
        groupManager.updatePlayerNickname(e.getUniqueId(), e.getName());
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        // Replace vanilla hasPermission request with own one
        try {
            Class<?> craftHumanEntity = Class.forName("org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().split( "\\." )[3] + ".entity.CraftHumanEntity");
            Field field = craftHumanEntity.getDeclaredField("perm");
            field.setAccessible(true);
            field.set(e.getPlayer(), new PlayerPermissibleBase(instance, e.getPlayer()));
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        GroupManager groupManager = instance.getGroupManager();
        Group group = groupManager.getPlayerGroup(e.getPlayer().getUniqueId());
        MessageManager messageManager = instance.getMessageManager();
        e.setJoinMessage(messageManager.getMessage(FileMessageEntry.MESSAGE_JOIN).replace("%REPLACE%", group.getPrefix()+e.getPlayer().getName()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        GroupManager groupManager = instance.getGroupManager();
        Group group = groupManager.getPlayerGroup(e.getPlayer().getUniqueId());
        MessageManager messageManager = instance.getMessageManager();
        e.setQuitMessage(messageManager.getMessage(FileMessageEntry.MESSAGE_QUIT).replace("%REPLACE%", group.getPrefix()+e.getPlayer().getName()));
    }

}
