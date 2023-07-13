package com.reu_24.wardmagic.object.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FurnaceBurnableItem extends Item {
    protected final int burnTime;

    public FurnaceBurnableItem(Properties properties, int burnTime) {
        super(properties);
        this.burnTime = burnTime;
    }

    @Override
    public int getBurnTime(ItemStack itemStack) {
        return burnTime;
    }
}
