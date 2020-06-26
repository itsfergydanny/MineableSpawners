package com.dnyferguson.mineablespawners.listeners;

import com.cryptomorin.xseries.XMaterial;
import com.dnyferguson.mineablespawners.MineableSpawners;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class AnvilRenameListener implements Listener {
  private MineableSpawners plugin;

  public AnvilRenameListener(MineableSpawners plugin) {
    this.plugin = plugin;
  }

  @EventHandler (ignoreCancelled = true)
  public void onAnvilRename(InventoryClickEvent e) {
    if (e.getInventory() == null || e.getInventory().getType() == null) {
      return;
    }

    if (e.getCurrentItem() == null || e.getCurrentItem().getType() == null) {
      return;
    }

    if (e.getInventory().getType() != InventoryType.ANVIL || e.getCurrentItem().getType() != XMaterial.SPAWNER.parseMaterial()) {
      return;
    }

    if (!plugin.getConfigurationHandler().getBoolean("anvil", "prevent-anvil")) {
      return;
    }

    Player player = (Player) e.getWhoClicked();
    e.setCancelled(true);
    player.sendMessage(plugin.getConfigurationHandler().getMessage("anvil", "prevented"));
  }
}
