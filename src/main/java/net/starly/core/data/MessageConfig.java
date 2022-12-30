package net.starly.core.data;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @deprecated Use {@link MessageData} instead.
 */
@Deprecated
public class MessageConfig {
    private Config config;
    private String prefix;
    private final String prefixPath;

    /**
     * @deprecated Use {@link MessageData} instead.
     */
    @Deprecated
    public MessageConfig(Config config) {
        this.config = config;
        this.prefix = "";
        this.prefixPath = null;
    }

    /**
     * @deprecated Use {@link MessageData} instead.
     */
    @Deprecated
    public MessageConfig(Config config, String prefixPath) {
        this.config = config;
        this.prefix = config.getString(prefixPath);
        this.prefixPath = prefixPath;
    }

    /**
     * @deprecated Use {@link MessageData} instead.
     */
    @Deprecated
    public String color(String msg) {
        return color(msg, '&');
    }

    /**
     * @deprecated Use {@link MessageData} instead.
     */
    @Deprecated
    public String color(String msg, char altChar) {
        return ChatColor.translateAlternateColorCodes(altChar, prefix + msg);
    }

    /**
     * @deprecated Use {@link MessageData} instead.
     */
    @Deprecated
    public String replace(String message, Map<String, String> map) {
        Map<String, String> newMap = new HashMap<>(map);
        for (Map.Entry<String, String> entry : newMap.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }

        return message;
    }

    /**
     * @deprecated Use {@link MessageData} instead.
     */
    @Deprecated
    public String getMessage(String path) {
        return color(config.getString(path));
    }

    /**
     * @deprecated Use {@link MessageData} instead.
     */
    @Deprecated
    public String getMessage(String path, Map<String, String> replacements) {
        return color(replace(config.getString(path), replacements));
    }

    /**
     * @deprecated Use {@link MessageData} instead.
     */
    @Deprecated
    public List<String> getMessages(String path) {
        List<String> list = new ArrayList<>();
        for (String msg : config.getStringList(path)) {
            list.add(color(msg));
        }

        return list;
    }

    /**
     * @deprecated Use {@link MessageData} instead.
     */
    @Deprecated
    public List<String> getMessages(String path, Map<String, String> replacements) {
        List<String> messages = new ArrayList<>();
        for (String message : config.getStringList(path)) {
            messages.add(color(replace(message, replacements)));
        }

        return messages;
    }

    /**
     * @deprecated Use {@link MessageData} instead.
     */
    @Deprecated
    public Config getConfig() {
        return config;
    }

    /**
     * @deprecated Use {@link MessageData} instead.
     */
    @Deprecated
    public void setConfig(Config config) {
        this.config = config;
        reloadConfig();
    }

    /**
     * @deprecated Use {@link MessageData} instead.
     */
    @Deprecated
    public String getPrefix() {
        return prefix;
    }

    /**
     * @deprecated Use {@link MessageData} instead.
     */
    @Deprecated
    public void reloadConfig() {
        config.reloadConfig();

        if (prefixPath != null) {
            String newPrefix = config.getString(prefixPath);
            prefix = newPrefix != null ? newPrefix : "";
        }
    }
}
