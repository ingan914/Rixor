package com.projectrixor.rixor.scrimmage.utils;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;
import org.dom4j.Element;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class XMLUtils {
    public static boolean parseBoolean(String text, boolean def)
    {
        if (text == null) {
            return def;
        }

        if (def) {
            return (!text.equalsIgnoreCase("no")) && (!text.equalsIgnoreCase("off")) && (!text.equalsIgnoreCase("false"));
        }
        return (text.equalsIgnoreCase("yes")) || (text.equalsIgnoreCase("on")) || (text.equalsIgnoreCase("true"));
    }

    public static List<Double> parseDoubleList(String text)
    {
        String[] pieces = text.split("[^o0-9\\.-]");
        List numbers = new LinkedList();
        for (String piece : pieces)
            try {
                double num = parseDouble(piece);
                numbers.add(Double.valueOf(num));
            }
            catch (NumberFormatException e) {
            }
        return numbers;
    }

    public static Vector parseVector(String text) {
        List numbers = parseDoubleList(text);
        if (numbers.size() >= 3) {
            return new Vector(((Double)numbers.get(0)).doubleValue(), ((Double)numbers.get(1)).doubleValue(), ((Double)numbers.get(2)).doubleValue());
        }
        return null;
    }

    public static double parseDouble(String s) throws NumberFormatException
    {
        if (s.equals("oo")) {
            return (1.0D / 0.0D);
        }
        if (s.equals("-oo")) {
            return (-1.0D / 0.0D);
        }
        return Double.parseDouble(s);
    }

    public static ChatColor parseChatColor(String text) {
        return parseChatColor(text, null);
    }

    public static ChatColor parseChatColor(String text, ChatColor defaultColor) {
        if (text == null) {
            return defaultColor;
        }
        for (ChatColor color : ChatColor.values()) {
            if (text.equalsIgnoreCase(color.name().replace("_", " "))) {
                return color;
            }
        }
        return defaultColor;
    }

    public static MaterialData parseMaterialData(String text) {
        String[] pieces = text.split(":");
        Material material = Material.matchMaterial(pieces[0]);
        if (material == null) {
            throw new IllegalArgumentException("Could not find material '" + pieces[0] + "'.");
        }
        byte data = 0;
        if (pieces.length > 1) {
            try {
                data = Byte.parseByte(pieces[1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid damage value: " + pieces[1], e);
            }
        }
        return material.getNewData(data);
    }

    public static Set<MaterialData> parseMaterialDataList(String text) {
        Set materials = Sets.newHashSet();
        for (String rawMat : Splitter.on(";").split(text)) {
            materials.add(parseMaterialData(rawMat.trim()));
        }
        return materials;
    }

    public static MaterialData parseBlockMaterial(String text) {
        MaterialData data = parseMaterialData(text);
        if (data.getItemType().isBlock()) {
            return data;
        }
        throw new IllegalArgumentException("Material must be a block.");
    }

    public static DyeColor parseDyeColor(String text)
    {
        for (DyeColor color : DyeColor.values()) {
            if (color.toString().replace("_", " ").equalsIgnoreCase(text)) {
                return color;
            }
        }
        return null;
    }

    public static Optional<Enchantment> parseEnchantment(String text) {
        return Optional.fromNullable(Enchantment.getByName(text.toUpperCase().replace(" ", "_")));
    }

    public static <T extends Enum<T>> T getEnumFromString(Class<T> c, String string)
    {
        return getEnumFromString(c, string, null);
    }

    public static <T extends Enum<T>> T getEnumFromString(Class<T> c, String string, T defaultValue) {
        if ((c != null) && (string != null))
            try {
                return Enum.valueOf(c, string.trim().toUpperCase().replace(' ', '_'));
            }
            catch (IllegalArgumentException ex)
            {
            }
        return defaultValue;
    }

    public static CreatureSpawnEvent.SpawnReason parseSpawnReason(String text) {
        for (CreatureSpawnEvent.SpawnReason reason : CreatureSpawnEvent.SpawnReason.values()) {
            if (text.equalsIgnoreCase(reason.toString().replace('_', ' '))) {
                return reason;
            }
        }
        return null;
    }

    public static Difficulty parseDifficulty(String text) {
        if (text == null) {
            return null;
        }
        for (Difficulty difficulty : Difficulty.values()) {
            if (text.equalsIgnoreCase(difficulty.toString())) {
                return difficulty;
            }
        }
        return null;
    }

    public static int parseNumericChild(Element el, String childName, int def) throws NumberFormatException {
        if (el.elements(childName) != null) {
            return parseNumericAttribute(el.element(childName).getText(), def);
        }
        return def;
    }

    public static double parseNumericChild(Element el, String childName, double def) throws NumberFormatException {
        if (el.element(childName) != null) {
            return parseNumericAttribute(el.element(childName).getText(), def);
        }
        return def;
    }

    public static float parseNumericChild(Element el, String childName, float def) throws NumberFormatException {
        if (el.element(childName) != null) {
            return parseNumericAttribute(el.element(childName).getText(), def);
        }
        return def;
    }

    public static int parseNumericAttribute(Element el, String attributeName, int def) throws NumberFormatException {
        return parseNumericAttribute(el.attributeValue(attributeName), def);
    }

    public static double parseNumericAttribute(Element el, String attributeName, double def) throws NumberFormatException {
        return parseNumericAttribute(el.attributeValue(attributeName), def);
    }

    public static float parseNumericAttribute(Element el, String attributeName, float def) throws NumberFormatException {
        return parseNumericAttribute(el.attributeValue(attributeName), def);
    }

    public static int parseNumericAttribute(String value, int def) throws NumberFormatException {
        if (value != null) {
            def = Integer.parseInt(value);
        }
        return def;
    }

    public static double parseNumericAttribute(String value, double def) throws NumberFormatException {
        if (value != null) {
            def = Double.parseDouble(value);
        }
        return def;
    }

    public static float parseNumericAttribute(String value, float def) throws NumberFormatException {
        if (value != null) {
            def = Float.parseFloat(value);
        }
        return def;
    }
}
