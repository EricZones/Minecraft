// Created by Eric B. 02.12.2022 09:53
package de.ericzones.permissionsystem.global.file;

import de.ericzones.permissionsystem.PermissionSystem;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileConfig {

    private final FileConfiguration fileConfiguration;
    private final File file;

    public FileConfig(PermissionSystem instance, String fileName) {
        file = new File(instance.getDataFolder(), fileName);
        if(!file.exists()) {
            instance.getDataFolder().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        fileConfiguration = new YamlConfiguration();
        try {
            fileConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException ex) {
            ex.printStackTrace();
        }
    }

    private void saveFile() {
        try {
            fileConfiguration.save(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void setEntry(String entry, Object value) {
        fileConfiguration.set(entry, value);
        saveFile();
    }

    public Object getValue(String entry) {
        return fileConfiguration.get(entry);
    }

    public Map<String, Object> getContent() {
        Map<String, Object> content = new HashMap();
        for(String current : fileConfiguration.getKeys(true)) {
            content.put(current, getValue(current));
        }
        return content;
    }

}
