package com.Nightsky.viperX.utils;

public class inventoryUtils {
    public static String getTitle(org.bukkit.event.inventory.InventoryClickEvent event) {
        try {
            // 1.14+ method
            return (String) event.getView().getClass().getMethod("getTitle").invoke(event.getView());
        } catch (Exception e) {
            try {
                // 1.8 – 1.13 method
                return (String) event.getInventory().getClass().getMethod("getTitle").invoke(event.getInventory());
            } catch (Exception ex) {
                try {
                    // fallback for very old servers
                    return (String) event.getInventory().getClass().getMethod("getName").invoke(event.getInventory());
                } catch (Exception ignored) {
                    return "";
                }
            }
        }
    }
}
