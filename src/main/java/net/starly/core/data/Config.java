package net.starly.core.data;

import net.starly.core.data.impl.DefaultConfigImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
import java.util.List;
import java.util.Map;

public class Config implements DefaultConfigImpl {
    private final JavaPlugin plugin;
    private final String name;
    private File file;
    private FileConfiguration config = new YamlConfiguration();

    public Config(String name) {
        this.plugin = JavaPlugin.getProvidingPlugin(getClass());
        this.name = name + ".yml";
        this.file = null;
    }

    public void loadDefaultConfig() {
        if (file == null) {
            file = new File(plugin.getDataFolder(), name);
            if (!file.exists()) {
                if (!plugin.getDataFolder().exists()) {
                    plugin.getDataFolder().mkdir();
                }

                try {
                    file.createNewFile();
                    config.load(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void loadDefaultPluginConfig() {
        if (file == null) {
            plugin.saveResource(name, false);
            file = new File(plugin.getDataFolder(), name);

            try {
                config.load(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public FileConfiguration getConfig() {
        if (file == null) loadDefaultConfig();

        return config;
    }

    public void saveConfig() {
        if (file == null) loadDefaultConfig();

        try {
            getConfig().save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isFileExist() {
        return file == null ? false : file.exists();
    }

    public void remove() {
        if (file != null) {
            file.delete();
            file = null;
            config = null;
        }
    }

    public void reloadConfig() {
        saveConfig();

        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        ConfigurationSection section = getConfig().createSection(path);
        section.set("size", inventory.getSize());
        section.set("title", title);

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack != null) setItemStack(path + ".items." + i, itemStack);
        }

        saveConfig();
    }

    public Inventory getInventory(String path) {
        ConfigurationSection section = getConfig().createSection(path);
        Inventory inventory;
        try {
            inventory = Bukkit.createInventory(null, section.getInt("size"), section.getString("title"));
        } catch (Exception e) {
            Bukkit.getLogger().warning("인벤토리를 불러오는데 실패했습니다. 경로: " + path);
            return null;
        }

        try {
            section.getConfigurationSection("items").getKeys(false).forEach(key ->
                    inventory.setItem(Integer.parseInt(key), getItemStack(path + ".items." + key)));
        } catch (Exception e) {
            Bukkit.getLogger().warning("인벤토리를 불러오는데 실패했습니다. 경로: " + path + ".items");
            return null;
        }

        return inventory;
    }

    public void setLocation(String path, Location location) {
        ConfigurationSection section = getConfig().createSection(path);

        section.set("world", location.getWorld().getName());
        section.set("x", location.getX());
        section.set("y", location.getY());
        section.set("z", location.getZ());
        section.set("yaw", location.getYaw());
        section.set("pitch", location.getPitch());
        saveConfig();
    }

    public Location getLocation(String path) {
        return new Location(
                Bukkit.getWorld(getConfig().getString(path + ".world")),
                getConfig().getDouble(path + ".x"),
                getConfig().getDouble(path + ".y"),
                getConfig().getDouble(path + ".z"),
                (float) getConfig().getDouble(path + ".yaw"),
                (float) getConfig().getDouble(path + ".pitch")
        );
    }
}
