package net.starly.core.jb.version.nms.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.World;

@AllArgsConstructor
@Getter
@Setter
public class WorldWrapper {
    public World bukkitWorld;
    public Object world;
}
