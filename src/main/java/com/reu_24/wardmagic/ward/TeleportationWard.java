package com.reu_24.wardmagic.ward;

import com.mojang.datafixers.util.Pair;
import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.capability.IWardEnchantments;
import com.reu_24.wardmagic.util.Functions;
import com.reu_24.wardmagic.util.WardHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.TextFormatting;

public class TeleportationWard extends AbstractWard {

    @Override
    public Pair<String, String> getNameTranslation() {
        return new Pair<>("wards." + WardMagic.MOD_ID + ".teleportation", "Teleportation");
    }

    @Override
    public Pair<String, String> getTooltipTranslation() {
        return new Pair<>("wards." + WardMagic.MOD_ID + ".teleportation_tooltip", "Adds the ability to teleport to your wand.");
    }

    @Override
    public String getTooltipColor() {
        return TextFormatting.DARK_GREEN.toString();
    }

    @Override
    protected byte getCanApply() {
        return WAND;
    }

    @Override
    public float getManaRequiredPerLevel() {
        return 5.3f;
    }

    @Override
    public float getAdditionalManaPercentageRequired() {
        return 0.05f;
    }

    @Override
    public ManaUsageLevel getManaUsageLevel() {
        return ManaUsageLevel.HIGH;
    }

    @Override
    public int getSpawnWeight() {
        return 1;
    }

    @Override
    public boolean onRightClick(WardEventContext wardEventContext, IWardEnchantments enchantments) {
        PlayerEntity player = wardEventContext.player;
        double distance = (wardEventContext.level * 0.5);
        Vector3d toAdd = player.getLookVec().mul(distance, distance, distance);
        Vector3i newPosition = player.getPosition().add(toAdd.x, toAdd.y, toAdd.z);
        player.setPosition(newPosition.getX(), newPosition.getY(), newPosition.getZ());
        wardEventContext.world.playSound(newPosition.getX(), newPosition.getY(), newPosition.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f, false);
        for (int i = 0; i < 150; i++) {
            wardEventContext.world.addParticle(ParticleTypes.PORTAL, player.getPosXRandom(0.5D), player.getPosYRandom() - 0.25D, player.getPosZRandom(0.5D), (Math.random() - 0.5D) * 2.0D, -Math.random(), (Math.random() - 0.5D) * 2.0D);
        }
        WardHelper.removeMana(player, Functions.exponentialMore(wardEventContext.level, 0.03f, 0.1f));
        return true;
    }
}
