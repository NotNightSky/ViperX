package com.Nightsky.viperX.menu;

import com.Nightsky.viperX.main;
import com.Nightsky.viperX.utils.pendingManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class pendingMenu implements Listener {
    private static final String GUI_TITLE = ChatColor.DARK_PURPLE + "Pending";
    private static final int MAX_SIZE = 54;
    private static final Map<UUID, Boolean> awaitingInput = new HashMap<>();

    public static void open(Player player) {
        pendingManager manager = main.getPlugin().getPendingManager();
        Set<UUID> pending = manager.getAll();

        int needed = pending.size() + 1; // Reserve one slot for Add button
        int size = ((needed + 8) / 9) * 9;
        size = Math.min(size, MAX_SIZE);

        Inventory gui = Bukkit.createInventory(null, size, GUI_TITLE);

        int index = 0;
        for (UUID uuid : pending) {
            if (index >= size - 1) {
                player.sendMessage(ChatColor.RED + "Too many pending clears to display! Max: " + (size - 1));
                break;
            }

            OfflinePlayer offline = Bukkit.getOfflinePlayer(uuid);
            String name = offline.getName() != null ? offline.getName() : uuid.toString();

            gui.setItem(index, createMenuItem(
                    Material.PAPER,
                    ChatColor.AQUA + name,
                    ChatColor.GRAY + "Click to remove this pending clear"
            ));
            index++;
        }

        gui.setItem(size - 1, createMenuItem(
                Material.EMERALD_BLOCK,
                ChatColor.GREEN + "Add New Pending Clear",
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

        pendingManager manager = main.getPlugin().getPendingManager();
        Material clicked = event.getCurrentItem().getType();

        if (clicked == Material.PAPER) {
            String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            OfflinePlayer target = Bukkit.getOfflinePlayer(name);
            UUID uuid = target.getUniqueId();

            if (manager.isPending(uuid)) {
                manager.remove(uuid);
                manager.save();
                player.sendMessage(ChatColor.RED + "Removed pending clear: " + name);

                Bukkit.getScheduler().runTask(main.getPlugin(), () -> open(player));
            }
        } else if (clicked == Material.EMERALD_BLOCK) {
            player.closeInventory();
            awaitingInput.put(player.getUniqueId(), true);
            player.sendMessage(ChatColor.AQUA + "Enter the player name or UUID to add to pending clears:");
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!awaitingInput.containsKey(player.getUniqueId())) return;

        event.setCancelled(true);
        awaitingInput.remove(player.getUniqueId());

        String input = event.getMessage();

        Bukkit.getScheduler().runTask(main.getPlugin(), () -> {
            try {
                UUID uuid;
                if (input.length() == 36) {
                    uuid = UUID.fromString(input);
                } else {
                    uuid = Bukkit.getOfflinePlayer(input).getUniqueId();
                }

                pendingManager manager = main.getPlugin().getPendingManager();
                manager.add(uuid);
                manager.save();

                player.sendMessage(ChatColor.GREEN + "Added " + input + " to pending clears.");
            } catch (Exception e) {
                player.sendMessage(ChatColor.RED + "Invalid input. Use a valid name or UUID.");
            }

            open(player);
        });
    }
}
