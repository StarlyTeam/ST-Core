package net.starly.core.data;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Time extends BukkitRunnable {
    private double seconds;

    /**
     * Time 오브젝트를 생성합니다.
     *
     * @param       seconds         기본 시간 (초)
     */
    public Time(double seconds) {
        this.seconds = seconds;
    }

    /**
     * Time 오브젝트를 자동 태스크와 함께 생성합니다.
     *
     * @param       seconds         기본 시간 (초)
     * @param       plugin          플러그인 인스턴스
     */
    public Time(double seconds, JavaPlugin plugin) {
        this.seconds = seconds;
        this.runTaskTimer(plugin, 0, 20);
    }

    /**
     * 시간을 추가합니다.
     *
     * @param       seconds         추가할 시간 (초)
     */
    public void add(double seconds) {
        this.seconds += seconds;
    }

    /**
     * 시간을 제거합니다.
     *
     * @param       seconds         제거할 시간 (초)
     */
    public void subtract(double seconds) {
        this.seconds -= seconds;
    }


    /**
     * 남은 시간을 불러옵니다.
     *
     * @return      Double          남은 시간 (초)
     */
    public double getSeconds() {
        return this.seconds;
    }

    /**
     * 시간을 재설정합니다.
     *
     * @param       seconds         재설정할 시간 (초)
     */
    public void setSeconds(double seconds) {
        this.seconds = seconds;
    }

    /**
     * 남은 시간을 불러옵니다.
     *
     * @return      Long            남은 시간 (분)
     */
    public long getMinutes() {
        return Math.round(getSeconds() / 60);
    }

    /**
     * 남은 시간을 불러옵니다.
     *
     * @return      Long            남은 시간 (시간)
     */
    public long getHours() {
        return Math.round(getMinutes() / 60);
    }

    /**
     * 남은 시간을 불러옵니다.
     *
     * @return      Long            남은 시간 (일)
     */
    public long getDays() {
        return Math.round(getHours() / 24);
    }

    /**
     * 남은 시간을 불러옵니다.
     *
     * @return      Long            남은 시간 (주)
     */
    public long getWeeks() {
        return Math.round(getDays() / 7);
    }

    /**
     * 남은 시간을 불러옵니다.
     *
     * @return      Long            남은 시간 (달)
     */
    public long getMonths() {
        return Math.round(getWeeks() / 4);
    }

    /**
     * 남은 시간을 불러옵니다.
     *
     * @return      Long            남은 시간 (년)
     */
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
