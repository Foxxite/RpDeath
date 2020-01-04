package com.foxxite.rpdeath;

import net.minecraft.server.v1_14_R1.Packet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

public class Common {
    static void tell(final CommandSender sender, final String message) {
        sender.sendMessage(colorize(message));
    }

    private static String colorize(final String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    static void setRepairCost(final ItemStack itemStack, final int repairCost) {
        if (itemStack == null || itemStack.getType() == Material.AIR)
            return;

        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta instanceof Repairable) {
            final Repairable repairable = (Repairable) itemMeta;
            repairable.setRepairCost(repairCost);
            itemStack.setItemMeta(itemMeta);
        }
    }

    static int getRepairCost(final ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR)
            return -1;

        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta instanceof Repairable) {
            final Repairable repairable = (Repairable) itemMeta;
            return repairable.getRepairCost();
        }
        return -1;
    }

    public static void sendPacket(org.bukkit.World w, Packet<?>... packets) {
        Bukkit.getOnlinePlayers().stream().filter(cur -> ((Player) cur).getWorld().equals(w)).forEach(cur -> {
            for(Packet p: packets)
                ((CraftPlayer)cur).getHandle().playerConnection.sendPacket(p);
        });
    }

    public static void sendPacket(org.bukkit.World w, Player excluded, Packet<?>... packets) {
        Bukkit.getOnlinePlayers().stream()
                .filter(cur -> ((Player) cur).getWorld().equals(w))
                .filter(cur -> !((Player) cur).equals(excluded)).forEach(cur -> {
            for(Packet p: packets)
                ((CraftPlayer)cur).getHandle().playerConnection.sendPacket(p);
        });
    }

}