package com.projectrixor.rixor.scrimmage.map;

import com.projectrixor.rixor.scrimmage.Match;


import org.bukkit.ChatColor;


public class MapTeam
{
    protected final MapTeamFactory info;
    protected final Match match;
    protected String name = null;
    protected ChatColor color = null;

    public MapTeam(MapTeamFactory info, Match match)
    {
        this.info = info;
        this.match = match;
    }

    public String toString()
    {
        return getName();
    }

    public MapTeamFactory getInfo()
    {
        return this.info;
    }

    public MapTeamFactory.Type getType()
    {
        return getInfo().getType();
    }


    public Match getMatch()
    {
        return this.match;
    }

    public String getName()
    {
        if (this.name == null) {
            return this.info.getDefaultName();
        }
        return this.name;
    }

    public String getColoredName()
    {
        return getColor() + getName();
    }

    public void setName(String newName)
    {
        this.name = newName;
    }

    public ChatColor getColor()
    {
        if (this.color == null) {
            return this.info.getDefaultColor();
        }
        return this.color;
    }

    public void setColor(ChatColor newColor)
    {
        this.color = newColor;
    }


}
