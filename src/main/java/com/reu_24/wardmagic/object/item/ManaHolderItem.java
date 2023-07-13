package com.reu_24.wardmagic.object.item;

import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.capability.IMana;
import com.reu_24.wardmagic.capability.ManaProvider;
import com.reu_24.wardmagic.capability.ManaWardEnchantmentsProvider;
import com.reu_24.wardmagic.ward.AbstractWard;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.List;

public class ManaHolderItem extends WardEnchantableItem {

    protected final float mana;
    protected final float maxMana;

    public ManaHolderItem(Properties properties, float mana, float maxMana) {
        super(properties, AbstractWard.HOLDER);

        this.mana = mana;
        this.maxMana = maxMana;
    }

    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT nbt = stack.getOrCreateTag();
        IMana cap = stack.getCapability(ManaProvider.MANA).orElseThrow(NullPointerException::new);
        nbt.putFloat("maxMana", cap.getMaxMana());
        nbt.putFloat("mana", cap.getMana());
        return nbt;
    }

    @Override
    public void readShareTag(ItemStack stack, CompoundNBT nbt) {
        super.readShareTag(stack, nbt);

        if (nbt != null) {
            IMana cap = stack.getCapability(ManaProvider.MANA).orElseThrow(NullPointerException::new);
            cap.setMaxMana(nbt.getFloat("maxMana"));
            cap.setMana(nbt.getFloat("mana"));
        }
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
        return new ManaWardEnchantmentsProvider(mana, maxMana);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        stack.getCapability(ManaProvider.MANA).ifPresent(cap -> {
            tooltip.add(new TranslationTextComponent("item." + WardMagic.MOD_ID + ".mana_holder_tooltip", cap.getMana(), cap.getMaxMana()));
        });
    }
}
