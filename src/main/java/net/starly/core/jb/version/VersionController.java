package net.starly.core.jb.version;

import lombok.Getter;
import net.starly.core.jb.exception.UnSupportedVersionException;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Arrays;
import java.util.Optional;

public class VersionController {

    public enum Version {
        v1_12_R1("1.12", false),
        v1_13_R1("1.13", true),
        v1_14_R1("1.14", true),
        v1_15_R1("1.15", true),
        v1_16_R1("1.16", true),
        v1_17_R1("1.17", true),
        v1_18_R1("1.18", true),
        v1_19_R1("1.19-R0.1", true),
        v1_19_R2("1.19", true);

        @Getter private final String v;
        @Getter public final String version = name();
        @Getter public final boolean highVersion;
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

    @Getter public final Version version;

    private VersionController(Server server) throws UnSupportedVersionException {
        version = checkVersions(server);
    }

    private Version checkVersions(Server server) throws UnSupportedVersionException {
        System.out.println(server.getBukkitVersion());
        Optional<Version> versionFilter = Arrays.stream(Version.values()).filter(it->server.getBukkitVersion().contains(it.v)).findFirst();
        if(versionFilter.isPresent()) return versionFilter.get();
        else throw new UnSupportedVersionException(server.getVersion());
    }

}
