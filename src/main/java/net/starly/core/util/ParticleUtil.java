package net.starly.core.util;

import net.starly.core.StarlyCore;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import static java.lang.Math.cos;
import static java.lang.Math.sin;


public class ParticleUtil {
    /**
     * 폭죽 파티클을 생성합니다.
     *
     * @param       loc     파티클이 생성될 위치
     * @param       color   파티클의 색상
     */
    public static void firework(Location loc, Color color) {
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fm = fw.getFireworkMeta();
        fm.setPower(0);
        fm.addEffect(FireworkEffect.builder().withColor(color).with(FireworkEffect.Type.BALL_LARGE).build());
        fw.setFireworkMeta(fm);
    }

    /**
     * 점 파티클을 생성합니다.
     *
     * @param       p       파티클을 보여줄 플레이어
     * @param       count   파티클의 개수
     * @param       size    파티클의 크기
     * @param       color   파티클의 색상
     */
    public static void dot(Player p, int count, double size, Color color) {
        new BukkitRunnable() {
            @Override
            public void run() {
                p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation(), count, new Particle.DustOptions(color, (float) size));
            }
        }.runTask(JavaPlugin.getProvidingPlugin(StarlyCore.class));
    }

    /**
     * 선 파티클을 생성합니다.
     *
     * @param       point1  파티클이 생성될 첫번째 위치
     * @param       point2  파티클이 생성될 두번째 위치
     * @param       space   파티클 사이의 간격
     * @param       color   파티클의 색상
     */
    public static void line(Location point1, Location point2, double space, Color color) {
        double distance = point1.distance(point2);
        Vector p1 = point1.toVector();
        Vector p2 = point2.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
        double covered = 0;
        for (; covered < distance; p1.add(vector)) {
            point1.getWorld().spawnParticle(Particle.REDSTONE, p1.getX(), p1.getY(), p1.getZ(), 1, new Particle.DustOptions(color, 1));
            covered += space;
        }
    }

    /**
     * 구 파티클을 생성합니다.
     *
     * @param       middle  파티클이 생성될 중심 위치
     * @param       radius  구의 반지름
     * @param       color   파티클의 색상
     */
    public static void sphere(Location middle, double radius, Color color) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (double polarAnge = 0.0; polarAnge < 360.0; polarAnge += 15) {
                    for (double elevationAngle = 0.0; elevationAngle < 180.0; elevationAngle += 15) {
                        double dx = radius * sin(Math.toRadians(elevationAngle)) * cos(Math.toRadians(polarAnge));
                        double dy = radius * sin(Math.toRadians(elevationAngle)) * sin(Math.toRadians(polarAnge));
                        double dz = radius * cos(Math.toRadians(elevationAngle));
                        middle.getWorld().spawnParticle(Particle.REDSTONE, middle.clone().add(dx, dy, dz), 0, dx, dy, dz, new Particle.DustOptions(color, 1));
                    }
                }
            }
        }.runTask(JavaPlugin.getProvidingPlugin(StarlyCore.class));
    }

    /**
     * 구 파티클을 생성합니다.
     *
     * @param       middle      파티클이 생성될 중심 위치
     * @param       radius      구의 반지름
     * @param       distance    파티클 사이의 간격
     * @param       color       파티클의 색상
     */
    public static void sphere(Location middle, double radius, double distance, Color color) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (double polarAnge = 0.0; polarAnge < 360.0; polarAnge += distance) {
                    for (double elevationAngle = 0.0; elevationAngle < 180.0; elevationAngle += distance) {
                        double dx = radius * sin(Math.toRadians(elevationAngle)) * cos(Math.toRadians(polarAnge));
                        double dy = radius * sin(Math.toRadians(elevationAngle)) * sin(Math.toRadians(polarAnge));
                        double dz = radius * cos(Math.toRadians(elevationAngle));
                        middle.getWorld().spawnParticle(Particle.REDSTONE, middle.clone().add(dx, dy, dz), 0, dx, dy, dz, new Particle.DustOptions(color, 3));
                    }
                }
            }
        }.runTask(JavaPlugin.getProvidingPlugin(StarlyCore.class));
    }

    /**
     * 구체 파티클을 생성합니다.
     *
     * @param       middle      파티클이 생성될 중심 위치
     * @param       radius      구의 반지름
     * @param       distance    파티클 사이의 간격
     * @param       particle    파티클의 종류
     */
    public static void sphere(Location middle, double radius, double distance, Particle particle) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (double polarAnge = 0.0; polarAnge < 360.0; polarAnge += distance) {
                    for (double elevationAngle = 0.0; elevationAngle < 180.0; elevationAngle += distance) {
                        double dx = radius * sin(Math.toRadians(elevationAngle)) * cos(Math.toRadians(polarAnge));
                        double dy = radius * sin(Math.toRadians(elevationAngle)) * sin(Math.toRadians(polarAnge));
                        double dz = radius * cos(Math.toRadians(elevationAngle));
                        middle.getWorld().spawnParticle(particle, middle.clone().add(dx, dy, dz), 0, 0, 0, 0);
                    }
                }
            }
        }.runTask(JavaPlugin.getProvidingPlugin(StarlyCore.class));
    }
}
