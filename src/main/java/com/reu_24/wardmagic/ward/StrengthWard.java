package com.reu_24.wardmagic.ward;

import com.mojang.datafixers.util.Pair;
import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.capability.IWardEnchantments;
import com.reu_24.wardmagic.util.Functions;
import com.reu_24.wardmagic.util.WardHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;

public class StrengthWard extends AbstractWard {

    @Override
    public Pair<String, String> getNameTranslation() {
        return new Pair<>("wards." + WardMagic.MOD_ID + ".strength", "Strength");
    }

    @Override
    public Pair<String, String> getTooltipTranslation() {
        return new Pair<>("wards." + WardMagic.MOD_ID + ".strength_tooltip", "Sharpens your sword similar to sharpness.");
    }

    @Override
    public String getTooltipColor() {
        return TextFormatting.DARK_AQUA.toString();
    }

    @Override
    protected byte getCanApply() {
        return SWORD;
    }

    @Override
    public float getManaRequiredPerLevel() {
        return 5.4f;
    }

    @Override
    public float getAdditionalManaPercentageRequired() {
        return 0.017f;
    }

    @Override
    public ManaUsageLevel getManaUsageLevel() {
        return ManaUsageLevel.LOW;
    }

    @Override
    public int getSpawnWeight() {
        return 2;
    }

    @Override
    public void onHitEntity(WardEventContext wardEventContext, LivingEntity target, IWardEnchantments enchantments) {
        float damage = WardHelper.calculateAttackDamage(wardEventContext.player, target, wardEventContext.level);
        WardHelper.removeMana(wardEventContext.player, Functions.exponentialMore(wardEventContext.level, 0.05f, 0.05f));
        target.attackEntityFrom(DamageSource.causePlayerDamage(wardEventContext.player), damage);
        wardEventContext.item.hitEntity(target, wardEventContext.player);
    }
}
