package com.projectrixor.rixor.scrimmage.modules;

import com.projectrixor.rixor.scrimmage.map.MapDetails;
import com.projectrixor.rixor.scrimmage.map.Contributor;
import com.projectrixor.rixor.scrimmage.utils.XMLUtils;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ModuleDescription(name="Details")
public class MapDetailsModule extends Module
{
    private final MapDetails details;

    public MapDetailsModule(MapDetails details)
    {
        this.details = details;
    }

    public MapDetails getDetails() {
        return this.details;
    }

    public static MapDetailsModule parse(ModuleContext context, Logger logger, Document doc) {
        Element root = doc.getRootElement();

        String name = root.elementText("name");
        if (name == null) {
            logger.severe("Map must have a name!");
        }

        String version = root.elementText("version");
        if (version == null) {
            logger.severe("Map needs a version!");
        }

        String objective = root.elementText("objective");
        if (objective == null) {
            logger.severe("Map must have at least one objective!");
        }

        List authors = readContributorList(root, "authors", "author");
        if (authors.size() < 1) {
            logger.severe("Map needs one or more authors!");
        }

        List contributors = readContributorList(root, "contributors", "contributor");

        List rules = new ArrayList();
        List<Element> ruleselements = root.element("rules").elements("rule");
        for (Element rule : ruleselements) {
                rules.add(rule.getText());
        }

        Difficulty difficulty = XMLUtils.parseDifficulty(root.elementText("difficulty"));

        World.Environment dimension = (World.Environment)XMLUtils.getEnumFromString(World.Environment.class, root.elementText("dimension"), World.Environment.NORMAL);

        boolean friendlyFire = XMLUtils.parseBoolean(root.elementText("friendlyfire"), false);

        return new MapDetailsModule(new MapDetails(name, version, objective, authors, contributors, rules, difficulty, dimension, friendlyFire));
    }

    private static List<Contributor> readContributorList(Element root, String topLevelTag, String tag) {
        List contribs = new ArrayList();
        List<Element> elems = root.elements(topLevelTag);
        for (Element element : elems) {
            List<Element> children = element.elements(tag);
            for (Element child : children) {
                String name = child.getText();
                if (child.attribute("contribution") != null)
                    contribs.add(new Contributor(name, child.attribute("contribution").toString()));
                else if (child.attribute("contrib") != null)
                    contribs.add(new Contributor(name, child.attribute("contrib").toString()));
                else {
                    contribs.add(new Contributor(name));
                }
            }
        }
        return contribs;
    }
}
