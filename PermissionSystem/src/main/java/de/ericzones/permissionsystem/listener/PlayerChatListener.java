// Created by Eric B. 06.12.2022 18:48
package de.ericzones.permissionsystem.listener;

import de.ericzones.permissionsystem.PermissionSystem;
import de.ericzones.permissionsystem.group.Group;
import de.ericzones.permissionsystem.group.GroupManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    private final PermissionSystem instance;

    public PlayerChatListener(PermissionSystem instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        GroupManager groupManager = instance.getGroupManager();
        Group group = groupManager.getPlayerGroup(e.getPlayer().getUniqueId());
        e.setFormat(group.getPrefix()+e.getPlayer().getName()+" §7>§r %2$s");
    }

}
