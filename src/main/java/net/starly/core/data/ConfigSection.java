package net.starly.core.data;

import net.starly.core.data.impl.DefaultConfigImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

public class ConfigSection implements DefaultConfigImpl {
    private final Config config;
    private final ConfigurationSection section;
    private final String path;


    public ConfigSection(String fileName, String path, JavaPlugin plugin) {
        Config config = new Config(fileName, plugin);
        config.loadDefaultConfig();

        this.config = config;
        this.path = path;
        this.section = config.getConfigurationSection(path);
    }

    public ConfigSection(Config config, String path) {
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
        getConfig().createSection(path);
    }

    public ConfigSection getSection(String path) {
        return new ConfigSection(config, path);
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return getConfig().getConfigurationSection(path);
    }

    @Override
    public void setString(String path, String value) {
        getConfig().setString(this.path + "." + path, value);
        saveConfig();
    }

    @Override
    public String getString(String path) {
        return getConfig().getString(this.path + "." + path);
    }

    @Override
    public void setBoolean(String path, boolean value) {
        getConfig().setBoolean(this.path + "." + path, value);
        saveConfig();
    }

    @Override
    public boolean getBoolean(String path) {
        return getConfig().getBoolean(this.path + "." + path);
    }

    @Override
    public void setChar(String path, char value) {
        getConfig().setChar(this.path + "." + path, value);
        saveConfig();
    }

    @Override
    public char getChar(String path) {
        return getConfig().getString(path).charAt(0);
    }

    @Override
    public void setByte(String path, byte value) {
        getConfig().setByte(this.path + "." + path, value);
        saveConfig();
    }

    @Override
    public byte getByte(String path) {
        return (byte) getConfig().getInt(this.path + "." + path);
    }

    @Override
    public void setShort(String path, short value) {
        getConfig().setShort(this.path + "." + path, value);
        saveConfig();
    }

    @Override
    public short getShort(String path) {
        return (short) getConfig().getInt(this.path + "." + path);
    }

    @Override
    public void setInt(String path, int value) {
        getConfig().setInt(this.path + "." + path, value);
        saveConfig();
    }

    @Override
    public int getInt(String path) {
        return getConfig().getInt(this.path + "." + path);
    }

    @Override
    public void setLong(String path, long value) {
        getConfig().setLong(this.path + "." + path, value);
        saveConfig();
    }

    @Override
    public long getLong(String path) {
        return getConfig().getLong(this.path + "." + path);
    }

    @Override
    public void setFloat(String path, float value) {
        getConfig().setFloat(this.path + "." + path, value);
        saveConfig();
    }

    @Override
    public float getFloat(String path) {
        return (float) getConfig().getDouble(this.path + "." + path);
    }

    @Override
    public void setDouble(String path, double value) {
        getConfig().setDouble(this.path + "." + path, value);
        saveConfig();
    }

    @Override
    public double getDouble(String path) {
        return getConfig().getDouble(this.path + "." + path);
    }

    @Override
    public void setObject(String path, Object value) {
        getConfig().setObject(this.path + "." + path, value);
        saveConfig();
    }

    @Override
    public Object getObject(String path) {
        return getConfig().getObject(this.path + "." + path);
    }

    @Override
    public void setObjectList(String path, List<Object> value) {
        getConfig().setObjectList(this.path + "." + path, value);
        saveConfig();
    }

    @Override
    public List<Object> getObjectList(String path) {
        return getConfig().getObjectList(this.path + "." + path);
    }

    @Override
    public void setStringList(String path, List<String> value) {
        getConfig().setStringList(this.path + "." + path, value);
        saveConfig();
    }

    @Override
    public List<String> getStringList(String path) {
        return getConfig().getStringList(this.path + "." + path);
    }

    public void setItemStack(String path, ItemStack value) {
        getConfig().setItemStack(this.path + "." + path, value);
        saveConfig();
    }

    public ItemStack getItemStack(String path) {
        return getConfig().getItemStack(this.path + "." + path);
    }

    public void setInventory(String path, Inventory inventory, String title) {
        getConfig().setInventory(this.path + "." + path, inventory, title);
        saveConfig();
    }

    public Inventory getInventory(String path) {
        return getConfig().getInventory(this.path + "." + path);
    }

    public void setLocation(String path, Location location) {
        getConfig().setLocation(this.path + "." + path, location);
        saveConfig();
    }

    public Location getLocation(String path) {
        return getConfig().getLocation(this.path + "." + path);
    }
}
