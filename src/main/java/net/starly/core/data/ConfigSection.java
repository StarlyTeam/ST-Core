package net.starly.core.data;

import net.starly.core.util.PreCondition;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated Use {@link ConfigurationSection} instead.
 */
@Deprecated
public class ConfigSection implements DefaultConfig {
    private final Config config;
    private final ConfigurationSection section;
    private final String path;


    /**
     * ConfigSection 오브젝트를 생성합니다.
     *
     * @param       fileName    파일 이름
     * @param       path        섹션 경로
     * @param       plugin      플러그인 인스턴스
     */
    public ConfigSection(String fileName, String path, JavaPlugin plugin) {
        PreCondition.nonNull(fileName, "fileName은 null일 수 없습니다.");
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        PreCondition.nonNull(plugin, "plugin은 null일 수 없습니다.");

        Config config = new Config(fileName, plugin);
        config.loadDefaultConfig();

        this.config = config;
        this.path = path;
        this.section = config.getConfigurationSection(path);
    }

    /**
     * ConfigSection 오브젝트를 생성합니다.
     *
     * @param       config      Config 오브젝트
     * @param       path        섹션 경로
     */
    public ConfigSection(Config config, String path) {
        PreCondition.nonNull(config, "config는 null일 수 없습니다.");
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        this.config = config;
        this.section = config.getConfigurationSection(path);
        this.path = path;
    }


    public Config getConfig() {
        return config;
    }

    public ConfigurationSection getConfigurationSection() {
        return section;
    }

    public String getPath() {
        return path;
    }


    public void saveConfig() {
        config.saveConfig();
    }

    public void reloadConfig() {
        config.reloadConfig();
    }


    public void createSection(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        getConfig().createSection(path);
    }

    public ConfigSection getSection(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        return new ConfigSection(config, path);
    }

    public List<String> getKeys() {
        return new ArrayList<>(section.getKeys(false));
    }

    public ConfigurationSection getConfigurationSection(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        return getConfig().getConfigurationSection(path);
    }

    @Override
    public void setString(String path, String value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        getConfig().setString(this.path + "." + path, value);
        saveConfig();
    }

    @Override
    public String getString(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        return getConfig().getString(this.path + "." + path);
    }

    @Override
    public void setBoolean(String path, boolean value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        getConfig().setBoolean(this.path + "." + path, value);
        saveConfig();
    }

    @Override
    public boolean getBoolean(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        return getConfig().getBoolean(this.path + "." + path);
    }

    @Override
    public void setChar(String path, char value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        getConfig().setChar(this.path + "." + path, value);
        saveConfig();
    }

    @Override
    public char getChar(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        return getConfig().getString(path).charAt(0);
    }

    @Override
    public void setByte(String path, byte value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        getConfig().setByte(this.path + "." + path, value);
        saveConfig();
    }

    @Override
    public byte getByte(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        return (byte) getConfig().getInt(this.path + "." + path);
    }

    @Override
    public void setShort(String path, short value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        getConfig().setShort(this.path + "." + path, value);
        saveConfig();
    }

    @Override
    public short getShort(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        return (short) getConfig().getInt(this.path + "." + path);
    }

    @Override
    public void setInt(String path, int value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        getConfig().setInt(this.path + "." + path, value);
        saveConfig();
    }

    @Override
    public int getInt(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        return getConfig().getInt(this.path + "." + path);
    }

    @Override
    public void setLong(String path, long value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        getConfig().setLong(this.path + "." + path, value);
        saveConfig();
    }

    @Override
    public long getLong(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        return getConfig().getLong(this.path + "." + path);
    }

    @Override
    public void setFloat(String path, float value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        getConfig().setFloat(this.path + "." + path, value);
        saveConfig();
    }

    @Override
    public float getFloat(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        return (float) getConfig().getDouble(this.path + "." + path);
    }

    @Override
    public void setDouble(String path, double value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        getConfig().setDouble(this.path + "." + path, value);
        saveConfig();
    }

    @Override
    public double getDouble(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        return getConfig().getDouble(this.path + "." + path);
    }

    @Override
    public void setObject(String path, Object value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        getConfig().setObject(this.path + "." + path, value);
        saveConfig();
    }

    @Override
    public Object getObject(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        return getConfig().getObject(this.path + "." + path);
    }

    @Override
    public void setObjectList(String path, List<Object> value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        getConfig().setObjectList(this.path + "." + path, value);
        saveConfig();
    }

    @Override
    public List<Object> getObjectList(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        return getConfig().getObjectList(this.path + "." + path);
    }

    @Override
    public void setStringList(String path, List<String> value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        getConfig().setStringList(this.path + "." + path, value);
        saveConfig();
    }

    @Override
    public List<String> getStringList(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        return getConfig().getStringList(this.path + "." + path);
    }

    public void setItemStack(String path, ItemStack value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        getConfig().setItemStack(this.path + "." + path, value);
        saveConfig();
    }

    public ItemStack getItemStack(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        return getConfig().getItemStack(this.path + "." + path);
    }

    public void setInventory(String path, Inventory value, String title) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        PreCondition.nonNull(title, "title은 null일 수 없습니다.");

        getConfig().setInventory(this.path + "." + path, value, title);
        saveConfig();
    }

    public Inventory getInventory(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        return getConfig().getInventory(this.path + "." + path);
    }

    public void setLocation(String path, Location value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        getConfig().setLocation(this.path + "." + path, value);
        saveConfig();
    }

    public Location getLocation(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        return getConfig().getLocation(this.path + "." + path);
    }
}
