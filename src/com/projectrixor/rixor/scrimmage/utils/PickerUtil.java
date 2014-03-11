package com.projectrixor.rixor.scrimmage.utils;

import java.util.Arrays;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import com.projectrixor.rixor.scrimmage.Scrimmage;
import com.projectrixor.rixor.scrimmage.map.Map;
import com.projectrixor.rixor.scrimmage.map.MapTeam;
import com.projectrixor.rixor.scrimmage.match.Match;

public class PickerUtil {
	@Getter Map map;
    public static Inventory obsInvPreview(String string) {
    	final Inventory preview = Bukkit.getServer().createInventory(null, 45, ChatColor.RED + string);    
    	for (MapTeam teams : Scrimmage.getMap().getTeams()) {
    		int i = 0;
        	ItemStack wow = new ItemStack(Material.WOOL, 1, (short) 1);
        	ItemMeta meta = wow.getItemMeta();
        	meta.setDisplayName(teams.getName());
        	meta.setLore(Arrays.asList(ChatColor.BLUE + "Join the" + teams.getColor() + " " + teams.getDisplayName() + ChatColor.BLUE + "!"));
        	wow.setItemMeta(meta);
        	preview.addItem(wow);
        	i = i + 1;
    	}
    	return preview;
    }
   /* staffhead = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
    ItemMeta sheadmeta = staffhead.getItemMeta();
    sheadmeta.setDisplayName(ChatColor.GOLD + "Staff");
    sheadmeta.setLore(Arrays.asList(ChatColor.BLUE + "A list of all staff members!"));
    staffhead.setItemMeta(sheadmeta);*/
    public static int getInventoryPreviewSlot(final int inventorySlot) {
        if (inventorySlot < 36) {
            return inventorySlot;
        }
        return inventorySlot;
        }
	
}
