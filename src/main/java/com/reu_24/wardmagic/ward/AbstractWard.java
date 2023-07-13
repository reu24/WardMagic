package com.reu_24.wardmagic.ward;

import com.mojang.datafixers.util.Pair;
import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.capability.IWardEnchantments;
import com.reu_24.wardmagic.entities.SoulEntity;
import com.reu_24.wardmagic.util.Functions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class AbstractWard {

    public String getName(boolean withWard) {
        return new TranslationTextComponent(getNameTranslation().getFirst()).getString() +
                (withWard ? " " + new TranslationTextComponent("ward." + WardMagic.MOD_ID + ".ward").getString() : "");
    }

    public String getTooltip() {
        return new TranslationTextComponent(getTooltipTranslation().getFirst()).getString();
    }
    public boolean canApply(byte tool) {
        return (getCanApply() & tool) != 0;
    }
    public boolean isMaxLevel1() {
        return false;
    }

    public float getManaRequired(int level) {
        return Functions.exponentialMore(level, getAdditionalManaPercentageRequired(), getManaRequiredPerLevel());
    }

    public void onEnchanted(WardEventContext wardEventContext) {}
    public void onHitEntity(WardEventContext wardEventContext, LivingEntity target, IWardEnchantments enchantments) {}
    public void onSoulHit(WardEventContext wardEventContext, float diedMaxHealth, SoulEntity soul) {}
    public boolean onRightClick(WardEventContext wardEventContext, IWardEnchantments enchantments) {
        return false;
    }
    public boolean onItemUse(WardEventContext wardEventContext, IWardEnchantments enchantments, ItemUseContext itemUseContext) {
        return false;
    }

    public abstract Pair<String, String> getNameTranslation();
    public abstract Pair<String, String> getTooltipTranslation();
    public abstract String getTooltipColor();
    protected abstract byte getCanApply();
    public abstract float getManaRequiredPerLevel();
    public abstract float getAdditionalManaPercentageRequired();
    public abstract ManaUsageLevel getManaUsageLevel();

    public abstract int getSpawnWeight();

    public static final byte SWORD = 1;
    public static final byte WAND = 1 << 1;
    public static final byte HOLDER = 1 << 2;

    public enum ManaUsageLevel {
        NONE("None", TextFormatting.GREEN.toString()),
        LOW("Low", TextFormatting.YELLOW.toString()),
        MEDIUM("Medium", TextFormatting.GOLD.toString()),
        HIGH("High", TextFormatting.RED.toString());

        private final String level;
        private final String color;

        ManaUsageLevel(String level, String color) {
            this.level = level;
            this.color = color;
        }

        public String getLevel() {
            return level;
        }

        public String getColor() {
            return color;
        }
    }
}
