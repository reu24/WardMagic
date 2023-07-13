package com.reu_24.wardmagic.util;

import com.reu_24.wardmagic.capability.WardEnchantmentsProvider;
import com.reu_24.wardmagic.object.item.IWardEnchantableItem;
import com.reu_24.wardmagic.object.item.ManaHolderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ManaHolderSlotItemHandler extends SlotItemHandler {
    public ManaHolderSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        return stack.getItem() instanceof ManaHolderItem;
    }
}
