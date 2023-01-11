package net.starly.core.data;

import net.starly.core.builder.ItemBuilder;
import net.starly.core.data.impl.DefaultConfigImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
import java.util.*;

public class Config implements DefaultConfigImpl {
    private final JavaPlugin plugin;

    private FileConfiguration config = new YamlConfiguration();
    private File file;

    private final String name;
    private String prefixPath;
    private boolean isLoaded = false;

    private final char ALT_COLOR_CHAR = '&';
    private final char COLOR_CHAR = '§';

    /**
     * Config 오브젝트를 생성합니다.
     *
     * @param name   파일 이름
     * @param plugin 플러그인 인스턴스
     */
    public Config(String name, JavaPlugin plugin) {
        this.plugin = plugin;
        this.name = name + ".yml";
        this.prefixPath = null;
        loadFile();
    }

    /**
     * Config 오브젝트를 생성합니다.
     *
     * @param name       파일 이름
     * @param plugin     플러그인 인스턴스
     * @param prefixPath 접두사 경로
     */
    public Config(String name, JavaPlugin plugin, String prefixPath) {
        this.plugin = plugin;
        this.name = name + ".yml";
        this.prefixPath = prefixPath;
        loadFile();
    }

    /**
     * File을 로드합니다. (생성시 자동 호출)
     */
    public void loadFile() {
        file = new File(plugin.getDataFolder(), name);
    }

    /**
     * FileConfiguration을 로드합니다.
     */
    public void loadDefaultConfig() {
        if (!isFileExist()) {
            InputStream is = plugin.getResource(name);
            if (is != null) {
                plugin.saveResource(name, false);
            } else {
                try {
                    file.createNewFile();
                } catch (Exception ignored) {
                }
            }
        }

        try {
            config.load(file);
        } catch (Exception ignored) {
        }

        isLoaded = true;
    }

    /**
     * @deprecated {@link Config#loadDefaultConfig} 사용을 권장합니다.
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

    /**
     * 폴더 파일들의 이름 목록을 반환합니다.
     *
     * @return  List<String>        이름 목록
     */
    public List<String> getFileNames() {
        return getFiles().stream().map(file -> file.getName().replace(".yml", "")).toList();
    }

    /**
     * 폴더의 파일 목록을 반환합니다.
     *
     * @return  List<File>          파일 목록
     */
    public List<File> getFiles() {
        return Arrays.stream(file.listFiles()).toList();
    }

    /**
     * 콘피그를 반환합니다.
     *
     * @return FileConfiguration   콘피그
     */
    public FileConfiguration getConfig() {
        if (!isLoaded) loadDefaultConfig();

        return config;
    }

