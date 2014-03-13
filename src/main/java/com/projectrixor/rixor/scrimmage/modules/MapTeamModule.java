package com.projectrixor.rixor.scrimmage.modules;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.projectrixor.rixor.scrimmage.Scrimmage;
import com.projectrixor.rixor.scrimmage.map.MapTeamFactory;
import com.projectrixor.rixor.scrimmage.utils.StringUtils;
import com.projectrixor.rixor.scrimmage.utils.XMLUtils;
import org.bukkit.ChatColor;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@ModuleDescription(name="Team")
public class MapTeamModule extends Module
{
    private final MapTeamFactory defaultTeam;
    private final Set<MapTeamFactory> teams;

    public MapTeamModule(MapTeamFactory defaultTeam, Set<MapTeamFactory> teams)
    {
        this.defaultTeam = defaultTeam;
        this.teams = teams;
    }

    public Set<MapTeamFactory> getTeams()
    {
        return Sets.union(this.teams, ImmutableSet.of(this.defaultTeam));
    }

    public MapTeamFactory getTeam(String search)
    {
        return (MapTeamFactory) StringUtils.bestFuzzyMatch(search, this.teams, 0.9D);
    }

    public MapTeamFactory getDefaultTeam() {
        return this.defaultTeam;
    }

    public static MapTeamModule parse(ModuleContext context, Logger logger, Document doc)
    {
        Set teamFactories = Sets.newLinkedHashSet();
        List<Element> teamsroot = doc.getRootElement().elements("teams");
        for (Element teamRootElement : teamsroot) {
            List<Element> teams = teamRootElement.elements("teaam");
            for (Element teamElement : teams) {
                teamFactories.add(parseTeam(teamElement));
            }
        }
        return new MapTeamModule(Scrimmage.createDefaultTeam(), teamFactories);
    }


    private static MapTeamFactory parseTeam(Element el) {
        String name = el.getText();
        int maxPlayers = Integer.parseInt(el.attributeValue("max"));
        ChatColor color = XMLUtils.parseChatColor(el.attributeValue("color"), ChatColor.WHITE);
        ChatColor overheadColor = XMLUtils.parseChatColor(el.attributeValue("overhead-color"), null);
        return new MapTeamFactory(MapTeamFactory.Type.Participating, name, color, overheadColor, maxPlayers);
    }
}
