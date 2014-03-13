package com.projectrixor.rixor.scrimmage.map;

import com.sun.istack.internal.Nullable;
import org.bukkit.ChatColor;

public class MapTeamFactory
{
    protected final Type type;
    protected final String defaultName;
    protected final ChatColor defaultColor;

    @Nullable
    protected final ChatColor overheadColor;
    protected final int maxPlayers;

    public MapTeamFactory(Type type, String defaultName, ChatColor defaultColor, @Nullable ChatColor overheadColor, int maxPlayers)
    {
        this.type = type;
        this.defaultName = defaultName;
        this.defaultColor = defaultColor;
        this.overheadColor = overheadColor;
        this.maxPlayers = maxPlayers;
    }

    public String toString()
    {
        return getDefaultName();
    }

    public Type getType()
    {
        return this.type;
    }

    public String getDefaultName()
    {
        return this.defaultName;
    }

    public ChatColor getDefaultColor()
    {
        return this.defaultColor;
    }

    public ChatColor getOverheadColor()
    {
        return this.overheadColor != null ? this.overheadColor : this.defaultColor;
    }

    public int getMaxPlayers()
    {
        return this.maxPlayers;
    }

    public static enum Type
    {
        Participating,

        Observing;
    }
}
