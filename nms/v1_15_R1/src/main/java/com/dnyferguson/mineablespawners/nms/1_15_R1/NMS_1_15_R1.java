package com.dnyferguson.mineablespawners.nms;

import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NBTTagString;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class NMS_1_15_R1 implements com.dnyferguson.mineablespawners.nms.NMS_Handler {

    @Override
    public ItemStack setType(ItemStack itemStack, EntityType type) {
        net.minecraft.server.v1_15_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nmsItemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        if (nmsItemCompound == null) {
            return null;
        }
        nmsItemCompound.set("ms_mob", NBTTagString.a(type.name()));
        nmsItem.setTag(nmsItemCompound);

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public EntityType getType(ItemStack itemStack) {
        net.minecraft.server.v1_15_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nmsItemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        if (nmsItemCompound == null) {
            return null;
        }

        String mob = nmsItemCompound.getString("ms_mob");

        EntityType entity = null;
        try {
            entity = EntityType.valueOf(mob);
        } catch (Exception ignore) {
            throw new NullPointerException("Invalid mob");
        }

        return entity;
    }

    @Override
    public boolean hasType(ItemStack itemStack) {
        net.minecraft.server.v1_15_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
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
