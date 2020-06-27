package com.dnyferguson.mineablespawners.nms;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public interface NMS_Handler {


    ItemStack setType(ItemStack itemStack, EntityType type);

    EntityType getType(ItemStack itemStack);

    boolean hasType(ItemStack itemStack);
}
