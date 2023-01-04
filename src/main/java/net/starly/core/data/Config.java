package net.starly.core.data;

import net.starly.core.data.impl.DefaultConfigImpl;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config implements DefaultConfigImpl {
    private final JavaPlugin plugin;

    private FileConfiguration config = new YamlConfiguration();
    private File file;

    private final String name;
    private String prefixPath;
    private boolean isLoaded = false;

    private final char ALT_COLOR_CHAR = '&';
    private final char COLOR_CHAR = '§';

    public Config(String name, JavaPlugin plugin) {
        this.plugin = plugin;
        this.name = name + ".yml";
        this.prefixPath = null;
        loadFile();
    }

    public Config(String name, String prefixPath, JavaPlugin plugin) {
        this.plugin = plugin;
        this.name = name + ".yml";
        this.prefixPath = prefixPath;
        loadFile();
    }

    public void loadFile() {
        file = new File(plugin.getDataFolder(), name);
    }

    public void loadDefaultConfig() {
        if (!isFileExist()) {
            InputStream is = plugin.getResource(name);
            if (is != null) {
                plugin.saveResource(name, false);
            } else {
                try {
                    file.createNewFile();
                } catch (Exception ignored) {}
            }
        }

        try {
            config.load(file);
        } catch (Exception ignored) {}

        isLoaded = true;
    }

    /**
     * @deprecated Use {@link Config#loadDefaultConfig} instead.
     */
    @Deprecated
    public void loadDefaultPluginConfig() {
        if (!isFileExist()) {
            plugin.saveResource(name, false);
        }

        try {
            config.load(file);
        } catch (Exception ignored) {
        }

        isLoaded = true;
    }

    public FileConfiguration getConfig() {
        if (!isLoaded) loadDefaultConfig();

        return config;
    }

    public void saveConfig() {
        try {
            getConfig().save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isFileExist() {
        return file.exists();
    }

    @Deprecated
    public void remove() {
        delete();
    }

    public void delete() {
        file.delete();
        file = null;
        config = null;
    }

    public void reloadConfig() {
        saveConfig();

        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPrefix(String prefixPath) {
        this.prefixPath = prefixPath;
    }

    public void createSection(String path) {
        getConfig().createSection(path);
    }

    public ConfigSection getSection(String path) {
        return new ConfigSection(this, path);
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return getConfig().getConfigurationSection(path);
    }

    @Override
    public void setString(String path, String value) {
        getConfig().set(path, value);
        saveConfig();
    }

    @Override
    public String getString(String path) {
        return getConfig().getString(path);
    }

    @Override
    public void setBoolean(String path, boolean value) {
        getConfig().set(path, value);
        saveConfig();
    }

    @Override
    public boolean getBoolean(String path) {
        return getConfig().getBoolean(path);
    }

    @Override
    public void setChar(String path, char value) {
        getConfig().set(path, value);
        saveConfig();
    }

    @Override
    public char getChar(String path) {
        return getConfig().getString(path).charAt(0);
    }

    @Override
    public void setByte(String path, byte value) {
        getConfig().set(path, value);
        saveConfig();
    }

    @Override
    public byte getByte(String path) {
        return (byte) getConfig().getInt(path);
    }

    @Override
    public void setShort(String path, short value) {
        getConfig().set(path, value);
        saveConfig();
    }

    @Override
    public short getShort(String path) {
        return (short) getConfig().getInt(path);
    }

    @Override
    public void setInt(String path, int value) {
        getConfig().set(path, value);
        saveConfig();
    }

    @Override
    public int getInt(String path) {
        return getConfig().getInt(path);
    }

    @Override
    public void setLong(String path, long value) {
        getConfig().set(path, value);
        saveConfig();
    }

    @Override
    public long getLong(String path) {
        return getConfig().getLong(path);
    }

    @Override
    public void setFloat(String path, float value) {
        getConfig().set(path, value);
        saveConfig();
    }

    @Override
    public float getFloat(String path) {
        return (float) getConfig().getDouble(path);
    }

    @Override
    public void setDouble(String path, double value) {
        getConfig().set(path, value);
        saveConfig();
    }

    @Override
    public double getDouble(String path) {
        return getConfig().getDouble(path);
    }

    @Override
    public void setObject(String path, Object value) {
        getConfig().set(path, value);
        saveConfig();
    }

    @Override
    public Object getObject(String path) {
        return getConfig().get(path);
    }

    @Override
    public void setObjectList(String path, List<Object> value) {
        getConfig().set(path, value);
        saveConfig();
    }

    @Override
    public List<Object> getObjectList(String path) {
        return (List<Object>) getConfig().getList(path);
    }

    @Override
    public void setStringList(String path, List<String> value) {
        getConfig().set(path, value);
        saveConfig();
    }

    @Override
    public List<String> getStringList(String path) {
        return getConfig().getStringList(path);
    }

    public void setItemStack(String path, ItemStack value) {
        /*
         * Slot
         * Amount
         * Type
         * Durability
         * MetaData
         */

        ConfigurationSection section = getConfig().createSection(path);

        section.set("material", value.getType().name());
        section.set("amount", value.getAmount());
        section.set("durability", value.getDurability());

        ItemMeta meta = value.getItemMeta();
        if (meta != null) {
            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.getKeys().forEach(key -> section.set("pdc." + key.getKey(), data.get(key, PersistentDataType.LONG)));
        }

        if (value.getType() == Material.ENCHANTED_BOOK) { //인챈트 북
            EnchantmentStorageMeta esm = (EnchantmentStorageMeta) value.getItemMeta();
            Map<Enchantment, Integer> enchantments = esm.getStoredEnchants();

            if (enchantments != null)
                enchantments.keySet().forEach(enchantment -> section.set("Enchant." + enchantment.getName(), enchantments.get(enchantment)));
        } else if (value.hasItemMeta()) { //일반 아이템
            ConfigurationSection metaSection = section.createSection("meta");

            if (meta.hasDisplayName()) metaSection.set("displayName", meta.getDisplayName());
            if (meta.hasLore()) metaSection.set("lores", meta.getLore());
            if (meta.hasCustomModelData()) metaSection.set("customModelData", meta.getCustomModelData());

            Map<Enchantment, Integer> enchantments = meta.getEnchants();
            value.getEnchantments().keySet().forEach(enchantment -> section.set("enchantments." + enchantment.getName(), enchantments.get(enchantment)));
        }

        saveConfig();
    }

    public ItemStack getItemStack(String path) {
        /*
         * Slot
         * Amount
         * Type
         * Durability
         * MetaData
         */

        ConfigurationSection section = getConfig().createSection(path);
        ItemStack itemStack;
        try {
            itemStack = new ItemStack(Material.valueOf(section.getString("material")));
        } catch (Exception e) {
            Bukkit.getLogger().warning("아이템을 불러오는데 실패했습니다. 경로: " + path + ".material");
            return null;
        }

        try {
            itemStack.setAmount(section.getInt("amount"));
        } catch (Exception e) {
            Bukkit.getLogger().warning("아이템을 불러오는데 실패했습니다. 경로: " + path + ".amount");
            return null;
        }

        try {
            itemStack.setDurability((short) section.getInt("durability"));
        } catch (Exception e) {
            Bukkit.getLogger().warning("아이템을 불러오는데 실패했습니다. 경로: " + path + ".durability");
            return null;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            PersistentDataContainer data = meta.getPersistentDataContainer();
            try {
                section.getConfigurationSection("pdc").getKeys(false).forEach(key ->
                        data.set(new NamespacedKey(plugin, key), PersistentDataType.LONG, section.getLong("pdc." + key)));
            } catch (Exception e) {
                Bukkit.getLogger().warning("아이템을 불러오는데 실패했습니다. 경로: " + path + ".pdc");
                return null;
            }

            try {
                meta.setDisplayName(section.getString("meta.displayName"));
            } catch (Exception e) {
                Bukkit.getLogger().warning("아이템을 불러오는데 실패했습니다. 경로: " + path + ".meta.displayName");
                return null;
            }

            try {
                meta.setLore(section.getStringList("meta.lores"));
            } catch (Exception e) {
                Bukkit.getLogger().warning("아이템을 불러오는데 실패했습니다. 경로: " + path + ".meta.lores");
                return null;
            }

            try {
                meta.setCustomModelData(section.getInt("meta.customModelData"));
            } catch (Exception e) {
                Bukkit.getLogger().warning("아이템을 불러오는데 실패했습니다. 경로: " + path + ".meta.customModelData");
                return null;
            }

            try {
                section.getConfigurationSection("meta.enchantments").getKeys(false).forEach(key ->
                        itemStack.addUnsafeEnchantment(Enchantment.getByName(key), section.getInt("meta.enchantments." + key)));
            } catch (Exception e) {
                Bukkit.getLogger().warning("아이템을 불러오는데 실패했습니다. 경로: " + path + ".meta.enchantments");
                return null;
            }

            itemStack.setItemMeta(meta);
        }

        return itemStack;
    }

    public void setInventory(String path, Inventory inventory, String title) {
        createSection(path);
        ConfigurationSection section = getConfigurationSection(path);
        section.set("size", inventory.getSize());
        section.set("title", title);

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack != null) setItemStack(path + ".items." + i, itemStack);
        }

        saveConfig();
    }

    public Inventory getInventory(String path) {
        ConfigurationSection section = getConfigurationSection(path);
        Inventory inventory;
        try {
            inventory = Bukkit.createInventory(null, section.getInt("size"), section.getString("title"));
        } catch (Exception e) {
            throw new IllegalArgumentException("인벤토리를 불러오는데 실패했습니다. 경로: " + path);
        }

        try {
            section.getConfigurationSection("items").getKeys(false).forEach(key ->
                    inventory.setItem(Integer.parseInt(key), getItemStack(path + ".items." + key)));
        } catch (Exception e) {
            throw new IllegalArgumentException("인벤토리를 불러오는데 실패했습니다. 경로: " + path + ".items");
        }

        return inventory;
    }

    public void setLocation(String path, Location location) {
        getConfig().createSection(path);
        ConfigurationSection section = getConfig().getConfigurationSection(path);

        section.set("world", location.getWorld().getName());
        section.set("x", location.getX());
        section.set("y", location.getY());
        section.set("z", location.getZ());
        section.set("yaw", location.getYaw());
        section.set("pitch", location.getPitch());
        saveConfig();
    }

    public Location getLocation(String path) {
        ConfigurationSection section = getConfig().getConfigurationSection(path);

        return new Location(
                Bukkit.getWorld(section.getString("world")),
                section.getDouble("x"),
                section.getDouble("y"),
                section.getDouble("z"),
                (float) section.getDouble("yaw"),
                (float) section.getDouble("pitch")
        );
    }






    private String getPrefix() {
        return prefixPath == null ? "" : getString(prefixPath);
    }

    private String color(String msg) {
        return (getPrefix() + msg).replace(ALT_COLOR_CHAR, COLOR_CHAR);
    }
    private String replace(String message, Map<String, String> map) {
        Map<String, String> newMap = new HashMap<>(map);
        for (Map.Entry<String, String> entry : newMap.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }

        return message;
    }

    public String getMessage(String path) {
        return color(config.getString(path));
    }
    public String getMessage(String path, Map<String, String> replacements) {
        return color(replace(config.getString(path), replacements));
    }

    public List<String> getMessages(String path) {
        List<String> list = new ArrayList<>();
        for (String msg : config.getStringList(path)) {
            list.add(color(msg));
        }

        return list;
    }
    public List<String> getMessages(String path, Map<String, String> replacements) {
        List<String> messages = new ArrayList<>();
        for (String message : config.getStringList(path)) {
            messages.add(color(replace(message, replacements)));
        }

        return messages;
    }
}
