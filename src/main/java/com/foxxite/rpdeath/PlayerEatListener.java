package com.foxxite.rpdeath;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_14_R1.PacketPlayInClientCommand;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import static com.foxxite.rpdeath.RPDeath.deadPlayers;

public class PlayerEatListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event)
    {
        Player player = event.getPlayer();

        if(deadPlayers.containsKey(player.getUniqueId()))
        {
            event.setCancelled(true);
        }
        else if(event.getItem().getType() == Material.COOKED_BEEF)
        {
            String message = "§7§lYOU WILL BLEED DEATH IN: §C§lNOW!";
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));

            player.removePotionEffect(PotionEffectType.BLINDNESS);
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);

            player.setHealth(0);

            deadPlayers.remove(player.getUniqueId());
        }
    }
}
