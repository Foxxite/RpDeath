package com.foxxite.rpdeath;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static com.foxxite.rpdeath.RPDeath.deadPlayers;

public class PlayerMovementListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMovement(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        Location loc = player.getLocation();

        if(deadPlayers.containsKey(player.getUniqueId()))
        {
            player.teleport(deadPlayers.get(player.getUniqueId()).location);
        }

    }
}
