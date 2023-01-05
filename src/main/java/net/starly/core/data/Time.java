package net.starly.core.data;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Time extends BukkitRunnable {
    private double seconds;

    public Time(double seconds) {
        this.seconds = seconds;
    }

    public Time(double seconds, JavaPlugin plugin) {
        this.seconds = seconds;
        this.runTaskTimer(plugin, 0, 20);
    }

    public void add(double seconds) {
        this.seconds += seconds;
    }

    public void subtract(double seconds) {
        this.seconds -= seconds;
    }

    public double getSeconds() {
        return this.seconds;
    }

    public void setSeconds(double seconds) {
        this.seconds = seconds;
    }

    public long getMinutes() {
        return Math.round(getSeconds() / 60);
    }

    public long getHours() {
        return Math.round(getMinutes() / 60);
    }

    public long getDays() {
        return Math.round(getHours() / 24);
    }

    public long getWeeks() {
        return Math.round(getDays() / 7);
    }

    public long getMonths() {
        return Math.round(getWeeks() / 4);
    }

    public long getYears() {
        return Math.round(getMonths() / 12);
    }


    @Override
    public void run() {
        this.subtract(1);

        if (this.getSeconds() == 0) {
            this.cancel();
        }
    }

    @Override
    public String toString() {
        return "Time{" +
                "seconds=" + seconds +
                "}";
    }
}
