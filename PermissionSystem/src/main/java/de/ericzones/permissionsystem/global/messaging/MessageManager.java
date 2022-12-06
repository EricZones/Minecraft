// Created by Eric B. 02.12.2022 21:10
package de.ericzones.permissionsystem.global.messaging;

import de.ericzones.permissionsystem.PermissionSystem;
import de.ericzones.permissionsystem.global.file.FileManager;

import java.util.Map;

public class MessageManager {

    private final PermissionSystem instance;
    private Language language;
    private Map<FileMessageEntry, String> messages;

    public MessageManager(PermissionSystem instance, Language language) {
        this.instance = instance;
        this.language = language;
        this.messages = instance.getFileManager().getMessages();
    }

    // Replacing every message of message file to specific language defaults
    public void setLanguage(Language language) {
        FileManager fileManager = instance.getFileManager();
        for(FileMessageEntry current : FileMessageEntry.values())
            fileManager.setMessage(current, current.getTranslation(language));
        fileManager.setLanguageConfig(language);
        this.language = language;
        this.messages = fileManager.getMessages();
    }

    public String getMessage(FileMessageEntry fileMessageEntry) {
        return messages.get(fileMessageEntry);
    }

    public Language getLanguage() {
        return language;
    }

}
