package com.reu_24.wardmagic.ward;

import com.mojang.datafixers.util.Pair;
import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.capability.IWardEnchantments;
import com.reu_24.wardmagic.capability.WardEnchantment;
import com.reu_24.wardmagic.init.WardInit;
import com.reu_24.wardmagic.util.WardHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class ManaStealWard extends AbstractWard {

    @Override
    public Pair<String, String> getNameTranslation() {
        return new Pair<>("wards." + WardMagic.MOD_ID + ".mana_steal", "Mana Steal");
    }

    @Override
    public Pair<String, String> getTooltipTranslation() {
        return new Pair<>("wards." + WardMagic.MOD_ID + ".mana_steal_tooltip", "Steal mana every time a entity is hurt by your sword or wand.");
    }

    @Override
    public String getTooltipColor() {
        return TextFormatting.BLUE.toString();
    }

    @Override
    protected byte getCanApply() {
        return SWORD | WAND;
    }

    @Override
    public float getManaRequiredPerLevel() {
        return 3.7f;
    }

    @Override
    public float getAdditionalManaPercentageRequired() {
        return 0.08f;
    }

    @Override
    public ManaUsageLevel getManaUsageLevel() {
        return ManaUsageLevel.NONE;
    }

    @Override
    public int getSpawnWeight() {
        return 5;
    }

    @Override
    public void onHitEntity(WardEventContext wardEventContext, LivingEntity target, IWardEnchantments enchantments) {
        int strengthLevel = enchantments.getEnchantments().stream()
                .filter(ench -> WardInit.getWards().get(ench.getWardId()) instanceof StrengthWard)
                .findFirst()
                .orElse(new WardEnchantment(0, 0))
                .getLevel();

        float damage = WardHelper.calculateAttackDamage(wardEventContext.player, target, strengthLevel);
        WardHelper.addMana(wardEventContext.player, getMana(damage, wardEventContext.level));
    }

    public static float getMana(float damage, int level) {
        return damage * 0.0125f * level;
    }
}
