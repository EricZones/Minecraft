// Created by Eric B. 29.11.2022 19:36
package de.ericzones.permissionsystem.global.database;

import de.ericzones.permissionsystem.PermissionSystem;
import de.ericzones.permissionsystem.global.file.FileConfigEntry;
import org.bukkit.Bukkit;

public class DatabaseHandler {

    private final SqlAdapter sqlAdapter;

    public DatabaseHandler(PermissionSystem instance) {
        this.sqlAdapter = new SqlAdapter(new SqlConnection(instance, String.valueOf(instance.getFileManager().getConfig(FileConfigEntry.HOST)), (int)instance.getFileManager().getConfig(FileConfigEntry.PORT),
                String.valueOf(instance.getFileManager().getConfig(FileConfigEntry.DATABASE)), String.valueOf(instance.getFileManager().getConfig(FileConfigEntry.USERNAME)),
                String.valueOf(instance.getFileManager().getConfig(FileConfigEntry.PASSWORD))));
        this.sqlAdapter.getConnection().connect();
        if(!this.sqlAdapter.getConnection().isConnected()) {
            Bukkit.getConsoleSender().sendMessage(instance.getPluginName()+" Could not connect to "+instance.getFileManager().getConfig(FileConfigEntry.DATABASE)+" via SQL");
            Bukkit.getPluginManager().disablePlugin(instance);
        }
    }

    public void disconnectDatabase() {
        this.sqlAdapter.getConnection().disconnect();
    }

    public SqlAdapter getSqlAdapter() {
        return sqlAdapter;
    }

}
