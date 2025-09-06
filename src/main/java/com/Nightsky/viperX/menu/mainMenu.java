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

public class mainMenu implements Listener {
    private static final String GUI_TITLE = ChatColor.DARK_RED + "ViperX";

    public static void open(Player player) {
        Inventory gui = Bukkit.createInventory(null, 9, GUI_TITLE);

        gui.setItem(2, createMenuItem(Material.BEDROCK, ChatColor.DARK_GREEN + "Pending List"));
        gui.setItem(4, createMenuItem(Material.ANVIL, ChatColor.DARK_GREEN + "Settings"));
        gui.setItem(6, createMenuItem(Material.BOOK, ChatColor.DARK_GREEN + "Links"));

        player.openInventory(gui);
    }

    private static ItemStack createMenuItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Cross-version title check
        String title;
        try {
            // 1.14+ method
            title = event.getView().getTitle();
        } catch (NoSuchMethodError e) {
            // 1.8 – 1.13 method
            title = event.getInventory().getTitle();
        }

        if (title.equals(GUI_TITLE)) {
            event.setCancelled(true);

            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
                return;
            }

            Player player = (Player) event.getWhoClicked();
            Material clicked = event.getCurrentItem().getType();

            switch (clicked) {
                case BEDROCK:
                    if (player.hasPermission("viperx.pending")) {
                        pendingMenu.open(player);
                    } else {
                        player.sendMessage("§cYou do not have permission to open the Pending menu.");
                    }
                    break;
                case ANVIL:
                    if (player.hasPermission("viperx.settings")) {
                        settingsMenu.open(player);
                    } else {
                        player.sendMessage("§cYou do not have permission to open the Settings menu.");
                    }
                    break;
                case BOOK:
                    if (player.hasPermission("viperx.links")) {
                        linksMenu.open(player);
                    } else {
                        player.sendMessage("§cYou do not have permission to open the Links menu.");
                    }
                    break;
            }
        }
    }
}
