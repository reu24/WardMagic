package com.reu_24.wardmagic.ward;

import com.mojang.datafixers.util.Pair;
import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.capability.ManaProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class CapacityWard extends AbstractWard {

    @Override
    public Pair<String, String> getNameTranslation() {
        return new Pair<>("wards." + WardMagic.MOD_ID + ".capacity", "Capacity");
    }

    @Override
    public Pair<String, String> getTooltipTranslation() {
        return new Pair<>("wards." + WardMagic.MOD_ID + ".capacity_tooltip", "Increases the capacity of a mana holder.");
    }

    @Override
    public String getTooltipColor() {
        return TextFormatting.GOLD.toString();
    }

    @Override
    protected byte getCanApply() {
        return HOLDER;
    }

    @Override
    public float getManaRequiredPerLevel() {
        return 3.0f;
    }

    @Override
    public float getAdditionalManaPercentageRequired() {
        return 0.0f;
    }

    @Override
    public ManaUsageLevel getManaUsageLevel() {
        return ManaUsageLevel.NONE;
    }

    @Override
    public int getSpawnWeight() {
        return 4;
    }

    @Override
    public void onEnchanted(WardEventContext wardEventContext) {
        wardEventContext.item.getCapability(ManaProvider.MANA).ifPresent(cap -> {
            cap.setMaxMana(cap.getMaxMana() + wardEventContext.level * 8.0f);
        });
    }
}
