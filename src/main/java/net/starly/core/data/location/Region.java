package net.starly.core.data.location;

import lombok.Getter;
import net.starly.core.util.PreCondition;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Region {
    private final Location pos1;
    private final Location pos2;

    private final World world;
    private final int minX, minY, minZ;
    private final int maxX, maxY, maxZ;


    /**
     * Region 오브젝트를 생성합니다.
     *
     * @param       pos1        첫번째 위치
     * @param       pos2        두번째 위치
     */
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

    /**
     * 구역에 포함된 모든 블럭을 반환합니다.
     *
     * @return      List<Block>     포함된 블럭들
     */
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

    /**
     * 위치가 구역에 포함되어 있는지 반환합니다.
     *
     * @param       location        위치
     * @return      Boolean         포함 여부
     */
    public boolean contains(Location location) {
        return location.getWorld() == world &&
                location.getBlockX() >= minX && location.getBlockX() <= maxX &&
                location.getBlockY() >= minY && location.getBlockY() <= maxY &&
                location.getBlockZ() >= minZ && location.getBlockZ() <= maxZ;
    }

    /**
     * 특정 플레이어가 구역에 포함되어 있는지 반환합니다.
     *
     * @param       player          플레이어
     * @return      Boolean         포함 여부
     */
    public boolean isInRegion(Player player) {
        return contains(player.getLocation()) || contains(player.getLocation().add(0, 1, 0));
    }

    /**
     * 구역의 크기를 반환합니다.
     *
     * @return      Long            구역의 크기
     */
    public long getSize() {
        return (long) (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1);
    }

    /**
     * 구역의 중점을 반환합니다.
     *
     * @return      Location        구역의 중점
     */
    public Location getCenter() {
        return new Location(world, (minX + maxX) / 2.0, (minY + maxY) / 2.0, (minZ + maxZ) / 2.0);
    }

    /**
     * 플레이어를 구역의 중점으로 텔레포트합니다.
     *
     * @param       player          플레이어
     */
    public void teleport(Player player) {
        player.teleport(getCenter());
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
