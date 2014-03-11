package com.projectrixor.rixor.scrimmage.countdowns;

import com.projectrixor.rixor.scrimmage.Map;
import com.projectrixor.rixor.scrimmage.Scrimmage;
import com.projectrixor.rixor.scrimmage.match.Match;
import com.projectrixor.rixor.scrimmage.rotation.Rotation;
import com.projectrixor.rixor.scrimmage.rotation.RotationSlot;
import me.confuser.barapi.BarAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MatchCycleCountdown extends Countdown
{
    protected final Match match;

    public MatchCycleCountdown(Match match)
    {
        this.match = match;
    }

    public void onTick(int secondsLeft, int starttime)
    {
        makeBar(secondsLeft, starttime);
    }

    public void onEnd()
    {

    }

    private void makeBar(int secondsLeft, int starttime) {
        String p = "s";
        if(secondsLeft == 1) p = "";
        for (Player Online : Bukkit.getOnlinePlayers()) {
            BarAPI.setMessage(Online, ChatColor.DARK_AQUA + "Cycling to " + ChatColor.AQUA + Scrimmage.getRotation().getNext().getLoader().getName() + ChatColor.DARK_AQUA
                    + " in " + ChatColor.DARK_RED + secondsLeft + ChatColor.DARK_AQUA + " second" + p + "!", (float) secondsLeft / starttime * 100);
        }
    }
}
