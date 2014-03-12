package com.projectrixor.rixor.scrimmage.countdowns;

import me.confuser.barapi.BarAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RestartCountdown extends Countdown
{

    public RestartCountdown()
    {
    }

    public void onTick(int secondsLeft, int starttime)
    {
        makeBar(secondsLeft, starttime);
    }

    public void onEnd()
    {
        for (Player Online : Bukkit.getOnlinePlayers()) {
            Online.kickPlayer(ChatColor.GOLD + "Server Restarting! \n" + ChatColor.AQUA + "Rejoin!");
        }
    }

    private void makeBar(int secondsLeft, int starttime) {
        String p = "s";
        if(secondsLeft == 1) p = "";
        for (Player Online : Bukkit.getOnlinePlayers()) {
            BarAPI.setMessage(Online, ChatColor.DARK_AQUA + "Server restarting in " + ChatColor.DARK_RED + secondsLeft + ChatColor.DARK_AQUA + " second" + p + "!", (float) secondsLeft / starttime * 100);
        }
    }
}
