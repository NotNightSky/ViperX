package com.Nightsky.viperX.menu;

import com.Nightsky.viperX.main;
import com.Nightsky.viperX.utils.durationParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class settings implements Listener {
    private static final String GUI_TITLE = ChatColor.DARK_RED + "Settings Menu";
    private static final Map<UUID, Boolean> awaitingDurationInput = new HashMap<>();

    public static void open(Player player) {
        FileConfiguration config = main.getPlugin().getConfig();
        Inventory gui = Bukkit.createInventory(null, 9, GUI_TITLE);

        // Offline mode toggle
        boolean offlineWarning = config.getBoolean("offline-mode-warning");
        gui.setItem(2, createMenuItem(Material.REDSTONE_TORCH,
                ChatColor.RED + "Offline Mode Warning: "
                        + (offlineWarning ? ChatColor.GREEN + "Enabled" : ChatColor.GRAY + "Disabled")));

        // Grace period
        int grace = config.getInt("advancedban.ban-durations.grace-period");
        gui.setItem(4, createMenuItem(Material.CLOCK,
                ChatColor.GOLD + "Grace Period: " + grace + " ms" + ChatColor.GRAY + " (LMB -100 / RMB +100)"));

        // Ban durations
        List<String> durations = config.getStringList("advancedban.ban-durations.duration");
        gui.setItem(6, createMenuItem(Material.PAPER,
                ChatColor.AQUA + "Ban Durations: " + String.join(", ", durations),
                ChatColor.GRAY + "Click to add/remove"));

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
        Material clicked = event.getCurrentItem().getType();

        switch (clicked) {
            case REDSTONE_TORCH -> {
                boolean current = config.getBoolean("offline-mode-warning");
                config.set("offline-mode-warning", !current);
                main.getPlugin().saveConfig();
                player.sendMessage(ChatColor.YELLOW + "Offline Mode Warning set to " + !current);
                open(player);
            }
            case CLOCK -> {
                int grace = config.getInt("advancedban.ban-durations.grace-period");
                if (event.isLeftClick()) grace = Math.max(0, grace - 100);
                if (event.isRightClick()) grace += 100;
                config.set("advancedban.ban-durations.grace-period", grace);
                main.getPlugin().saveConfig();
                player.sendMessage(ChatColor.YELLOW + "Grace Period updated: " + grace + " ms");
                open(player);
            }
            case PAPER -> {
                player.closeInventory();
                awaitingDurationInput.put(player.getUniqueId(), true);
                player.sendMessage(ChatColor.AQUA + "Enter a duration (e.g., 1s, 5m, 2h, 1d) or type 'remove <value>' to delete:");
            }
        }
    }

    @EventHandler
    public void onChatInput(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!awaitingDurationInput.containsKey(player.getUniqueId())) return;

        event.setCancelled(true);
        String msg = event.getMessage();
        FileConfiguration config = main.getPlugin().getConfig();
        List<String> durations = new ArrayList<>(config.getStringList("advancedban.ban-durations.duration"));

        if (msg.toLowerCase().startsWith("remove ")) {
            String target = msg.substring(7).trim();
            if (durations.remove(target)) {
                config.set("advancedban.ban-durations.duration", durations);
                main.getPlugin().saveConfig();
                player.sendMessage(ChatColor.RED + "Removed duration: " + target);
            } else {
                player.sendMessage(ChatColor.GRAY + "Duration not found: " + target);
            }
        } else {
            try {
                // validate by parsing
                long millis = durationParser.parseToMillis(msg);
                if (millis > 0) {
                    durations.add(msg);
                    config.set("advancedban.ban-durations.duration", durations);
                    main.getPlugin().saveConfig();
                    player.sendMessage(ChatColor.GREEN + "Added duration: " + msg + " (" + millis + " ms)");
                } else {
                    player.sendMessage(ChatColor.RED + "Invalid duration: " + msg);
                }
            } catch (Exception e) {
                player.sendMessage(ChatColor.RED + "Invalid format! Use like 1s, 5m, 2h, 1d");
            }
        }

        awaitingDurationInput.remove(player.getUniqueId());
        Bukkit.getScheduler().runTask(main.getPlugin(), () -> open(player));
    }
}
