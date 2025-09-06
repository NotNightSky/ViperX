package com.Nightsky.viperX.menu;

import com.Nightsky.viperX.main;
import com.Nightsky.viperX.utils.durationParser;
import com.Nightsky.viperX.utils.inventoryUtils;
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

public class settingsMenu implements Listener {
    private static final String GUI_TITLE = ChatColor.DARK_RED + "Settings Menu";
    private static final Map<UUID, Boolean> awaitingDurationInput = new HashMap<>();

    public static void startAwaitingDurationInput(Player player) {
        awaitingDurationInput.put(player.getUniqueId(), true);
        player.sendMessage(ChatColor.AQUA +
                "Enter a duration (e.g., 1s, 5m, 2h, 1d) or type 'remove <value>' to delete.");
    }

    public static void open(Player player) {
        if (!player.hasPermission("viperx.settings")) {
            player.sendMessage("§cYou do not have permission to open the Settings menu.");
            return;
        }

        FileConfiguration config = main.getPlugin().getConfig();
        Inventory gui = Bukkit.createInventory(null, 9, GUI_TITLE);

        // Offline mode toggle
        boolean offlineWarning = config.getBoolean("offline-mode-warning");
        gui.setItem(2, createMenuItem(Material.REDSTONE_BLOCK,
                ChatColor.RED + "Offline Mode Warning: "
                        + (offlineWarning ? ChatColor.GREEN + "Enabled" : ChatColor.GRAY + "Disabled")));

        // Grace period
        int grace = config.getInt("global.ban-durations.grace-period");
        gui.setItem(4, createMenuItem(Material.DIAMOND,
                ChatColor.GOLD + "Grace Period: " + grace + " ms" + ChatColor.GRAY + " (LMB -100 / RMB +100)"));

        // Ban durations link
        gui.setItem(6, createMenuItem(Material.PAPER,
                ChatColor.AQUA + "Ban Durations",
                ChatColor.GRAY + "Click to manage"));

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
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (!inventoryUtils.getTitle(event).equals(GUI_TITLE)) return;

        event.setCancelled(true);
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        FileConfiguration config = main.getPlugin().getConfig();
        Material clicked = event.getCurrentItem().getType();

        switch (clicked) {
            case REDSTONE_BLOCK:
                boolean current = config.getBoolean("offline-mode-warning");
                config.set("offline-mode-warning", !current);
                main.getPlugin().saveConfig();
                player.sendMessage(ChatColor.YELLOW + "Offline Mode Warning set to " + !current);
                open(player);
                break;
            case DIAMOND:
                int grace = config.getInt("global.ban-durations.grace-period");
                if (event.isLeftClick()) grace = Math.max(0, grace - 100);
                if (event.isRightClick()) grace += 100;
                config.set("global.ban-durations.grace-period", grace);
                main.getPlugin().saveConfig();
                player.sendMessage(ChatColor.YELLOW + "Grace Period updated: " + grace + " ms");
                open(player);
                break;
            case PAPER:
                banDurationMenu.open(player);
                break;
            default:
                break;
        }
    }

    @EventHandler
    public void onChatInput(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!awaitingDurationInput.containsKey(player.getUniqueId())) return;

        event.setCancelled(true);
        String msg = event.getMessage().trim();
        FileConfiguration config = main.getPlugin().getConfig();
        List<String> durations = new ArrayList<>(config.getStringList("global.ban-durations.duration"));

        if (msg.toLowerCase().startsWith("remove ")) {
            String target = msg.substring(7).trim();
            if (durations.remove(target)) {
                config.set("global.ban-durations.duration", durations);
                main.getPlugin().saveConfig();
                main.getPlugin().reloadConfig();
                player.sendMessage(ChatColor.RED + "Removed duration: " + target);
            } else {
                player.sendMessage(ChatColor.GRAY + "Duration not found: " + target);
            }
        } else {
            try {
                long millis = durationParser.parseToMillis(msg);
                if (millis > 0) {
                    durations.add(msg);
                    config.set("global.ban-durations.duration", durations);
                    main.getPlugin().saveConfig();
                    main.getPlugin().reloadConfig();
                    player.sendMessage(ChatColor.GREEN + "Added duration: " + msg + " (" + millis + " ms)");
                } else {
                    player.sendMessage(ChatColor.RED + "Invalid duration: " + msg);
                }
            } catch (Exception e) {
                player.sendMessage(ChatColor.RED + "Invalid format! Use like 1s, 5m, 2h, 1d");
            }
        }

        awaitingDurationInput.remove(player.getUniqueId());

        // reopen ban duration menu safely
        Bukkit.getScheduler().runTask(main.getPlugin(), () -> banDurationMenu.open(player));
    }
}
