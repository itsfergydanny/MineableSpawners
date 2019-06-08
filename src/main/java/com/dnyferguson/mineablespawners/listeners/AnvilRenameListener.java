package com.dnyferguson.mineablespawners.listeners;

import com.dnyferguson.mineablespawners.MineableSpawners;
import com.dnyferguson.mineablespawners.utils.Chat;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class AnvilRenameListener implements Listener {

  private boolean noAnvil;
  private String noAnvilMessage;

  public AnvilRenameListener(MineableSpawners plugin) {
    FileConfiguration config = plugin.getConfig();
    noAnvil = config.getBoolean("prevent-anvil");
    noAnvilMessage = config.getString("prevent-anvil-message");
  }

  @EventHandler
  public void onAnvilRename(InventoryClickEvent e) {
    if (e.getInventory().getType() != InventoryType.ANVIL || e.getCurrentItem().getType() != Material.SPAWNER) {
      return;
    }

    if (!noAnvil) {
      return;
    }

    Player player = (Player) e.getWhoClicked();
    e.setCancelled(true);
    player.sendMessage(Chat.format(noAnvilMessage));
  }
}
