// Created by Eric B. 06.12.2022 20:38
package de.ericzones.permissionsystem.listener;

import de.ericzones.permissionsystem.PermissionSystem;
import de.ericzones.permissionsystem.group.Group;
import de.ericzones.permissionsystem.group.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.util.UUID;

public class SignChangeListener implements Listener {

    private final PermissionSystem instance;

    public SignChangeListener(PermissionSystem instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        if(e.getLines().length == 0) return;
        if(e.getLine(0) == null) return;
        if(e.getLine(0).startsWith("[") && e.getLine(0).endsWith("]")) {
            /*
            Creating sign display player information
            First line: [playername]
             */
            String name = e.getLine(0).replace("[", "").replace("]", "");
            if(name.length() == 0) return;

            GroupManager groupManager = instance.getGroupManager();
            Player target = Bukkit.getPlayer(name);
            if(target != null) {
                name = target.getName(); Group group = groupManager.getPlayerGroup(target.getUniqueId());
                String duration = groupManager.getPlayerGroupDuration(target.getUniqueId());
                e.setLine(0, name); e.setLine(1, group.getName());
                e.setLine(2, duration);
                return;
            }
            if(!groupManager.isPlayerInGroup(name)) return;
            UUID uuid = UUID.fromString(groupManager.getPlayerUniqueId(name));
            Group group = groupManager.getPlayerGroup(uuid);
            String duration = groupManager.getPlayerGroupDuration(uuid);
            e.setLine(0, name); e.setLine(1, group.getName());
            e.setLine(2, duration);
        }
    }


}
