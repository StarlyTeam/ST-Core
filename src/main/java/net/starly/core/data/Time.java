package net.starly.core.data;

public class Time {
    private long seconds;

    public Time(long seconds) {
        this.seconds = seconds;
    }

    public void add(long seconds) {
        this.seconds += seconds;
    }

    public void subtract(long seconds) {
        this.seconds -= seconds;
    }

    public long getSeconds() {
        return this.seconds;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    public long getMinutes() {
        return (long) (seconds / 60.0);
    }

    public long getHours() {
        return (long) (seconds / 3600.0);
    }

    public long getDays() {
        return (long) (seconds / 86400.0);
    }

    public long getWeeks() {
        return (long) (seconds / 604800.0);
    }

    public long getMonths() {
        return (long) (seconds / 2592000.0);
    }

    public long getYears() {
        return (long) (seconds / 31536000.0);
    }
}
