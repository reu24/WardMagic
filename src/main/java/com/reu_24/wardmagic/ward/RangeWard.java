package com.reu_24.wardmagic.ward;

import com.mojang.datafixers.util.Pair;
import com.reu_24.wardmagic.WardMagic;
import net.minecraft.util.text.TextFormatting;

public class RangeWard extends AbstractWard {

    @Override
    public Pair<String, String> getNameTranslation() {
        return new Pair<>("wards." + WardMagic.MOD_ID + ".range", "Range");
    }

    @Override
    public Pair<String, String> getTooltipTranslation() {
        return new Pair<>("wards." + WardMagic.MOD_ID + ".range_tooltip", "Makes other effects ranged.");
    }

    @Override
    public String getTooltipColor() {
        return TextFormatting.GRAY.toString();
    }

    @Override
    protected byte getCanApply() {
        return WAND;
    }

    @Override
    public float getManaRequiredPerLevel() {
        return 60.0f;
    }

    @Override
    public float getAdditionalManaPercentageRequired() {
        return 0;
    }

    @Override
    public ManaUsageLevel getManaUsageLevel() {
        return ManaUsageLevel.NONE;
    }

    @Override
    public int getSpawnWeight() {
        return 1;
    }

    @Override
    public boolean isMaxLevel1() {
        return true;
    }
}
