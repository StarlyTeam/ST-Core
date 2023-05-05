package net.starly.core.jb.version.nms;

import net.starly.core.jb.exception.UnSupportedVersionException;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Optional;

public class VersionController {

    public enum Version {
        v1_12_R1("1.12-R0.1 | 1.12.1-R0.1 | 1.12.2-R0.1", false),

        v1_13_R1("1.13-R0.1 | 1.13.1-R0.1 | 1.13-R0.1", true),
        v1_13_R2("1.13.2-R0.1", true),

        v1_14_R1("1.14-R0.1 | 1.14.1-R0.1 | 1.14.2-R0.1 | 1.14.3-R0.1 | 1.14.4-R0.1", true),

        v1_15_R1("1.15-R0.1 | 1.15.1-R0.1 | 1.15.2-R0.1", true),

        v1_16_R1("1.16-R0.1 | 1.16.1-R0.1 | 1.16.2-R0.1", true),
        v1_16_R2("1.16.3-R0.1", true),
        v1_16_R3("1.16.4-R0.1 | 1.16.5-R0.1", true),

        v1_17_R1("1.17-R0.1 | 1.17.1-R0.1", true),

        v1_18_R1("1.18-R0.1 | 1.18.1-R0.1", true),
        v1_18_R2("1.18.2-R0.1", true),

        v1_19_R1("1.19-R0.1 | 1.19.1-R0.1 | 1.19.2-R0.1", true),
        v1_19_R2("1.19.3-R0.1", true),
        v1_19_R3("1.19.4-R0.1", true);

        private final String v;
        public final String version = name();
        public final boolean highVersion;

        Version(String v, boolean highVersion) {
            this.v = v;
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
        } catch (Exception ignored) {
            return null;
        }
    }

    private final Version version;
    public Version getVersion() {
        return version;
    }

    private VersionController(Server server) throws UnSupportedVersionException {
        version = checkVersions(server);
    }

    private Version checkVersions(Server server) throws UnSupportedVersionException {
        System.out.println("Running on currently version : " + server.getBukkitVersion());
        Optional<Version> versionFilter = Arrays.stream(Version.values()).filter(it -> Arrays.stream(it.v.split(" \\| ")).anyMatch(v -> server.getBukkitVersion().contains(v))).findFirst();
        if (versionFilter.isPresent()) return versionFilter.get();
        else throw new UnSupportedVersionException(server.getVersion());
    }

}
