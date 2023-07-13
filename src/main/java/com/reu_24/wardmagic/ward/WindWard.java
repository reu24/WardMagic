package com.reu_24.wardmagic.ward;

import com.mojang.datafixers.util.Pair;
import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.capability.IWardEnchantments;
import com.reu_24.wardmagic.object.item.IWardEnchantableItem;
import com.reu_24.wardmagic.util.Functions;
import com.reu_24.wardmagic.util.WardHelper;
import com.reu_24.wardmagic.util.WindSound;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WardMagic.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WindWard extends AbstractWard {

    protected static Entity entityToCancelKnockBack = null;

    @Override
    public Pair<String, String> getNameTranslation() {
        return new Pair<>("wards." + WardMagic.MOD_ID + ".wind", "Wind");
    }

    @Override
    public Pair<String, String> getTooltipTranslation() {
        return new Pair<>("wards." + WardMagic.MOD_ID + ".wind_tooltip", "Adds the power of wind to your wand or sword.");
    }

    @Override
    public String getTooltipColor() {
        return TextFormatting.YELLOW.toString();
    }

    @Override
    protected byte getCanApply() {
        return SWORD | WAND;
    }

    @Override
    public float getManaRequiredPerLevel() {
        return 6.7f;
    }

    @Override
    public float getAdditionalManaPercentageRequired() {
        return 0.15f;
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
    public void onHitEntity(WardEventContext wardEventContext, LivingEntity target, IWardEnchantments enchantments) {
        if (((IWardEnchantableItem) wardEventContext.item.getItem()).getToolType() == SWORD) {

            double ratioX = wardEventContext.player.getPosX() - target.getPosX();
            double ratioZ;
            for(ratioZ = wardEventContext.player.getPosZ() - target.getPosZ(); ratioX * ratioX + ratioZ * ratioZ < 1.0E-4D; ratioZ = (Math.random() - Math.random()) * 0.01D) {
                ratioX = (Math.random() - Math.random()) * 0.01D;
            }

            float motionY = 0.07f * wardEventContext.level;

            float strength = 0.4F;
            strength = (float)((double)strength * (1.0D - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)));
            if (!(strength <= 0.0F)) {
                target.isAirBorne = true;
                Vector3d vector3d = target.getMotion();
                Vector3d vector3d1 = (new Vector3d(ratioX, 0.0D, ratioZ)).normalize().scale((double)strength);
                target.setMotion(vector3d.x / 2.0D - vector3d1.x, vector3d.y + motionY, vector3d.z / 2.0D - vector3d1.z);
            }

            WardHelper.removeMana(wardEventContext.player, Functions.exponentialMore(wardEventContext.level, 0.04f, 0.05f));
            PlayerEntity player = wardEventContext.player;
            wardEventContext.world.playSound(player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_BAT_TAKEOFF, SoundCategory.PLAYERS, 0.25f, 1.0f, true);
            entityToCancelKnockBack = target;
        }
    }

    @SubscribeEvent
    public static void applyKnockBack(LivingKnockBackEvent event) {
        if (entityToCancelKnockBack != null && entityToCancelKnockBack == event.getEntity()) {
            entityToCancelKnockBack = null;
            event.setCanceled(true);
        }
    }

    @Override
    public boolean onRightClick(WardEventContext wardEventContext, IWardEnchantments enchantments) {
        if (((IWardEnchantableItem) wardEventContext.item.getItem()).getToolType() == WAND) {
            PlayerEntity player = wardEventContext.player;

            if (player.getLookVec().getY() <= -0.95) {
                player.addPotionEffect(new EffectInstance(Effects.SLOW_FALLING, wardEventContext.level));
                WardHelper.removeMana(player, Functions.exponentialMore(wardEventContext.level, 0.2f, 0.5f));
            } else {
                double velocity = (wardEventContext.level * 0.1f);
                Vector3d toAdd = player.getLookVec().mul(velocity, velocity, velocity);
                player.addVelocity(toAdd.x, toAdd.y, toAdd.z);
                WardHelper.removeMana(player, Functions.exponentialMore(wardEventContext.level, 0.1f, 0.22f));

                if (wardEventContext.world.isRemote()) {
                    Minecraft.getInstance().getSoundHandler().play(new WindSound(player));
                }
            }
            return true;
        }
        return false;
    }
}
