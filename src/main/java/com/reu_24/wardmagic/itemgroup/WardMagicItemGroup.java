package com.reu_24.wardmagic.itemgroup;

import com.reu_24.wardmagic.init.ItemInit;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class WardMagicItemGroup extends ItemGroup {

    public static final WardMagicItemGroup INSTANCE = new WardMagicItemGroup(ItemGroup.GROUPS.length, "ward_magic");

    private WardMagicItemGroup(int index, String label) {
        super(index, label);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ItemInit.MAGNIFYING_GLASS.get());
    }

}
