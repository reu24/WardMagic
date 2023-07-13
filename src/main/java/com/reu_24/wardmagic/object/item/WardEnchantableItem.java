package com.reu_24.wardmagic.object.item;

import com.reu_24.wardmagic.capability.WardEnchantmentsProvider;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.List;

public class WardEnchantableItem extends Item implements IWardEnchantableItem {

    protected final byte toolType;

    public WardEnchantableItem(Properties properties, byte toolType) {
        super(properties);
        this.toolType = toolType;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
        return new WardEnchantmentsProvider();
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        addTooltip(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ActionResult<ItemStack> actionResult = itemRightClick(worldIn, playerIn, handIn);
        if (actionResult.getType() != ActionResultType.PASS) {
            return actionResult;
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        return itemUse(context);
    }

    @Override
    public byte getToolType() {
        return toolType;
    }
}
