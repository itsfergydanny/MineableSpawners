package com.dnyferguson.mineablespawners.nms;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public interface NMS_Handler {
    public ItemStack setType(ItemStack itemStack, EntityType type);
    public EntityType getType(ItemStack itemStack);
    public boolean hasType(ItemStack itemStack);
}
