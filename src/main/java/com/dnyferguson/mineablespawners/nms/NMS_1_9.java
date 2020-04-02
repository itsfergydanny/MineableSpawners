package com.dnyferguson.mineablespawners.nms;

import net.minecraft.server.v1_9_R2.NBTTagCompound;
import net.minecraft.server.v1_9_R2.NBTTagString;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class NMS_1_9 implements NMS_Handler {
    public ItemStack setType(ItemStack itemStack, EntityType type) {
        net.minecraft.server.v1_9_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nmsItemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        if (nmsItemCompound == null) {
            return null;
        }
        nmsItemCompound.set("ms_mob", new NBTTagString(type.name()));
        nmsItem.setTag(nmsItemCompound);

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    public EntityType getType(ItemStack itemStack) {
        net.minecraft.server.v1_9_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nmsItemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        if (nmsItemCompound == null) {
            return null;
        }

        String mob = nmsItemCompound.getString("ms_mob");

        EntityType entity = EntityType.PIG;
        try {
            entity = EntityType.valueOf(mob);
        } catch (Exception ignore) {}

        return entity;
    }

    public boolean hasType(ItemStack itemStack) {
        net.minecraft.server.v1_9_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nmsItemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        if (nmsItemCompound == null) {
            return false;
        }

        try {
            String mob = nmsItemCompound.getString("ms_mob");
            return !mob.equals("");
        } catch (Exception ignore) {
            return false;
        }
    }
}