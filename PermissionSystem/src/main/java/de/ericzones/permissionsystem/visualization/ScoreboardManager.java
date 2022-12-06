// Created by Eric B. 06.12.2022 21:09
package de.ericzones.permissionsystem.visualization;

import de.ericzones.permissionsystem.PermissionSystem;
import de.ericzones.permissionsystem.group.Group;
import de.ericzones.permissionsystem.group.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;

public class ScoreboardManager implements Listener {

    private final PermissionSystem instance;

    public ScoreboardManager(PermissionSystem instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Bukkit.getOnlinePlayers().forEach(this::updatePlayerTeams);
    }

    private void updatePlayerTeams(Player player) {
        Scoreboard scoreboard = player.getScoreboard();

        GroupManager groupManager = instance.getGroupManager();
        Map<Integer, Team> teams = new HashMap<>();

        for(Group current : groupManager.getGroups()) {
            Team team = scoreboard.getTeam(current.getSortingId()+current.getName());
            if(team == null) {
                team = scoreboard.registerNewTeam(current.getSortingId()+current.getName());
                team.setPrefix(current.getPrefix());
            }
            teams.put(current.getId(), team);
        }

        for(Player current : Bukkit.getOnlinePlayers()) {
            Group group = groupManager.getPlayerGroup(current.getUniqueId());
            teams.get(group.getId()).addEntry(current.getName());
        }
    }

}
