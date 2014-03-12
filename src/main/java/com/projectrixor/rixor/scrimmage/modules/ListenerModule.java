package com.projectrixor.rixor.scrimmage.modules;

import com.projectrixor.rixor.scrimmage.Match;

public abstract class ListenerModule
{
    protected final Match match;

    public ListenerModule(Match match)
    {
        this.match = match;
    }
    public abstract void enable();

    public abstract void disable();

    public Match getMatch() {
        return this.match;
    }
}
