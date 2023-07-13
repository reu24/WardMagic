package com.reu_24.wardmagic.ward;

import com.mojang.datafixers.util.Pair;
import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.capability.IWardEnchantments;
import com.reu_24.wardmagic.capability.WardEnchantment;
import com.reu_24.wardmagic.init.WardInit;
import com.reu_24.wardmagic.util.Functions;
import com.reu_24.wardmagic.util.WardHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class LifeStealWard extends AbstractWard {

    @Override
    public Pair<String, String> getNameTranslation() {
        return new Pair<>("wards." + WardMagic.MOD_ID + ".life_steal", "Life Steal");
    }

    @Override
    public Pair<String, String> getTooltipTranslation() {
        return new Pair<>("wards." + WardMagic.MOD_ID + ".life_steal_tooltip", "Steal HP every time a entity is hurt by your sword or wand.");
    }

    @Override
    public String getTooltipColor() {
        return TextFormatting.LIGHT_PURPLE.toString();
    }

    @Override
    protected byte getCanApply() {
        return SWORD | WAND;
    }

    @Override
    public float getManaRequiredPerLevel() {
        return 3.0f;
    }

    @Override
    public float getAdditionalManaPercentageRequired() {
        return 0.05f;
    }

    @Override
    public ManaUsageLevel getManaUsageLevel() {
        return ManaUsageLevel.MEDIUM;
    }

    @Override
    public int getSpawnWeight() {
        return 3;
    }

    @Override
    public void onHitEntity(WardEventContext wardEventContext, LivingEntity target, IWardEnchantments enchantments) {
        int strengthLevel = enchantments.getEnchantments().stream()
                .filter(ench -> WardInit.getWards().get(ench.getWardId()) instanceof StrengthWard)
                .findFirst()
                .orElse(new WardEnchantment(0, 0))
                .getLevel();

        float damage = WardHelper.calculateAttackDamage(wardEventContext.player, target, strengthLevel);
        float healAmount = getHealAmount(damage, wardEventContext.level);
        WardHelper.removeMana(wardEventContext.player, Functions.exponentialMore((int) healAmount, 0.08f, 0.9f));
        wardEventContext.player.heal(healAmount);
    }

    public static float getHealAmount(float damage, int level) {
        return damage * 0.005f * level;
    }
}
