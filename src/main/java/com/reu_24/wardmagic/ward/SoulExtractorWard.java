package com.reu_24.wardmagic.ward;

import com.mojang.datafixers.util.Pair;
import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.entities.SoulEntity;
import com.reu_24.wardmagic.util.WardHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TextFormatting;

public class SoulExtractorWard extends AbstractWard {

    @Override
    public Pair<String, String> getNameTranslation() {
        return new Pair<>("wards." + WardMagic.MOD_ID + ".soul_extractor", "Soul Extractor");
    }

    @Override
    public Pair<String, String> getTooltipTranslation() {
        return new Pair<>("wards." + WardMagic.MOD_ID + ".soul_extractor_tooltip", "Extracts mana from souls of killed entities.");
    }

    @Override
    public String getTooltipColor() {
        return TextFormatting.WHITE.toString();
    }

    @Override
    protected byte getCanApply() {
        return SWORD;
    }

    @Override
    public float getManaRequiredPerLevel() {
        return 5.0f;
    }

    @Override
    public float getAdditionalManaPercentageRequired() {
        return 0.15f;
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
    public void onSoulHit(WardEventContext wardEventContext, float diedMaxHealth, SoulEntity soul) {
        WardHelper.addMana(wardEventContext.player, wardEventContext.level * diedMaxHealth * 0.05f);
        PlayerEntity player = wardEventContext.player;
        wardEventContext.world.playSound(wardEventContext.player, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
        wardEventContext.item.hitEntity(null, wardEventContext.player);
    }
}
