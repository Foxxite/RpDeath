package com.foxxite.rpdeath;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.Timestamp;

public class DeadPlayer {

    public Player player;
    public Location location;
    public int deathTime;

    public DeadPlayer(Player player, Location location, int deathTime)
    {
        this.player = player;
        this.location = location;
        this.deathTime = deathTime;
    }

}
