package com.aefonix.compasstracker;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class InventoryManager implements Listener {
  public String itemName = ChatColor.RED + "Compass Tracker" + ChatColor.GRAY + " (Right Click)";
  private final CompassTracker compassTracker;

  public InventoryManager(CompassTracker compassTracker) {
    this.compassTracker = compassTracker;
  }

  @EventHandler
  public void onPlayerRespawn(PlayerRespawnEvent event) {
    Player player = event.getPlayer();

    if (compassTracker.tracking.containsKey(player)) {
      giveCompass(player);
    }
  }

  public void giveCompass(Player player) {
    ItemStack compass = new ItemStack(Material.COMPASS);
    ItemMeta compassMeta = compass.getItemMeta();

    compassMeta.setDisplayName(itemName);

    List<String> compassLore = Lists.newArrayList();
    compassLore.add(
      ChatColor.GRAY
        + "Tracking:"
        + ChatColor.WHITE
        + " N/A"
    );

    compassMeta.setLore(compassLore);
    compass.setItemMeta(compassMeta);

    player.getInventory().addItem(compass);
  }

  public void removeCompass(Player player) {
    for (ItemStack item : player.getInventory().getContents()) {
      if (item != null && item.getType() == Material.COMPASS) {
        if (item.getItemMeta().getDisplayName().equals(itemName)) {
          player.getInventory().remove(item);
        }
      }
    }
  }

  public void updateCompass(Player player, String target) {
    for (ItemStack item : player.getInventory().getContents()) {
      if (item != null && item.getType() == Material.COMPASS) {
        if (item.getItemMeta().getDisplayName().equals(itemName)) {
          ItemMeta compassMeta = item.getItemMeta();

          List<String> compassLore = Lists.newArrayList();
          compassLore.add(
            ChatColor.GRAY
              + "Tracking: "
              + ChatColor.WHITE
              + target
          );

          compassMeta.setLore(compassLore);
          item.setItemMeta(compassMeta);

          player.updateInventory();
        }
      }
    }
  }
}
