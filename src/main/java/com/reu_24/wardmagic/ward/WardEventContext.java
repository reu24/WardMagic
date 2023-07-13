package com.reu_24.wardmagic.ward;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class WardEventContext {

    public final int level;
    public final World world;
    public final PlayerEntity player;
    public final ItemStack item;

    public WardEventContext(int level, PlayerEntity player, ItemStack item) {
        this.level = level;
        this.world = player.world;
        this.player = player;
        this.item = item;
    }
}
