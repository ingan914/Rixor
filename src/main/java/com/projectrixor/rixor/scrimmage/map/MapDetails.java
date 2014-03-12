package com.projectrixor.rixor.scrimmage.map;

import com.projectrixor.rixor.scrimmage.map.extras.Contributor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.World;

import java.util.List;

public class MapDetails {
    @Getter
    String name;
    @Getter String version;
    @Getter String objective;
    @Getter
    List<String> rules;
    @Getter List<Contributor> authors;
    @Getter List<Contributor> contributors;
    @Getter Difficulty difficulty;
    @Getter boolean friendlyFire;

    public MapDetails(String name, String version, String objective, List<Contributor> authors, List<Contributor> contributors, List<String> rules, Difficulty difficulty, World.Environment dimension, boolean friendlyFire)
    {
        this.name = name;
        this.version = version;
        this.objective = objective;
        this.authors = authors;
        this.contributors = contributors;
        this.rules = rules;
        this.difficulty = difficulty;
        this.friendlyFire = friendlyFire;
    }

}

