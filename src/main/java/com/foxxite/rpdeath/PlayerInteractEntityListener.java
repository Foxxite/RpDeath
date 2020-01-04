package com.foxxite.rpdeath;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntityDestroy;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.inventivetalent.glow.GlowAPI;

import static com.foxxite.rpdeath.Common.sendPacket;
import static com.foxxite.rpdeath.RPDeath.deadPlayers;
import static com.foxxite.rpdeath.RPDeath.fakeDeadPlayers;

public class PlayerInteractEntityListener implements Listener {

    private final RPDeath plugin;

    public PlayerInteractEntityListener(RPDeath plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEntityEvent event)
    {
        if(event.getRightClicked() instanceof Player)
        {
            if(event.getPlayer() == event.getRightClicked())
            {
                event.setCancelled(true);
            }
            /*
            else if(deadPlayers.containsKey(event.getRightClicked().getUniqueId()) || fakeDeadPlayers.containsKey(event.getRightClicked()))
            {
                if(event.getPlayer().getInventory().getItemInMainHand().getType() == Material.MUSHROOM_STEW)
                {
                    Player deadPlayer = (Player) event.getRightClicked();
                    String message = "§7§lYOU HAVE BEEN REVIVED BY " + event.getPlayer().getDisplayName();
                    deadPlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));

                    deadPlayer.setGameMode(GameMode.SURVIVAL);
                    deadPlayer.setHealth(20);
                    GlowAPI.setGlowing(deadPlayer, false, Bukkit.getOnlinePlayers());

                    sendPacket(deadPlayer.getWorld(), new PacketPlayOutEntityDestroy(fakeDeadPlayers.get(deadPlayer).getId()));

                    deadPlayers.remove(deadPlayer);
                    fakeDeadPlayers.remove(deadPlayer);

                    Player player = (Player) event.getPlayer();
                    message = "§7§lYOU HAVE REVIVED " + deadPlayer.getDisplayName();
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                }
            }
            */

            System.out.println("Entity " + event.getRightClicked().toString() + " right clicked by: " + event.getPlayer().getDisplayName());

        }
    }
}
