package com.projectrixor.rixor.scrimmage.player;

import java.util.ArrayList;

public class StatTracker {
	public static ArrayList<String> totalKills = new ArrayList<String>();
	public static ArrayList<String> totalDeaths = new ArrayList<String>();
	
	public static void gainKill(String name) {
		int kills = 0;
		for (Object part : totalKills.toArray()) {
			String[] parts = part.toString().split(":");
			if (parts[0].equalsIgnoreCase(name)) {
				totalKills.remove(part);
				kills = Integer.parseInt(parts[1]);
			}
		}
		totalKills.add(name + ":" + kills);
	}
	
	public static void gainDeath(String name) {
		int kills = 0;
		for (Object part : totalDeaths.toArray()) {
			String[] parts = part.toString().split(":");
			if (parts[0].equalsIgnoreCase(name)) {
				totalDeaths.remove(part);
				kills = Integer.parseInt(parts[1]);
			}
		}
		totalDeaths.add(name + ":" + kills);
	}
}
