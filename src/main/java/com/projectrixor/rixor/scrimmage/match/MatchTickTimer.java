package com.projectrixor.rixor.scrimmage.match;

import com.projectrixor.rixor.scrimmage.Match;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MatchTickTimer extends Event {
	
    private static final HandlerList handlers = new HandlerList();
    @Getter @Setter boolean ended = false;
    @Getter
    Match match;
 
    public MatchTickTimer(Match match) {
    	this.match = match;
    }
 
    public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }
	
}