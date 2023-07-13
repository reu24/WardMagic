package com.reu_24.wardmagic.object.item;

import com.reu_24.wardmagic.capability.WardEnchantment;
import com.reu_24.wardmagic.capability.WardEnchantmentsProvider;
import com.reu_24.wardmagic.init.WardInit;
import com.reu_24.wardmagic.util.WardHelper;
import com.reu_24.wardmagic.ward.AbstractWard;
import com.reu_24.wardmagic.ward.WardEventContext;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public interface IWardEnchantableItem {
    default void addTooltip(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        stack.getCapability(WardEnchantmentsProvider.WARDS_ENCHANTMENTS).ifPresent(cap -> {
            for (WardEnchantment wardEnchantment : cap.getEnchantments()) {
                AbstractWard ward = WardInit.getWards().get(wardEnchantment.getWardId());
                String text = ward.getTooltipColor() + ward.getName(false);
                text += ward.isMaxLevel1() ? "" : " " + WardHelper.toRoman(wardEnchantment.getLevel());
                tooltip.add(new StringTextComponent(text));
            }
        });
    }

    default ActionResult<ItemStack> itemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack item = playerIn.getHeldItem(handIn);
        AtomicReference<ActionResult<ItemStack>> actionResult = new AtomicReference<>(ActionResult.resultPass(item));
        item.getCapability(WardEnchantmentsProvider.WARDS_ENCHANTMENTS).ifPresent(enchantments -> {
            for (WardEnchantment wardEnchantment : enchantments.getEnchantments()) {
                if (WardInit.getWards().get(wardEnchantment.getWardId()).onRightClick(new WardEventContext(wardEnchantment.getLevel(), playerIn, item), enchantments)) {
                    actionResult.set(ActionResult.resultSuccess(playerIn.getHeldItem(handIn)));
                }
            }
        });
        return actionResult.get();
    }

    default ActionResultType itemUse(ItemUseContext context) {
        AtomicReference<ActionResultType> actionResult = new AtomicReference<>(ActionResultType.PASS);
        context.getItem().getCapability(WardEnchantmentsProvider.WARDS_ENCHANTMENTS).ifPresent(enchantments -> {
            for (WardEnchantment wardEnchantment : enchantments.getEnchantments()) {
                if (WardInit.getWards().get(wardEnchantment.getWardId()).onItemUse(new WardEventContext(wardEnchantment.getLevel(), context.getPlayer(), context.getItem()), enchantments, context)) {
                    actionResult.set(ActionResultType.SUCCESS);
                    break;
                }
            }
        });

        return actionResult.get();
    }

    byte getToolType();
}
