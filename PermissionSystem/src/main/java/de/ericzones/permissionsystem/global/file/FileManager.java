// Created by Eric B. 02.12.2022 09:59
package de.ericzones.permissionsystem.global.file;

import de.ericzones.permissionsystem.PermissionSystem;
import de.ericzones.permissionsystem.global.messaging.FileMessageEntry;
import de.ericzones.permissionsystem.global.messaging.Language;

import java.util.HashMap;
import java.util.Map;

public class FileManager {

    private final PermissionSystem instance;
    private final FileConfig config, messages;

    public FileManager(PermissionSystem instance) {
        this.instance = instance;
        config = new FileConfig(instance, "config.yml");
        messages = new FileConfig(instance, "messages.yml");
        checkEntries();
    }

    // creating files and adding missing entries
    private void checkEntries() {
        for(FileConfigEntry current : FileConfigEntry.values()) {
            if(config.getValue(current.getName()) == null)
                config.setEntry(current.getName(), current.getPreset());
        }
        Language language = Language.getLanguageBySynonym(String.valueOf(config.getValue(FileConfigEntry.LANGUAGE.getName())));
        if(language == null) {
            System.out.println(instance.getPluginName()+" The language configuration is not valid");
            config.setEntry(FileConfigEntry.LANGUAGE.getName(), FileConfigEntry.LANGUAGE.getPreset());
            language = Language.getLanguageBySynonym((String) FileConfigEntry.LANGUAGE.getPreset());
        }
        for(FileMessageEntry current : FileMessageEntry.values()) {
            if(messages.getValue(current.getName()) == null) {
                if(language == null)
                    messages.setEntry(current.getName(), current.getTranslations()[0]);
                else
                    messages.setEntry(current.getName(), current.getTranslation(language));
            }
        }
    }

    // Get specific entry of configuration file
    public Object getConfig(FileConfigEntry fileConfigEntry) {
        if(fileConfigEntry == FileConfigEntry.PORT) {
            try {
                int port = (int) config.getValue(fileConfigEntry.getName());
            } catch (NumberFormatException e) {
                System.out.println(instance.getPluginName()+" The port configuration is not valid");
                config.setEntry(fileConfigEntry.getName(), fileConfigEntry.getPreset());
            }
        }
        return config.getValue(fileConfigEntry.getName());
    }

    // Get every entry of message file
    public Map<FileMessageEntry, String> getMessages() {
        Map<FileMessageEntry, String> messageEntries = new HashMap<>();
        for(FileMessageEntry current : FileMessageEntry.values()) {
            if(messages.getValue(current.getName()) == null) {
                Language language = instance.getMessageManager().getLanguage();
                setMessage(current, current.getTranslation(language));
                messageEntries.put(current, current.getTranslation(language));
            } else
                messageEntries.put(current, String.valueOf(messages.getValue(current.getName())));
        }
        return messageEntries;
    }

    public void setMessage(FileMessageEntry fileMessageEntry, String message) {
        messages.setEntry(fileMessageEntry.getName(), message);
    }

    public void setLanguageConfig(Language language) {
        config.setEntry(FileConfigEntry.LANGUAGE.getName(), language.getSynonym());
    }

}
