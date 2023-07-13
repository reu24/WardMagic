package com.reu_24.wardmagic.object.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class FurnaceBurnableBlockItem extends BlockItem {

    protected final int burnTime;

    public FurnaceBurnableBlockItem(Block blockIn, Properties builder, int burnTime) {
        super(blockIn, builder);
        this.burnTime = burnTime;
    }

    @Override
    public int getBurnTime(ItemStack itemStack) {
        return burnTime;
    }
}
