package net.starly.core.jb.version.nms;

import net.starly.core.jb.exception.UnSupportedVersionException;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class VersionController {

    public enum Version {
        v1_12_R1(Arrays.asList("1.12-R0.1", "1.12.1-R0.1", "1.12.2-R0.1"), false),

        v1_13_R1(Arrays.asList("1.13-R0.1", "1.13.1-R0.1", "1.13-R0.1"), true),
        v1_13_R2(Arrays.asList("1.13.2-R0.1"), true),

        v1_14_R1(Arrays.asList("1.14-R0.1", "1.14.1-R0.1", "1.14.2-R0.1", "1.14.3-R0.1", "1.14.4-R0.1"), true),

        v1_15_R1(Arrays.asList("1.15-R0.1", "1.15.1-R0.1", "1.15.2-R0.1"), true),

        v1_16_R1(Arrays.asList("1.16-R0.1", "1.16.1-R0.1", "1.16.2-R0.1"), true),
        v1_16_R2(Arrays.asList("1.16.3-R0.1"), true),
        v1_16_R3(Arrays.asList("1.16.4-R0.1", "1.16.5-R0.1"), true),

        v1_17_R1(Arrays.asList("1.17-R0.1", "1.17.1-R0.1"), true),

        v1_18_R1(Arrays.asList("1.18-R0.1", "1.18.1-R0.1"), true),
        v1_18_R2(Arrays.asList("1.18.2-R0.1"), true),

        v1_19_R1(Arrays.asList("1.19-R0.1", "1.19.1-R0.1", "1.19.2-R0.1"), true),
        v1_19_R2(Arrays.asList("1.19.3-R0.1"), true),
        v1_19_R3(Arrays.asList("1.19.4-R0.1"), true);

        private final List<String> bukkitVersion;
        public final String version = name();
        public final boolean highVersion;

        Version(List<String> bukkitVersion, boolean highVersion) {
            this.bukkitVersion = bukkitVersion;
            this.highVersion = highVersion;
        }
    }

    private static VersionController instance;
    private static JavaPlugin plugin;

    @Deprecated
    public static void $initializing(JavaPlugin plugin) {
        VersionController.plugin = plugin;
    }

    public static VersionController getInstance() {
        try {
            if (instance == null) instance = new VersionController(plugin.getServer());
            return instance;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private final Version version;
    public Version getVersion() {
        return version;
    }

    private VersionController(Server server) throws UnSupportedVersionException {
        version = checkVersion(server);
    }

    private Version checkVersion(Server server) throws UnSupportedVersionException {
        Optional<Version> versionFilter = Arrays
                .stream(Version.values())
                .filter(version -> version.bukkitVersion
                        .stream()
                        .anyMatch(bukkitVersion -> server.getBukkitVersion().contains(bukkitVersion))).findFirst();
        if (versionFilter.isPresent()) return versionFilter.get();
        else throw new UnSupportedVersionException(server.getVersion());
    }
}