package com.foxxite.rpdeath;

import net.minecraft.server.v1_14_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Timer;
import java.util.UUID;

public class RPDeath extends JavaPlugin {


    private PlayerDamageListener playerDamageListener;
    private PlayerEatListener playerEatListener;
    private PlayerMovementListener playerMovementListener;
    private PlayerInteractEntityListener playerInteractEntityListener;
    private PlayerItemPickupListener playerItemPickupListener;

    private Timer timer = new Timer();

    public static final HashMap<UUID, DeadPlayer> deadPlayers = new HashMap<UUID, DeadPlayer>();
    public static final HashMap<UUID, EntityPlayer> fakeDeadPlayers = new HashMap<UUID, EntityPlayer>();

    @Override
    public void onEnable() {
        System.out.println("Foxxite's RP Death plugin enabled");

        this.playerDamageListener = new PlayerDamageListener(this);
        this.playerEatListener = new PlayerEatListener();
        this.playerMovementListener = new PlayerMovementListener();
        this.playerInteractEntityListener = new PlayerInteractEntityListener(this);
        this.playerItemPickupListener = new PlayerItemPickupListener(this);

        this.getServer().getPluginManager().registerEvents(this.playerDamageListener, this);
        this.getServer().getPluginManager().registerEvents(this.playerEatListener, this);
        this.getServer().getPluginManager().registerEvents(this.playerMovementListener, this);
        this.getServer().getPluginManager().registerEvents(this.playerItemPickupListener, this);

        this.timer.schedule(new RespawnTimer(this), 0, 1000);
    }

    @Override
    public void onDisable() {
        System.out.println("Foxxite's RP Death plugin plugin disabled");

        this.playerDamageListener = null;
        this.playerEatListener = null;
        this.playerMovementListener = null;
        this.playerInteractEntityListener = null;
        this.playerItemPickupListener = null;

        this.timer = null;
    }

    public PlayerDamageListener getPlayerDamageListener() {
        return this.playerDamageListener;
    }

    public void setPlayerDamageListener(final PlayerDamageListener playerDamageListener) {
        this.playerDamageListener = playerDamageListener;
    }
}