    /**
     * 콘피그를 저장합니다.
     */
    public void saveConfig() {
        try {
            getConfig().save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 파일이 존재하는지 반환합니다. <br>
     * ※ {@link Config#loadDefaultConfig}를 호출한 이후에는 파일이 자동생성되어 항상 true를 반환합니다.
     *
     * @return Boolean     존재 여부
     */
    public boolean isFileExist() {
        return file.exists();
    }

    /**
     * 파일을 삭제합니다.
     *
     * @deprecated {@link Config#delete} 사용을 권장합니다.
     */
    @Deprecated
    public void remove() {
        delete();
    }

    /**
     * 파일을 삭제합니다.
     */
    public void delete() {
        file.delete();
        file = null;
        config = null;
    }

    /**
     * 콘피그를 다시 불러옵니다.
     */
    public void reloadConfig() {
        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 접두사를 설정합니다.
     *
     * @param prefixPath 접두사 경로
     */
    public void setPrefix(String prefixPath) {
        this.prefixPath = prefixPath;
    }

    /**
     * 섹션을 생성합니다.
     *
     * @param path 경로
     */
    public ConfigurationSection createSection(String path) {
        return getConfig().createSection(path);
    }

    /**
     * 섹션을 반환합니다. (ConfigSection)
     *
     * @param path 경로
     * @return ConfigSection   섹션
     */
    public ConfigSection getSection(String path) {
        return new ConfigSection(this, path);
    }

    /**
     * 섹션을 반환합니다. (ConfigurationSection)
     *
     * @param path 경로
     * @return ConfigurationSection    섹션
     */
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

            if (enchantments != null) {
                enchantments.keySet().forEach(enchantment -> section.set("enchantments." + enchantment.getName(), enchantments.get(enchantment)));
            }
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
        ConfigurationSection section = getConfig().getConfigurationSection(path);
        ItemBuilder itemBuilder;


        // ----------------------------------------------------


        try {
            itemBuilder = new ItemBuilder(Material.valueOf(section.getString("material")));
        } catch (Exception e) {
            throw new IllegalArgumentException("아이템을 불러오는데 실패했습니다. 경로: " + path + ".material");
        }

        try {
            itemBuilder.setAmount(section.getInt("amount"));
        } catch (Exception e) {
            throw new IllegalArgumentException("아이템을 불러오는데 실패했습니다. 경로: " + path + ".amount");
        }

        if (section.get("durability") != null) {
            try {
                itemBuilder.setDurability((short) section.getInt("durability"));
            } catch (Exception e) {
                throw new IllegalArgumentException("아이템을 불러오는데 실패했습니다. 경로: " + path + ".durability");
            }
        }


        // ----------------------------------------------------


        if (section.getConfigurationSection("pdc") != null) {
            try {

                section.getConfigurationSection("pdc").getKeys(false).forEach(key -> {
                    try {
                        itemBuilder.setNBT(key, section.getString("pdc." + key));
                    } catch (Exception ex) {
                        throw new IllegalArgumentException("아이템을 불러오는데 실패했습니다. 경로: " + path + ".pdc." + key);
                    }
                });
            } catch (Exception e) {
                throw new IllegalArgumentException("아이템을 불러오는데 실패했습니다. 경로: " + path + ".pdc");
            }
        }

        if (section.get("meta.displayName") != null) {
            try {
                itemBuilder.setDisplayName(section.getString("meta.displayName"));
            } catch (Exception e) {
                throw new IllegalArgumentException("아이템을 불러오는데 실패했습니다. 경로: " + path + ".meta.displayName");
            }
        }

        if (section.get("meta.lores") != null) {
            try {
                itemBuilder.setLore(section.getStringList("meta.lores"));
            } catch (Exception e) {
                throw new IllegalArgumentException("아이템을 불러오는데 실패했습니다. 경로: " + path + ".meta.lores");
            }
        }

        if (section.get("meta.customModelData") != null) {
            try {
                itemBuilder.setCustomModelData(section.getInt("meta.customModelData"));
            } catch (Exception e) {
                throw new IllegalArgumentException("아이템을 불러오는데 실패했습니다. 경로: " + path + ".meta.customModelData");
            }
        }

        if (section.getConfigurationSection("meta.enchantments") != null) {
            try {
                section.getConfigurationSection("meta.enchantments").getKeys(false).forEach(key -> {
                    try {
                        itemBuilder.addUnsafeEnchantment(Enchantment.getByName(key), section.getInt("meta.enchantments." + key));
                    } catch (Exception ex) {
                        throw new IllegalArgumentException("아이템을 불러오는데 실패했습니다. 경로: " + path + ".meta.enchantments." + key);
                    }
                });
            } catch (Exception e) {
                throw new IllegalArgumentException("아이템을 불러오는데 실패했습니다. 경로: " + path + ".meta.enchantments");
            }
        }


        // ----------------------------------------------------


        return itemBuilder.build();
    }

    public void setInventory(String path, Inventory inventory, String title) {
        createSection(path);
        ConfigurationSection section = getConfig().getConfigurationSection(path);
        section.set("size", inventory.getSize());
        section.set("title", title);

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack != null) setItemStack(path + ".items." + i, itemStack);
        }

        saveConfig();
    }

    public Inventory getInventory(String path) {
        ConfigurationSection section = getConfig().getConfigurationSection(path);
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
