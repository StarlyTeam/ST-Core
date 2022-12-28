package net.starly.core.data.location;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Region {
    private final Location pos1;
    private final Location pos2;

    private final int minX;
    private final int minY;
    private final int minZ;
    private final int maxX;
    private final int maxY;
    private final int maxZ;

    public Region(Location pos1, Location pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;

        this.minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        this.maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());

        this.minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        this.maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());

        this.minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        this.maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
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
                    blocks.add(pos1.getWorld().getBlockAt(i, j, k));
                }
            }
        }

        return blocks;
    }

    public boolean contains(Location location) {
        return location.getWorld().equals(pos1.getWorld()) &&
                location.getBlockX() >= minX && location.getBlockX() <= maxX &&
                location.getBlockY() >= minY && location.getBlockY() <= maxY &&
                location.getBlockZ() >= minZ && location.getBlockZ() <= maxZ;
    }

    public boolean isInRegion(Player player) {
        return contains(player.getLocation()) ||
                contains(player.getLocation().add(0, 1, 0));
    }

    public long getSize() {
        return (long) (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1);
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
                "pos1=" + pos1 +
                ", pos2=" + pos2 +
                ", minX=" + minX +
                ", minY=" + minY +
                ", minZ=" + minZ +
                ", maxX=" + maxX +
                ", maxY=" + maxY +
                ", maxZ=" + maxZ +
                '}';
    }
}
