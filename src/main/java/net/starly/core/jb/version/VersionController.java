package net.starly.core.jb.version;

import lombok.Getter;
import net.starly.core.util.collection.STSet;

import java.util.Optional;

import static org.bukkit.Bukkit.getServer;

public class VersionController {

    public static final STSet<String> HIGH_VERSIONS = new STSet<>("v1_12_R1", "v1_12_R2", "v1_13_R1", "v1_13_R2", "v1_14_R1", "v1_15_R1", "v1_16_R1", "v1_16_R2", "v1_16_R3", "v1_17_R1", "v1_18_R1", "v1_18_R2", "v1_19_R1", "v1_19_R2", "v1_19_R3");

    private static VersionController instance;
    public static VersionController getInstance() {
        if (instance == null) instance = new VersionController();
        return instance;
    }

    @Getter private Optional<String> version;
    private VersionController() {
        String v = getServer().getClass().getName();
        v = v.substring(v.lastIndexOf('.') + 1);

        try {
            Class.forName("net.minecraft.server." + v + ".ItemStack");
            version = Optional.of(v);
        } catch (ClassNotFoundException ignored) {
            version = Optional.empty();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
