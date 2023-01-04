package net.starly.core.data.location;

import net.starly.core.util.PreCondition;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Region {
    private final Location pos1;
    private final Location pos2;

    private final World world;
    private final int minX, minY, minZ;
    private final int maxX, maxY, maxZ;

    public Region(Location pos1, Location pos2) {
        PreCondition.nonNull(pos1, "pos1은 null일 수 없습니다.");
        PreCondition.nonNull(pos2, "pos2은 null일 수 없습니다.");

        if (pos1.getWorld() != pos2.getWorld()) {
            throw new IllegalArgumentException("두 위치의 월드는 같아야 합니다.");
        }

        this.world = pos1.getWorld();
        this.pos1 = pos1;
        this.pos2 = pos2;

        this.minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        this.maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());

        this.minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        this.maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());

        this.minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        this.maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
    }

    public World getWorld() {
        return world;
    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public List<Block> getBlocks() {
        List<Block> blocks = new ArrayList<>();
        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                for (int k = minZ; k <= maxZ; k++) {
                    blocks.add(world.getBlockAt(i, j, k));
                }
            }
        }

        return blocks;
    }

    public boolean contains(Location location) {
        return location.getWorld() == world &&
                location.getBlockX() >= minX && location.getBlockX() <= maxX &&
                location.getBlockY() >= minY && location.getBlockY() <= maxY &&
                location.getBlockZ() >= minZ && location.getBlockZ() <= maxZ;
    }

    public boolean isInRegion(Player player) {
        return contains(player.getLocation()) || contains(player.getLocation().add(0, 1, 0));
    }

    public long getSize() {
        return (long) (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1);
    }

    public Location getCenter() {
        return new Location(world, (minX + maxX) / 2.0, (minY + maxY) / 2.0, (minZ + maxZ) / 2.0);
    }

    public void teleport(Player player) {
        player.teleport(getCenter());
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMaxZ() {
        return maxZ;
    }

    @Override
    public String toString() {
        return "Region{" +
                "world=" + world +
                "pos1=" + pos1 +
                ", pos2=" + pos2 +
                ", minX=" + minX +
                ", minY=" + minY +
                ", minZ=" + minZ +
                ", maxX=" + maxX +
                ", maxY=" + maxY +
                ", maxZ=" + maxZ +
                "}";
    }
}
