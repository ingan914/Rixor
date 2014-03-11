package com.projectrixor.rixor.scrimmage.countdowns;

import org.bukkit.plugin.Plugin;

public class MatchCountdown  extends CountdownFactory
{
    public MatchCountdown(Plugin plugin)
    {
        super(plugin);
    }

    public void start(Countdown countdown, int seconds)
    {
        cancelAll();
        super.start(countdown, seconds);
    }
}
