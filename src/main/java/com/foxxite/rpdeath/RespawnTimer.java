package com.foxxite.rpdeath;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_14_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_14_R1.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.inventivetalent.glow.GlowAPI;

import java.util.Date;
import java.util.Map;
import java.util.TimerTask;
import java.util.UUID;

import static com.foxxite.rpdeath.Common.sendPacket;
import static com.foxxite.rpdeath.RPDeath.*;

public class RespawnTimer extends TimerTask {

    private final RPDeath plugin;

    public RespawnTimer(RPDeath plugin) {
        this.plugin = plugin;
    }

    public void run()
    {
        if(deadPlayers.size() > 0)
        {
            for(Map.Entry<UUID, DeadPlayer> entry : deadPlayers.entrySet())
            {
                Player player = entry.getValue().player;
                DeadPlayer deadPlayer = entry.getValue();

                int ts = (int) (new Date().getTime()/1000);

                int respawnTime = deadPlayer.deathTime + 60;

                if(ts >= respawnTime)
                {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        String message = "§7§lYOU WILL BLEED DEATH IN: §C§l" + (respawnTime - ts) / 1000 + " seconds!";

                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                        player.setGameMode(GameMode.SURVIVAL);
                        player.setHealth(0);
                        GlowAPI.setGlowing(player, false, Bukkit.getOnlinePlayers());

                        sendPacket(player.getWorld(), new PacketPlayOutEntityDestroy(fakeDeadPlayers.get(player.getUniqueId()).getId()));

                        deadPlayers.remove(player.getUniqueId());
                        fakeDeadPlayers.remove(player.getUniqueId());
                    });
                }
                else
                {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        String message = "§7§lYOU WILL BLEED DEATH IN: §C§l" + (respawnTime - ts) + " seconds!";
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));

                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 300, 1), true);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 300, 1), true);
                    });
                }

            }
        }

    }

}
