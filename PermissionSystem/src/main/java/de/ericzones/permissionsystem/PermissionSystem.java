// Created by Eric B. 29.11.2022 16:12
package de.ericzones.permissionsystem;

import de.ericzones.permissionsystem.commands.PermissionCommand;
import de.ericzones.permissionsystem.global.database.DatabaseHandler;
import de.ericzones.permissionsystem.global.file.FileConfigEntry;
import de.ericzones.permissionsystem.global.file.FileManager;
import de.ericzones.permissionsystem.global.messaging.Language;
import de.ericzones.permissionsystem.global.messaging.MessageManager;
import de.ericzones.permissionsystem.group.GroupManager;
import de.ericzones.permissionsystem.listener.PlayerChatListener;
import de.ericzones.permissionsystem.listener.PlayerConnectListener;
import de.ericzones.permissionsystem.listener.SignChangeListener;
import de.ericzones.permissionsystem.visualization.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PermissionSystem extends JavaPlugin {

    private FileManager fileManager;
    private DatabaseHandler databaseHandler;
    private MessageManager messageManager;
    private GroupManager groupManager;
    private ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        initialObjects();
        registerListener();
        registerCommands();
    }

    @Override
    public void onDisable() {
        if(this.databaseHandler != null)
            this.databaseHandler.disconnectDatabase();
    }

    private void initialObjects() {
        this.fileManager = new FileManager(this);
        this.databaseHandler = new DatabaseHandler(this);
        this.messageManager = new MessageManager(this, Language.getLanguageBySynonym(String.valueOf(this.fileManager.getConfig(FileConfigEntry.LANGUAGE))));
        this.groupManager = new GroupManager(this);
        this.scoreboardManager = new ScoreboardManager(this);
    }

    private void registerListener() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerConnectListener(this), this);
        pluginManager.registerEvents(new PlayerChatListener(this), this);
        pluginManager.registerEvents(new SignChangeListener(this), this);
        pluginManager.registerEvents(scoreboardManager, this);
    }

    private void registerCommands() {
        getCommand("permission").setExecutor(new PermissionCommand(this));
    }

    public String getPluginName() {
        return "[PermissionSystem]";
    }

    public String getPluginPrefix() {
        return "§3•§b● §bPermSys §8§l┃§r ";
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }

}
