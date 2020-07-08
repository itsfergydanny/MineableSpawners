package com.dnyferguson.mineablespawners.api;

import com.cryptomorin.xseries.XMaterial;
import com.dnyferguson.mineablespawners.MineableSpawners;
import com.dnyferguson.mineablespawners.utils.Chat;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class API {
    private MineableSpawners plugin;

    public API(MineableSpawners plugin) {
        this.plugin = plugin;
    }

    private String findInLore(List<String> lore){
        List<String> cLore = plugin.getConfigurationHandler().getList("global", "lore");
        for(int i = 0; i< cLore.size(); i++){
            String line = cLore.get(i);
            if(line.contains("%mob%")){
                String rest = line.replaceAll("&.", "");
                String prefix = rest.split("%mob%")[0];
                String suffix = rest.split("%mob%")[1];
                return StringUtils.substringBetween(lore.get(i).replaceAll("§.", ""), prefix, suffix).toUpperCase();
            }
        }
        return null;
    }

    public EntityType getEntityTypeFromItemStack(ItemStack item) {
        EntityType entityType = null;


        System.out.println(item.getItemMeta().getLore());
        System.out.println(plugin.getConfigurationHandler().getList("global", "lore"));
        if(plugin.getConfigurationHandler().getBoolean("global", "lore-enabled")){
            try {
            return EntityType.valueOf(findInLore(item.getItemMeta().getLore()));
            } catch (Exception ignore) {
                return null; //we shouldn't ingore this for security reasons
            }
        }

        // v1 compatibility
        try {
            entityType = EntityType.valueOf(item.getItemMeta().getLore().toString().split(": §7")[1].split("]")[0].toUpperCase());
        } catch (Exception ignore) {}

        // v2 compatibility
        if (entityType == null) {
            try {
                entityType = EntityType.valueOf(ChatColor.stripColor(item.getItemMeta().getDisplayName()).split(" Spawner")[0].replace("[", "").replace(" ", "_").toUpperCase());
            } catch (Exception ignore) {}
        }

        // v3 compatibility
        if (entityType == null) {
            try {
                entityType = plugin.getNmsHandler().getType(item);
            } catch (Exception ignore) {}
        }

        return entityType;
    }

    public ItemStack getSpawnerFromEntityName(String entityName) {
        EntityType entityType = EntityType.valueOf(entityName.toUpperCase().replace(" ","_"));
        return getSpawnerFromEntityType(entityType);
    }

    public ItemStack getSpawnerFromEntityType(EntityType entityType) {
        ItemStack item = new ItemStack(Objects.requireNonNull(XMaterial.SPAWNER.parseMaterial()));
        ItemMeta meta = item.getItemMeta();

        String mobFormatted = Chat.uppercaseStartingLetters(entityType.name().toString());
        meta.setDisplayName(plugin.getConfigurationHandler().getMessage("global", "name").replace("%mob%", mobFormatted));
        List<String> newLore = new ArrayList<>();
        if (plugin.getConfigurationHandler().getList("global", "lore") != null && plugin.getConfigurationHandler().getBoolean("global", "lore-enabled")) {
            for (String line : plugin.getConfigurationHandler().getList("global", "lore")) {
                newLore.add(Chat.format(line).replace("%mob%", mobFormatted));
            }
            meta.setLore(newLore);
        }
        item.setItemMeta(meta);

        return plugin.getNmsHandler().setType(item, entityType);
    }
}
