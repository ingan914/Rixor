package com.projectrixor.rixor.scrimmage.countdowns;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class CountdownController
        implements Runnable
{
    protected final Plugin plugin;
    protected final Countdown countdown;
    protected int secondsLeft = -1;
    protected int taskId = -1;
    protected int starttime = -1;

    public CountdownController(Plugin plugin, Countdown countdown) {
        this.plugin = plugin;
        this.countdown = countdown;
    }

    public Countdown getCountdown() {
        return this.countdown;
    }

    public CountdownController start(int secondsLeft) {
        if (this.taskId == -1) {
            this.secondsLeft = secondsLeft;
            this.starttime = secondsLeft;
            this.taskId = getScheduler().scheduleSyncRepeatingTask(this.plugin, this, 0L, 20L);
            this.countdown.onStart(secondsLeft);
        }
        return this;
    }

    protected void cancel() {
        this.countdown.onCancel();
        stop();
    }

    protected void stop() {
        if (this.taskId != -1)
            getScheduler().cancelTask(this.taskId);
    }

    protected BukkitScheduler getScheduler()
    {
        return this.plugin.getServer().getScheduler();
    }

    public void run()
    {
        if (this.secondsLeft <= 0) {
            this.countdown.onEnd();
            stop();
        } else {
            this.countdown.onTick(this.secondsLeft, this.starttime);
        }
        this.secondsLeft -= 1;
    }
}
