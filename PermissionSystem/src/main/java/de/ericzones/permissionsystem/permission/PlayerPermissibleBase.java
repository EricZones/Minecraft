// Created by Eric B. 06.12.2022 19:57
package de.ericzones.permissionsystem.permission;

import de.ericzones.permissionsystem.PermissionSystem;
import de.ericzones.permissionsystem.group.Group;
import de.ericzones.permissionsystem.group.GroupManager;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.jetbrains.annotations.NotNull;

public class PlayerPermissibleBase extends PermissibleBase {

    private final PermissionSystem instance;
    private final Player player;

    public PlayerPermissibleBase(PermissionSystem instance, Player player) {
        super(player);
        this.instance = instance;
        this.player = player;
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        GroupManager groupManager = instance.getGroupManager();
        Group group = groupManager.getPlayerGroup(player.getUniqueId());
        if(group.hasPermission("*"))
            return true;
        return group.hasPermission(permission);
    }

}
