package com.Nightsky.viperX.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class linksMenu implements Listener {
    private static final String GUI_TITLE = ChatColor.DARK_BLUE + "Useful Links";

    public static void open(Player player) {
        Inventory gui = Bukkit.createInventory(null, 9, GUI_TITLE);

        gui.setItem(2, createMenuItem(
                Material.BOOK,
                ChatColor.GREEN + "GitHub",
                ChatColor.GRAY + "Click to get the GitHub link"
        ));

        gui.setItem(4, createMenuItem(
                Material.PAPER,
                ChatColor.RED + "YouTube",
                ChatColor.GRAY + "Click to get the YouTube link"
        ));

        gui.setItem(6, createMenuItem(
                Material.BOOKSHELF,
                ChatColor.AQUA + "Wiki",
                ChatColor.GRAY + "Click to get the Wiki link"
        ));

        player.openInventory(gui);
    }

    private static ItemStack createMenuItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore.length > 0) meta.setLore(Arrays.asList(lore));
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!event.getView().getTitle().equals(GUI_TITLE)) return;

        event.setCancelled(true);
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        Material clicked = event.getCurrentItem().getType();

        switch (clicked) {
            case BOOK -> {
                player.closeInventory();
                player.sendMessage(ChatColor.GREEN + "GitHub: " + ChatColor.UNDERLINE + "https://github.com/xmoderlive");
            }
            case PAPER -> {
                player.closeInventory();
                player.sendMessage(ChatColor.RED + "YouTube: " + ChatColor.UNDERLINE + "https://youtube.com/@SkysVaultWasTaken");
            }
            case BOOKSHELF -> {
                player.closeInventory();
                player.sendMessage(ChatColor.AQUA + "Wiki: " + ChatColor.UNDERLINE + "https://github.com/xmoderlive/ViperX/wiki");
            }
        }
    }
}
