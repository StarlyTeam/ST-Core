package net.starly.core.data;

import net.starly.core.StarlyCore;
import net.starly.core.builder.ItemBuilder;
import net.starly.core.data.impl.DefaultConfigImpl;
import net.starly.core.util.PreCondition;
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
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("all")
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
        PreCondition.nonNull(name, "name은 null일 수 없습니다.");
        PreCondition.nonNull(plugin, "plugin은 null일 수 없습니다.");

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
        PreCondition.nonNull(name, "name은 null일 수 없습니다.");
        PreCondition.nonNull(plugin, "plugin은 null일 수 없습니다.");
        PreCondition.nonNull(prefixPath, "prefixPath는 null일 수 없습니다.");

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
        return getFiles().stream().map(file -> file.getName().replace(".yml", "")).collect(Collectors.toList());
    }

    /**
     * 폴더의 파일 목록을 반환합니다.
     *
     * @return  List<File>          파일 목록
     */
    public List<File> getFiles() {
        File dir = new File(plugin.getDataFolder(), name.replace(".yml", ""));
        return Arrays.asList(dir.listFiles());
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
        PreCondition.nonNull(prefixPath, "prefixPath는 null일 수 없습니다.");

        this.prefixPath = prefixPath;
    }

    /**
     * 섹션을 생성합니다.
     *
     * @param path 경로
     */
    public ConfigurationSection createSection(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        return getConfig().createSection(path);
    }

    /**
     * 섹션을 반환합니다. (ConfigSection)
     *
     * @param path 경로
     * @return ConfigSection   섹션
     */
    public ConfigSection getSection(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        return new ConfigSection(this, path);
    }

    /**
     * 섹션을 반환합니다. (ConfigurationSection)
     *
     * @param path 경로
     * @return ConfigurationSection    섹션
     */
    public ConfigurationSection getConfigurationSection(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        return getConfig().getConfigurationSection(path);
    }

    @Override
    public void setString(String path, String value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        PreCondition.nonNull(value, "value는 null일 수 없습니다.");

        getConfig().set(path, value);
        saveConfig();
    }

    @Override
    public String getString(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        return getConfig().getString(path);
    }

    @Override
    public void setBoolean(String path, boolean value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        PreCondition.nonNull(value, "value는 null일 수 없습니다.");

        getConfig().set(path, value);
        saveConfig();
    }

    @Override
    public boolean getBoolean(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        return getConfig().getBoolean(path);
    }

    @Override
    public void setChar(String path, char value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        PreCondition.nonNull(value, "value는 null일 수 없습니다.");

        getConfig().set(path, value);
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
        PreCondition.nonNull(value, "value는 null일 수 없습니다.");

        getConfig().set(path, value);
        saveConfig();
    }

    @Override
    public byte getByte(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        return (byte) getConfig().getInt(path);
    }

    @Override
    public void setShort(String path, short value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        PreCondition.nonNull(value, "value는 null일 수 없습니다.");

        getConfig().set(path, value);
        saveConfig();
    }

    @Override
    public short getShort(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        return (short) getConfig().getInt(path);
    }

    @Override
    public void setInt(String path, int value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        PreCondition.nonNull(value, "value는 null일 수 없습니다.");

        getConfig().set(path, value);
        saveConfig();
    }

    @Override
    public int getInt(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        return getConfig().getInt(path);
    }

    @Override
    public void setLong(String path, long value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        PreCondition.nonNull(value, "value는 null일 수 없습니다.");

        getConfig().set(path, value);
        saveConfig();
    }

    @Override
    public long getLong(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        return getConfig().getLong(path);
    }

    @Override
    public void setFloat(String path, float value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        PreCondition.nonNull(value, "value는 null일 수 없습니다.");

        getConfig().set(path, value);
        saveConfig();
    }

    @Override
    public float getFloat(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        return (float) getConfig().getDouble(path);
    }

    @Override
    public void setDouble(String path, double value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        PreCondition.nonNull(value, "value는 null일 수 없습니다.");

        getConfig().set(path, value);
        saveConfig();
    }

    @Override
    public double getDouble(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        return getConfig().getDouble(path);
    }

    @Override
    public void setObject(String path, Object value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        PreCondition.nonNull(value, "value는 null일 수 없습니다.");

        getConfig().set(path, value);
        saveConfig();
    }

    @Override
    public Object getObject(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        return getConfig().get(path);
    }

    @Override
    public void setObjectList(String path, List<Object> value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        PreCondition.nonNull(value, "value는 null일 수 없습니다.");

        getConfig().set(path, value);
        saveConfig();
    }

    @Override
    public List<Object> getObjectList(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        return getConfig().getList(path).stream().map(o -> (Object) o).collect(Collectors.toList());
    }

    @Override
    public void setStringList(String path, List<String> value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        PreCondition.nonNull(value, "value는 null일 수 없습니다.");

        getConfig().set(path, value);
        saveConfig();
    }

    @Override
    public List<String> getStringList(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        return getConfig().getStringList(path);
    }

    public void setItemStack(String path, ItemStack value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        PreCondition.nonNull(value, "value는 null일 수 없습니다.");

        ConfigurationSection section = getConfig().createSection(path);


        // ----------------------------------------------------


        section.set("material", value.getType().name());
        section.set("amount", value.getAmount());
        section.set("durability", value.getDurability());


        // ----------------------------------------------------


        ItemMeta meta = value.getItemMeta();
        if (meta != null) {
            ConfigurationSection metaSection = section.createSection("meta");

            // ----------------------------------------------------


            if (meta.hasDisplayName()) metaSection.set("displayName", meta.getDisplayName());
            if (meta.hasLore()) metaSection.set("lores", meta.getLore());
            try {
                meta.getClass().getMethod("hasCustomModelData");
                if (meta.hasCustomModelData()) metaSection.set("customModelData", meta.getCustomModelData());
            } catch (NoSuchMethodException ignored) {}


            // ----------------------------------------------------


            try {
                meta.getClass().getMethod("getPersistentDataContainer");
                PersistentDataContainer data = meta.getPersistentDataContainer();
                data.getKeys().forEach(key -> section.set("pdc." + key.getKey(), data.get(key, PersistentDataType.STRING)));
            } catch (NoSuchMethodException ignored) {}
        }


        // ----------------------------------------------------


        if (!Bukkit.getVersion().contains("1.12")) {
            try {
                if (value.getType() == Material.PLAYER_HEAD) {
                    SkullMeta skullMeta = (SkullMeta) value.getItemMeta();

                    if (skullMeta != null) {
                        section.set("skullMeta", skullMeta);
                    }
                }
            } catch (Exception ignored) {}
        }


        // -----------------------------------------------------


        if (value.getType() == Material.ENCHANTED_BOOK) {           // 인챈트 북
            EnchantmentStorageMeta esm = (EnchantmentStorageMeta) value.getItemMeta();
            Map<Enchantment, Integer> enchantments = esm.getStoredEnchants();

            if (enchantments != null) {
                enchantments.keySet().forEach(enchantment -> section.set("enchantments." + enchantment.getName(), enchantments.get(enchantment)));
            }
        } else if (value.getItemMeta().hasEnchants()) {             // 이외 아이템
            Map<Enchantment, Integer> enchantments = meta.getEnchants();
            value.getEnchantments().keySet().forEach(enchantment -> section.set("enchantments." + enchantment.getName(), enchantments.get(enchantment)));
        }


        // ----------------------------------------------------


        saveConfig();
    }

    public ItemStack getItemStack(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");

        // ----------------------------------------------------


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


        if (section.get("meta.displayName") != null) {
            try {
                itemBuilder.setDisplayName(section.getString("meta.displayName"));
            } catch (Exception e) {
                throw new IllegalArgumentException("아이템을 불러오는데 실패했습니다. 경로: " + path + ".meta.displayName");
            }
        }


        // ----------------------------------------------------


        if (section.get("meta.lores") != null) {
            try {
                itemBuilder.setLore(section.getStringList("meta.lores"));
            } catch (Exception e) {
                throw new IllegalArgumentException("아이템을 불러오는데 실패했습니다. 경로: " + path + ".meta.lores");
            }
        }


        // ----------------------------------------------------


        if (section.get("meta.customModelData") != null) {
            try {
                itemBuilder.setCustomModelData(section.getInt("meta.customModelData"));
            } catch (Exception e) {
                throw new IllegalArgumentException("아이템을 불러오는데 실패했습니다. 경로: " + path + ".meta.customModelData");
            }
        }


        // ----------------------------------------------------


        ItemStack itemStack = itemBuilder.build();

        if (section.get("skullMeta") != null) {
            itemStack.setItemMeta((SkullMeta) section.get("skullMeta"));
        }

        if (section.getConfigurationSection("pdc") != null) {
            try {
                section.getConfigurationSection("pdc").getKeys(false).forEach(key -> {
                    try {
                        itemStack.getItemMeta().getPersistentDataContainer().set(new NamespacedKey(plugin, key), PersistentDataType.STRING, section.getString("pdc." + key));
                    } catch (Exception ex) {
                        throw new IllegalArgumentException("아이템을 불러오는데 실패했습니다. 경로: " + path + ".pdc." + key);
                    }
                });
            } catch (Exception e) {
                throw new IllegalArgumentException("아이템을 불러오는데 실패했습니다. 경로: " + path + ".pdc");
            }
        }

        if (section.getConfigurationSection("enchantments") != null) {
            try {
                section.getConfigurationSection("enchantments").getKeys(false).forEach(key -> {
                    try {
                        itemStack.addUnsafeEnchantment(Enchantment.getByName(key), section.getInt("enchantments." + key));
                    } catch (Exception ex) {
                        throw new IllegalArgumentException("아이템을 불러오는데 실패했습니다. 경로: " + path + ".enchantments." + key);
                    }
                });
            } catch (Exception e) {
                throw new IllegalArgumentException("아이템을 불러오는데 실패했습니다. 경로: " + path + ".enchantments");
            }
        }


        // ----------------------------------------------------


        return itemStack;
    }

    public void setInventory(String path, Inventory value, String title) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        PreCondition.nonNull(value, "value는 null일 수 없습니다.");
        PreCondition.nonNull(title, "title은 null일 수 없습니다.");

        createSection(path);
        ConfigurationSection section = getConfig().getConfigurationSection(path);
        section.set("size", value.getSize());
        section.set("title", title);

        for (int i = 0; i < value.getSize(); i++) {
            ItemStack itemStack = value.getItem(i);
            if (itemStack != null) setItemStack(path + ".items." + i, itemStack);
        }
        if (getConfigurationSection(path + ".items") == null) setObject(path + ".items", new HashMap<>());

        saveConfig();
    }

    public Inventory getInventory(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
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

    public void setLocation(String path, Location value) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        PreCondition.nonNull(value, "value는 null일 수 없습니다.");

        getConfig().createSection(path);
        ConfigurationSection section = getConfig().createSection(path);

        section.set("world", value.getWorld().getName());
        section.set("x", value.getX());
        section.set("y", value.getY());
        section.set("z", value.getZ());
        section.set("yaw", value.getYaw());
        section.set("pitch", value.getPitch());
        saveConfig();
    }

    public Location getLocation(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
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
        PreCondition.nonNull(msg, "msg는 null일 수 없습니다.");

        return (getPrefix() + msg).replace(ALT_COLOR_CHAR, COLOR_CHAR);
    }

    private String replace(String message, Map<String, String> map) {
        PreCondition.nonNull(message, "message는 null일 수 없습니다.");
        PreCondition.nonNull(map, "map은 null일 수 없습니다.");

        Map<String, String> newMap = new HashMap<>(map);
        for (Map.Entry<String, String> entry : newMap.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }

        return message;
    }

    public String getMessage(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        return color(config.getString(path));
    }

    public String getMessage(String path, Map<String, String> replacements) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        PreCondition.nonNull(replacements, "replacements는 null일 수 없습니다.");

        return color(replace(config.getString(path), replacements));
    }

    public List<String> getMessages(String path) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        List<String> messages = new ArrayList<>();
        for (String msg : config.getStringList(path)) {
            messages.add(color(msg));
        }

        return messages;
    }

    public List<String> getMessages(String path, Map<String, String> replacements) {
        PreCondition.nonNull(path, "path는 null일 수 없습니다.");
        PreCondition.nonNull(replacements, "replacements는 null일 수 없습니다.");

        List<String> messages = new ArrayList<>();
        for (String message : config.getStringList(path)) {
            messages.add(color(replace(message, replacements)));
        }

        return messages;
    }
}
