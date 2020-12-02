package com.aefonix.compasstracker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CompassTracker extends JavaPlugin implements Listener {
  public CommandHandler commandHandler;
  public InterfaceHandler interfaceHandler;
  public InventoryManager inventoryManager;
  public Map<Player, Player> tracking = new HashMap<Player, Player>();
  public int task = 0;

  @Override
  public void onEnable() {
    commandHandler = new CommandHandler(this);
    interfaceHandler = new InterfaceHandler(this);
    inventoryManager = new InventoryManager(this);

    this.getServer().getPluginManager().registerEvents(inventoryManager, this);
    this.getServer().getPluginManager().registerEvents(interfaceHandler, this);
    this.getServer().getPluginManager().registerEvents(this, this);

    this.getCommand("tracker").setExecutor(commandHandler);

    task = this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
      @Override
      public void run() {
        updateTracking();
      }
    }, 0L, 20L);
  }

  @Override
  public void onDisable() {}

  public void updateTracking() {
    for (Map.Entry<Player, Player> entry : tracking.entrySet()) {
      Player player = entry.getKey();
      Player target = entry.getValue();

      if (target != null) {
        player.setCompassTarget(target.getLocation());
      } else {
        player.setCompassTarget(player.getWorld().getSpawnLocation());
      }
    }
  }
}
