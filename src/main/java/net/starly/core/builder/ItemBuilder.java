package net.starly.core.builder;

import com.sun.crypto.provider.Preconditions;
import net.starly.core.StarlyCore;
import net.starly.core.jb.version.nms.VersionController;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

public class ItemBuilder {
    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    /**
     * ItemStack을 이용해 ItemBuilder를 생성합니다.
     *
     * @param itemStack ItemStack 인스턴스
     */
    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    /**
     * Material과 수량을 이용해 ItemBuilder를 생성합니다.
     *
     * @param material 아이템의 종류
     * @param amount   아이템의 수량
     */
    public ItemBuilder(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    /**
     * Material을 이용해 ItemBuilder를 생성합니다.
     *
     * @param material 아이템의 종류
     */
    public ItemBuilder(Material material) {
        this(material, 1);
    }

    /**
     * 아이템의 타입을 설정합니다.
     *
     * @param type 아이템의 타입
     * @return this
     */
    public ItemBuilder setType(Material type) {
        itemStack.setType(type);
        return this;
    }

    /**
     * 아이템의 개수를 설정합니다.
     *
     * @param amount 아이템의 개수
     * @return this
     */
    public ItemBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    /**
     * 아이템의 이름을 설정합니다.
     *
     * @param name 아이템의 이름
     * @return this
     * @throws IllegalArgumentException 만약 이름(name)이 null일 경우
     */
    public ItemBuilder setDisplayName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("이름(name)은 null이 될 수 없습니다.");
        }
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        return this;
    }

    /**
     * 아이템의 설명(lore)을 설정합니다.
     *
     * @param lore 아이템의 설명
     * @return this
     */
    public ItemBuilder setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    /**
     * 아이템의 설명(lore)을 설정합니다.
     *
     * @param lore 아이템의 설명
     * @return this
     */
    public ItemBuilder setLore(List<String> lore) {
        List<String> coloredLore = lore.stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .collect(Collectors.toList());
        itemMeta.setLore(coloredLore);
        return this;
    }

    /**
     * 아이템의 설명(lore)을 추가합니다.
     *
     * @param line 추가될 설명
     * @return this
     */
    public ItemBuilder addLore(String line) {
        List<String> lore = new ArrayList<>(itemMeta.getLore());
        lore.add(ChatColor.translateAlternateColorCodes('&', line));
        itemMeta.setLore(lore);
        return this;
    }

    /**
     * 아이템의 파괴 불가능 여부를 설정합니다.
     *
     * @param unbreakable 아이템의 파괴 불가능 여부
     * @return this
     */
    public ItemBuilder setUnbreakable(boolean unbreakable) {
        itemMeta.setUnbreakable(unbreakable);
        return this;
    }

    /**
     * 아이템에 마법 부여를 추가합니다.
     *
     * @param enchantment 마법 부여 유형
     * @param level       마법 부여 레벨
     * @return this
     * @throws IllegalArgumentException 만약 마법 부여(enchantment)가 null이거나 레벨(level)이 1 미만일 경우
     */
    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        if (enchantment == null) {
            throw new IllegalArgumentException("마법 부여(enchantment)는 null이 될 수 없습니다.");
        }
        if (level < 1) {
            throw new IllegalArgumentException("마법 부여 레벨(level)은 1보다 작을 수 없습니다.");
        }
        itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    /**
     * 비정상 마법 부여를 추가합니다.
     *
     * @deprecated use instead {@link ItemBuilder#addEnchantment}
     * @param enchantment 마법 부여 유형
     * @param level       마법 부여 레벨
     * @return this
     */
    @Deprecated
    public ItemBuilder addUnsafeEnchantment(Enchantment enchantment, int level) {
        itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    /**
     * 아이템에서 마법 부여를 제거합니다.
     *
     * @param enchantment 마법 부여 유형
     * @return this
     */
    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        itemMeta.removeEnchant(enchantment);
        return this;
    }

    /**
     * 아이템의 커스텀 모델 데이터를 설정합니다.
     *
     * @param customModelData 커스텀 모델 데이터 코드
     * @return this
     */
    public ItemBuilder setCustomModelData(int customModelData) {
        itemMeta.setCustomModelData(customModelData);
        return this;
    }

    /**
     * 아이템에서 커스텀 모델 데이터를 제거합니다.
     *
     * @return this
     */
    public ItemBuilder removeCustomModelData() {
        setCustomModelData(0);
        return this;
    }

    /**
     * 아이템의 내구도를 설정합니다.
     *
     * @param durability 남아있는 내구도
     * @return this
     */
    public ItemBuilder setDurability(short durability) {
        ((Damageable) itemMeta).setDamage(itemStack.getType().getMaxDurability() - durability);
        return this;
    }

    /**
     * 아이템에 플래그를 추가합니다.
     *
     * @param itemFlag 추가할 아이템 플래그
     * @return this
     */
    public ItemBuilder addItemFlag(ItemFlag itemFlag) {
        itemMeta.addItemFlags(itemFlag);
        return this;
    }


    /**
     * 아이템에 NBT 태그를 설정합니다.
     *
     * @param key   키
     * @param value 값
     * @return this
     */
    public ItemBuilder setNBT(String key, String value) {
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(JavaPlugin.getProvidingPlugin(StarlyCore.class), key), PersistentDataType.STRING, value);
        return this;
    }

    /**
     * 아이템의 NBT 태그를 삭제합니다.
     *
     * @param key 키
     * @return this
     */
    public ItemBuilder removeNBT(String key) {
        itemMeta.getPersistentDataContainer().remove(new NamespacedKey(JavaPlugin.getProvidingPlugin(StarlyCore.class), key));
        return this;
    }

    /**
     * 아이템에 PDC 태그를 추가합니다.
     *
     * @param key       키
     * @param value     값
     * @param type      타입
     * @param plugin    플러그인
     * @return this
     */
    public ItemBuilder setPDC(String key, String value, PersistentDataType<Object, Object> type, JavaPlugin plugin) {
        try {
            itemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, key), type, value);
        } catch (NoClassDefFoundError ignored) {
            throw new UnsupportedOperationException("ItemBuilder#setPDC is not available on this version");
        }
        return this;
    }

    /**
     * 아이템에 PDC 태그를 삭제합니다.
     *
     * @param key       키
     * @param plugin    플러그인
     * @return this
     */
    public ItemBuilder removePDC(String key, JavaPlugin plugin) {
        try {
            itemMeta.getPersistentDataContainer().remove(new NamespacedKey(plugin, key));
        } catch (NoClassDefFoundError ignored) {
            throw new UnsupportedOperationException("ItemBuilder#removePDC is not available on this version");
        }
        return this;
    }

    /**
     * 플레이어 머리의의 소유자를 설정합니다.
     *
     * @param owner 스컬 아이템의 소유자 UUID
     * @return this
     * @throws IllegalArgumentException 만약 owner가 null이거나 itemMeta가 SkullMeta의 인스턴스가 아닐 경우
     */
    public ItemBuilder setSkullOwner(UUID owner) {
        if (owner == null) {
            throw new IllegalArgumentException("소유자 UUID(owner)는 null이 될 수 없습니다.");
        } else if (!(itemMeta instanceof SkullMeta)) {
            throw new IllegalArgumentException("itemMeta는 SkullMeta의 인스턴스여야 합니다.");
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(owner);
        ((SkullMeta) itemMeta).setOwningPlayer(player);
        return this;
    }

    /**
     * 가죽 방어구 아이템의 색을 설정합니다.
     *
     * @param color 가죽 방어구 아이템의 색
     * @return this
     */
    public ItemBuilder setColor(Color color) {
        if (itemMeta instanceof LeatherArmorMeta) ((LeatherArmorMeta) itemMeta).setColor(color);
        return this;
    }

    /**
     * 최종적으로 아이템스택을 생성합니다.
     *
     * @return 생성된 아이템스택
     */
    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
