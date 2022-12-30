package net.starly.core.data;

import org.bukkit.ChatColor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageData {
    private final Object clazz;
    private final Field field;
    private final String prefixPath;

    public MessageData(Object clazz, String variableName) {
        Field field_t = null;
        try {
            field_t = clazz.getClass().getField(variableName);
            field_t.setAccessible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (field_t == null) throw new NullPointerException("Field(" + variableName + ") is null");
        else if (field_t.getType() != Config.class)
            throw new IllegalArgumentException("변수 " + variableName + " 는 Config 객체가 아닙니다.");

        this.clazz = clazz;
        this.field = field_t;
        this.prefixPath = null;
    }

    public MessageData(Object clazz, String variableName, String prefixPath) {
        Field field_t = null;
        try {
            field_t = clazz.getClass().getField(variableName);
            field_t.setAccessible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (field_t == null) throw new NullPointerException("Field(" + variableName + ") is null");
        else if (field_t.getType() != Config.class)
            throw new IllegalArgumentException("변수 " + variableName + " 는 Config 객체가 아닙니다.");

        this.clazz = clazz;
        this.field = field_t;
        this.prefixPath = prefixPath;
    }

    public String color(String msg) {
        return color(msg, '&');
    }

    public String color(String msg, char altChar) {
        return ChatColor.translateAlternateColorCodes(altChar, getPrefix() + msg);
    }

    public String replace(String message, Map<String, String> map) {
        Map<String, String> newMap = new HashMap<>(map);
        for (Map.Entry<String, String> entry : newMap.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }

        return message;
    }

    public String getMessage(String path) {
        return color(getConfig().getString(path));
    }

    public String getMessage(String path, Map<String, String> replacements) {
        return color(replace(getConfig().getString(path), replacements));
    }

    public List<String> getMessages(String path) {
        List<String> list = new ArrayList<>();
        for (String msg : getConfig().getStringList(path)) {
            list.add(color(msg));
        }

        return list;
    }

    public List<String> getMessages(String path, Map<String, String> replacements) {
        List<String> messages = new ArrayList<>();
        for (String message : getConfig().getStringList(path)) {
            messages.add(color(replace(message, replacements)));
        }

        return messages;
    }

    public String getPrefix() {
        return prefixPath == null ? "" : getConfig().getString(prefixPath);
    }

    public Config getConfig() {
        try {
            return (Config) field.get(clazz);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
