package com.dnyferguson.mineablespawners.nms;

import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.NBTTagString;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;

public class NMS_1_8_R1 implements NMS_Handler {

    @Override
    public org.bukkit.inventory.ItemStack setType(org.bukkit.inventory.ItemStack itemStack, org.bukkit.entity.EntityType type) {
        net.minecraft.server.v1_8_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nmsItemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        if (nmsItemCompound == null) {
            return null;
        }
        nmsItemCompound.set("ms_mob", new NBTTagString(type.name()));
        nmsItem.setTag(nmsItemCompound);

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public org.bukkit.entity.EntityType getType(org.bukkit.inventory.ItemStack itemStack) {
        net.minecraft.server.v1_8_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nmsItemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        if (nmsItemCompound == null) {
            return null;
        }

        String mob = nmsItemCompound.getString("ms_mob");

        EntityType entity = null;
        try {
            entity = EntityType.valueOf(mob);
        } catch (Exception ignore) {}

        return entity;
    }

    @Override
    public boolean hasType(org.bukkit.inventory.ItemStack itemStack) {
        net.minecraft.server.v1_8_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
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
