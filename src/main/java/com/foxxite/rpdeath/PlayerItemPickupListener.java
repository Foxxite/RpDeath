package com.foxxite.rpdeath;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.inventivetalent.glow.GlowAPI;

import static com.foxxite.rpdeath.Common.sendPacket;
import static com.foxxite.rpdeath.RPDeath.deadPlayers;
import static com.foxxite.rpdeath.RPDeath.fakeDeadPlayers;

public class PlayerItemPickupListener implements Listener {

    private final RPDeath plugin;

    public PlayerItemPickupListener(RPDeath plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemPickup(EntityPickupItemEvent event)
    {
        if(event.getEntity() instanceof Player)
        {
            Player player = (Player) event.getEntity();
            System.out.println("Player " + player.getDisplayName() + " picked up item: " + event.getItem().getItemStack().getType());

            if(deadPlayers.containsKey(player.getUniqueId()))
            {
                if (event.getItem().getItemStack().getType() == Material.ENCHANTED_GOLDEN_APPLE || event.getItem().getItemStack().getType() == Material.GOLDEN_APPLE) {
                    event.getItem().getItemStack().setType(Material.AIR);

                    String message = "§7§lYOU HAVE BEEN REVIVED!";
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));

                    Location fixedLocation = player.getLocation();
                    fixedLocation.setY(fixedLocation.getY() + 1);

                    //player.teleport(fixedLocation);

                    player.removePotionEffect(PotionEffectType.BLINDNESS);
                    player.removePotionEffect(PotionEffectType.NIGHT_VISION);

                    GlowAPI.setGlowing(player, false, Bukkit.getOnlinePlayers());

                    sendPacket(player.getWorld(), new PacketPlayOutEntityDestroy(fakeDeadPlayers.get(player.getUniqueId()).getId()));

                    CraftPlayer cPlayer = (CraftPlayer) player;
                    EntityPlayer entityPlayer = cPlayer.getHandle();

                    //PacketPlayOutNamedEntitySpawn packetPlayOutNamedEntitySpawn = new PacketPlayOutNamedEntitySpawn(entityPlayer);
                    //PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer);
                    //sendPacket(player.getWorld(), info, packetPlayOutNamedEntitySpawn);

                    PacketPlayOutCamera spec = new PacketPlayOutCamera(entityPlayer);
                    entityPlayer.playerConnection.sendPacket(spec);

                    player.setGameMode(GameMode.SURVIVAL);
                    player.setHealth(20);

                    deadPlayers.remove(player.getUniqueId());
                    fakeDeadPlayers.remove(player.getUniqueId());
                }
            }
        }
    }
}
