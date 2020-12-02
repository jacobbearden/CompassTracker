package com.aefonix.compasstracker;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {
  private final CompassTracker compassTracker;

  public CommandHandler(CompassTracker compassTracker) {
    this.compassTracker = compassTracker;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender instanceof ConsoleCommandSender) {
      sender.sendMessage(ChatColor.RED + "[!]" + ChatColor.GRAY + " Cannot run as console");
      return true;
    }

    if (sender instanceof Player) {
      Player player = (Player) sender;

      if (compassTracker.tracking.containsKey(player)) {
        compassTracker.tracking.remove(player);
        player.setCompassTarget(player.getWorld().getSpawnLocation());

        compassTracker.inventoryManager.removeCompass(player);
        player.sendMessage(ChatColor.RED + "[-]" + ChatColor.GRAY + " Compass Tracker");
      } else {
        compassTracker.tracking.put((Player) sender, null);

        compassTracker.inventoryManager.giveCompass(player);
        player.sendMessage(ChatColor.GREEN + "[+]" + ChatColor.GRAY + " Compass Tracker");
      }
    }

    return true;
  }
}
