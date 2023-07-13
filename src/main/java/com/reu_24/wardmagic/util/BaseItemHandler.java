package com.reu_24.wardmagic.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class BaseItemHandler extends ItemStackHandler {

    public BaseItemHandler(int size, ItemStack... stacks) {
        super(size);

        for (int i = 0; i < stacks.length; i++) {
            this.stacks.set(i, stacks[i]);
        }
    }

    public void clear() {
        for (int i = 0; i< getSlots(); i++) {
            stacks.set(i, ItemStack.EMPTY);
            onContentsChanged(i);
        }
    }

    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = getStackInSlot(index);
        stack.shrink(count);
        this.onContentsChanged(index);
        return stack;
    }

    public void removeStackFromSlot(int index) {
        this.stacks.set(index, ItemStack.EMPTY);
        this.onContentsChanged(index);
    }

    public NonNullList<ItemStack> toNonNullList() {
        NonNullList<ItemStack> items = NonNullList.create();
        items.addAll(this.stacks);
        return items;
    }

    public void setNonNullList(NonNullList<ItemStack> items) {
        if (items.size() == 0)
            return;
        if (items.size() != this.getSlots())
            throw new IllegalArgumentException("NonNullList must be same size as ItemStackHandler!");
        for (int index = 0; index < items.size(); index++) {
            this.stacks.set(index, items.get(index));
        }
    }

    @Override
    public String toString() {
        return this.stacks.toString();
    }

}
