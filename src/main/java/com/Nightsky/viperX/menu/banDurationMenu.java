package com.Nightsky.viperX.menu;

import com.Nightsky.viperX.main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class banDurationMenu implements Listener {
    private static final String GUI_TITLE = ChatColor.DARK_AQUA + "Ban Durations";
    private static final int MAX_SIZE = 54; // 9x6 is the maximum allowed

    public static void open(Player player) {
        FileConfiguration config = main.getPlugin().getConfig();
        List<String> durations = config.getStringList("global.ban-durations.duration");

        // Ensure we have space for the emerald block by reserving one slot
        int needed = durations.size() + 1;
        int size = ((needed + 8) / 9) * 9; // round up to full row
        size = Math.min(size, MAX_SIZE);

        Inventory gui = Bukkit.createInventory(null, size, GUI_TITLE);

        int index = 0;
        for (String duration : durations) {
            if (index >= size - 1) {
                // Too many durations to fit, stop and warn
                player.sendMessage(ChatColor.RED + "No way you need this many durations!! (Too many durations to display!) Max supported: " + (size - 1));
                break;
            }

            gui.setItem(index, createMenuItem(
                    Material.PAPER,
                    ChatColor.AQUA + duration,
                    ChatColor.GRAY + "Click to remove this duration"
            ));
            index++;
        }

        // Place emerald block safely in last slot
        gui.setItem(size - 1, createMenuItem(
                Material.EMERALD_BLOCK,
                ChatColor.GREEN + "Add New Duration",
                ChatColor.GRAY + "Click to add via chat"
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

        FileConfiguration config = main.getPlugin().getConfig();
        List<String> durations = new ArrayList<>(config.getStringList("global.ban-durations.duration"));
        Material clicked = event.getCurrentItem().getType();

        if (clicked == Material.PAPER) {
            String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            if (durations.remove(name)) {
                config.set("global.ban-durations.duration", durations);
                main.getPlugin().saveConfig();
                main.getPlugin().reloadConfig();

                player.sendMessage(ChatColor.RED + "Removed duration: " + name);

                Bukkit.getScheduler().runTask(main.getPlugin(), () -> open(player));
            }
        } else if (clicked == Material.EMERALD_BLOCK) {
            player.closeInventory();
            settingsMenu.startAwaitingDurationInput(player);
        }
    }
}
