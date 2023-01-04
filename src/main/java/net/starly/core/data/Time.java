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
        return (long) getSeconds() / 60;
    }

    public long getHours() {
        return (long) getMinutes() / 60;
    }

    public long getDays() {
        return (long) getHours() / 24;
    }

    public long getWeeks() {
        return (long) getDays() / 7;
    }

    public long getMonths() {
        return (long) getWeeks() / 4;
    }

    public long getYears() {
        return (long) getMonths() / 12;
    }
}
