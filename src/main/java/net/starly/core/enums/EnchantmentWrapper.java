package net.starly.core.enums;

import net.starly.core.util.PreCondition;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

public enum EnchantmentWrapper {
    SHARPNESS("날카로움", "Sharpness", Enchantment.DAMAGE_ALL),
    POWER("힘", "Power", Enchantment.ARROW_DAMAGE),
    FLAME("화염", "Flame", Enchantment.ARROW_FIRE),
    INFINITY("무한", "Infinity", Enchantment.ARROW_INFINITE),
    PUNCH("밀어내기", "Punch", Enchantment.ARROW_KNOCKBACK),
    BINDING_CURSE("귀속 저주", "Binding Curse", Enchantment.BINDING_CURSE),
    CHANNELING("집전", "Channeling", Enchantment.CHANNELING),
    BANE_OF_ARTHROPODS("살충", "Bane Of Arthropods", Enchantment.DAMAGE_ARTHROPODS),
    SMITE("강타", "Smite", Enchantment.DAMAGE_UNDEAD),
    DEPTH_STRIDER("물갈퀴", "Depth Strider", Enchantment.DEPTH_STRIDER),
    ERRICIENCY("효율", "Efficiency", Enchantment.DIG_SPEED),
    UNBREAKING("내구성", "Unbreaking", Enchantment.DURABILITY),
    FIRE_ASPECT("발화", "Fire Aspect", Enchantment.FIRE_ASPECT),
    FROST_WALKER("차가운 걸음", "Frost Walker", Enchantment.FROST_WALKER),
    IMPALING("찌르기", "Impaling", Enchantment.IMPALING),
    KNOCKBACK("밀치기", "Knockback", Enchantment.KNOCKBACK),
    FORTUNE("행운", "Fortune", Enchantment.LOOT_BONUS_BLOCKS),
    LOOTING("약탈", "Looting", Enchantment.LOOT_BONUS_MOBS),
    LOYALTY("충절", "Loyalty", Enchantment.LOYALTY),
    LUCK_OF_SEA("바다의 행운", "Luck Of Sea", Enchantment.LUCK),
    LURE("미끼", "Lure", Enchantment.LURE),
    MENDING("수선", "Mending", Enchantment.MENDING),
    MULTISHOT("다중발사", "Multishot", Enchantment.MULTISHOT),
    RESPIRATION("호흡", "Respiration", Enchantment.OXYGEN),
    PIERCING("관통", "Piercing", Enchantment.PIERCING),
    PROTECTION("보호", "Protection", Enchantment.PROTECTION_ENVIRONMENTAL),
    BLAST_PROTECTION("폭발로부터 보호", "Blast Protection", Enchantment.PROTECTION_EXPLOSIONS),
    FEATHER_FALLING("가벼운 착지", "Feather Falling", Enchantment.PROTECTION_FALL),
    FIRE_PROTECTION("화염으로부터 보호", "Fire Protection", Enchantment.PROTECTION_FIRE),
    PROJECTILE_PROTECTION("발사체로부터 보호", "Projectile Protection", Enchantment.PROTECTION_PROJECTILE),
    QUICK_CHARGE("빠른 장전", "Quick Charge", Enchantment.QUICK_CHARGE),
    RIPTIDE("급류", "Riptide", Enchantment.RIPTIDE),
    SILK_TOUCH("섬세한 손길", "Silk Touch", Enchantment.SILK_TOUCH),
    SOUL_SPEED("영혼 가속", "Soul Speed", Enchantment.SOUL_SPEED),
    SWEEPING_EDGE("휘몰아치는 칼날", "Sweeping Edge", Enchantment.SWEEPING_EDGE),
    THORNS("가시", "Thorns", Enchantment.THORNS),
    VANISHING_CURSE("소실저주", "Vanishing Curse", Enchantment.VANISHING_CURSE),
    AQUA_AFFINITY("친수성", "Aqua Affinity", Enchantment.WATER_WORKER);


    private final String kr;
    private final String en;
    private final Enchantment enchantment;

    EnchantmentWrapper(@NotNull String kr, @NotNull String en, @NotNull Enchantment enchantment) {
        PreCondition.nonNull(kr, "kr은 null일 수 없습니다.");
        PreCondition.nonNull(en, "en은 null일 수 없습니다.");
        PreCondition.nonNull(enchantment, "enchantment은 null일 수 없습니다.");

        this.kr = kr;
        this.en = en;
        this.enchantment = enchantment;
    }

    public String getName(@NotNull Locale locale) {
        PreCondition.nonNull(locale, "locale은 null일 수 없습니다.");

        return locale == Locale.KR ? kr : (locale == Locale.EN ? en : null);
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public static EnchantmentWrapper getWrapper(@NotNull Enchantment enchantment) {
        PreCondition.nonNull(enchantment, "enchantment은 null일 수 없습니다.");

        for (EnchantmentWrapper wrapper : values()) {
            if (wrapper.getEnchantment().equals(enchantment)) return wrapper;
        }

        return null;
    }

    public static EnchantmentWrapper getWrapper(@NotNull Locale locale, @NotNull String name) {
        PreCondition.nonNull(locale, "locale은 null일 수 없습니다.");
        PreCondition.nonNull(name, "name은 null일 수 없습니다.");

        for (EnchantmentWrapper wrapper : values()) {
            if (wrapper.getName(locale).replace(" ", "").equals(name.replace(" ", ""))) return wrapper;
        }

        return null;
    }
}