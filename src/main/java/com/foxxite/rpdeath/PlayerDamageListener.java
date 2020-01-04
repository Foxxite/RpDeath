package com.foxxite.rpdeath;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.inventivetalent.glow.GlowAPI;

import java.util.Date;
import java.sql.Timestamp;
import java.util.UUID;

import static com.foxxite.rpdeath.Common.sendPacket;
import static com.foxxite.rpdeath.RPDeath.*;

public class PlayerDamageListener implements Listener {

    private final RPDeath plugin;

    public PlayerDamageListener(RPDeath plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event)
    {
        if(event.getEntity() instanceof Player)
        {
            Player player = (Player)event.getEntity();

            if(deadPlayers.containsKey(player.getUniqueId()))
            {
                event.setCancelled(true);
            }
            else if(event.getFinalDamage() >= player.getHealth())
            {
                event.setCancelled(true);
                player.setHealth(1);
                player.setGlowing(true);
                player.setGameMode(GameMode.SPECTATOR);

                Location fixedLocation = player.getLocation();
                fixedLocation.setY(fixedLocation.getY() - 0.0125f);

                //player.teleport(fixedLocation);

                GlowAPI.setGlowing(player, GlowAPI.Color.DARK_RED, Bukkit.getOnlinePlayers());

                MinecraftServer server = MinecraftServer.getServer();
                CraftPlayer cPlayer = (CraftPlayer) player;
                WorldServer world = ((CraftWorld) player.getWorld()).getHandle();
                GameProfile profile = cPlayer.getProfile();
                PlayerInteractManager interactManager = new PlayerInteractManager(world);
                EntityPlayer entityPlayer = cPlayer.getHandle();
                

                EntityPlayer fakeEntityPlayer = new EntityPlayer(server, world, profile, interactManager);
                fakeEntityPlayer.setPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
                PacketPlayOutNamedEntitySpawn packetPlayOutNamedEntitySpawn = new PacketPlayOutNamedEntitySpawn(fakeEntityPlayer);
                PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer);

                sendPacket(player.getWorld(),info, packetPlayOutNamedEntitySpawn);

                BlockPosition bedBlockPosition = new BlockPosition(entityPlayer);
                bedBlockPosition.down(1);
                fakeEntityPlayer.e(bedBlockPosition);
                PacketPlayOutEntityMetadata meta = new PacketPlayOutEntityMetadata(fakeEntityPlayer.getId(), fakeEntityPlayer.getDataWatcher(), false);
                sendPacket(player.getWorld(),meta);

                UUID uuid = UUID.randomUUID();
                fakeEntityPlayer.a(uuid);

                PacketPlayOutCamera spec = new PacketPlayOutCamera(fakeEntityPlayer);
                entityPlayer.playerConnection.sendPacket(spec);
                sendPacket(player.getWorld(), player,new PacketPlayOutEntityDestroy(entityPlayer.getId()));

                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 300, 1), true);
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 300, 1), true);

                int ts = (int) (new Date().getTime()/1000);
                DeadPlayer deadPlayer = new DeadPlayer(player, player.getLocation(), ts);

                player.setGameMode(GameMode.SURVIVAL);

                deadPlayers.put(player.getUniqueId(), deadPlayer);
                fakeDeadPlayers.put(player.getUniqueId(), fakeEntityPlayer);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamage(EntityDamageByEntityEvent event)
    {
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player)
        {
            Player otherPlayer = (Player)event.getDamager();
            if(deadPlayers.containsKey(otherPlayer.getUniqueId()))
            {
                event.setCancelled(true);
            }
        }

    }
}
