package com.aefonix.compasstracker;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class InterfaceHandler implements Listener {
  private final CompassTracker compassTracker;

  public InterfaceHandler(CompassTracker compassTracker) {
    this.compassTracker = compassTracker;
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    Action action = event.getAction();
    ItemStack item = event.getItem();

    if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
      if (item != null && item.getType() == Material.COMPASS) {
        if (item.getItemMeta().getDisplayName().equals(compassTracker.inventoryManager.itemName)) {
          Inventory trackerInterface = renderInventory(player);
          player.openInventory(trackerInterface);
        }
      }
    }
  }

  @EventHandler
  public void onInventoryInteract(InventoryClickEvent event) {
    Player player = (Player) event.getWhoClicked();
    ItemStack item = event.getCurrentItem();
    Inventory inventory = event.getInventory();

    if (inventory.getName().equals(ChatColor.RED + "Compass Tracker")) {
      if (item.getType() == Material.SKULL_ITEM) {
        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        Player target = compassTracker.getServer().getPlayer(skullMeta.getOwner());

        compassTracker.tracking.put(player, target);
        compassTracker.inventoryManager.updateCompass(player, target.getDisplayName());
        player.sendMessage(ChatColor.YELLOW + "[#]" + ChatColor.GRAY + " Now tracking " + ChatColor.WHITE + target.getDisplayName());
        player.closeInventory();
      }

      if (item.getType() == Material.BARRIER) {
        compassTracker.tracking.put(player, null);
        compassTracker.inventoryManager.updateCompass(player, "N/A");
        player.sendMessage(ChatColor.RED + "[#]" + ChatColor.GRAY + " Stopped tracking");
        player.closeInventory();
      }

      event.setCancelled(true);
    }
  }

  public Inventory renderInventory(Player player) {
    Inventory inventory = Bukkit.createInventory(player, 54, ChatColor.RED + "Compass Tracker");

    ItemStack border = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
    ItemMeta borderMeta = border.getItemMeta();

    borderMeta.setDisplayName(" ");
    border.setItemMeta(borderMeta);

    // top row
    for (int i = 0; i <= 8; i++) {
      inventory.setItem(i, border);
    }
    // right column
    for (int i = 8; i <= 53; i += 9) {
      inventory.setItem(i, border);
    }
    // bottom row
    for (int i = 53; i >= 45; i--) {
      inventory.setItem(i, border);
    }
    // left column
    for (int i = 45; i >= 0; i -= 9) {
      inventory.setItem(i, border);
    }

    ItemStack selectNone = new ItemStack(Material.BARRIER);
    ItemMeta selectNoneMeta = selectNone.getItemMeta();
    List<String> noneLore = Lists.newArrayList();

    selectNoneMeta.setDisplayName(ChatColor.RED + "None");
    noneLore.add(ChatColor.GRAY + "Stop Tracking");
    selectNoneMeta.setLore(noneLore);
    selectNone.setItemMeta(selectNoneMeta);

    inventory.setItem(49, selectNone);

    int i = 10;
    for (Player onlinePlayer : compassTracker.getServer().getOnlinePlayers()) {
      if (player.equals(onlinePlayer)) {
        continue;
      }

      String name = onlinePlayer.getDisplayName();

      ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
      SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
      skullMeta.setOwner(name);
      skullMeta.setDisplayName(ChatColor.RESET + name);
      skull.setItemMeta(skullMeta);

      inventory.setItem(i, skull);

      if (i == 16 || i == 25 || i == 34) {
        i += 3;
      } else {
        i++;
      }
    }

    return inventory;
  }
}
