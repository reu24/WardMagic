package com.reu_24.wardmagic.ward;

import com.mojang.datafixers.util.Pair;
import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.capability.IWardEnchantments;
import com.reu_24.wardmagic.entities.RangeExplosionEntity;
import com.reu_24.wardmagic.init.WardInit;
import com.reu_24.wardmagic.object.item.IWardEnchantableItem;
import com.reu_24.wardmagic.util.Functions;
import com.reu_24.wardmagic.util.WardHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WardMagic.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ExplosionWard extends AbstractWard {

    private static final float SPEED = 0.2f;

    protected static PlayerEntity protectFromExplosion = null;

    @Override
    public Pair<String, String> getNameTranslation() {
        return new Pair<>("wards." + WardMagic.MOD_ID + ".explosion", "Explosion");
    }

    @Override
    public Pair<String, String> getTooltipTranslation() {
        return new Pair<>("wards." + WardMagic.MOD_ID + ".explosion_tooltip", "Adds the power of explosion to your sword or wand.");
    }

    @Override
    public String getTooltipColor() {
        return TextFormatting.GRAY.toString();
    }

    @Override
    protected byte getCanApply() {
        return SWORD | WAND;
    }

    @Override
    public float getManaRequiredPerLevel() {
        return 4.8f;
    }

    @Override
    public float getAdditionalManaPercentageRequired() {
        return 0.04f;
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
    public void onHitEntity(WardEventContext context, LivingEntity target, IWardEnchantments enchantments) {
        if (((IWardEnchantableItem) context.item.getItem()).getToolType() == SWORD) {
            protectFromExplosion = context.player;
            context.world.createExplosion(null, target.getPosX(), target.getPosY(), target.getPosZ(), 0.1f * context.level, Explosion.Mode.DESTROY);
            protectFromExplosion = null;
            double factor = Math.sqrt(context.level * 0.13);
            target.setMotion(target.getMotion().mul(factor, factor, factor));
            WardHelper.removeMana(context.player, Functions.exponentialMore(context.level, 0.1f, 0.2f));
        }
    }

    @Override
    public boolean onRightClick(WardEventContext wardEventContext, IWardEnchantments enchantments) {
        if (((IWardEnchantableItem) wardEventContext.item.getItem()).getToolType() == WAND) {
            PlayerEntity player = wardEventContext.player;
            if (enchantments.getEnchantments().stream().anyMatch(e -> WardInit.getWards().get(e.getWardId()) instanceof RangeWard)) {
                Vector3d vel = player.getLookVec().mul(SPEED, SPEED, SPEED);
                if (!wardEventContext.world.isRemote()) {
                    RangeExplosionEntity rangeExplosion = new RangeExplosionEntity(wardEventContext.world, player, vel.x, vel.y, vel.z, 0.25f * wardEventContext.level);
                    Vector3d lookVector = player.getLookVec();
                    rangeExplosion.forceSetPosition(rangeExplosion.getPosX() + lookVector.x, rangeExplosion.getPosY() + 1, rangeExplosion.getPosZ() + lookVector.y);
                    wardEventContext.world.addEntity(rangeExplosion);
                }
                wardEventContext.world.playSound(player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_WITHER_SHOOT, SoundCategory.PLAYERS, 1.0f, 1.0f, true);
                WardHelper.removeMana(wardEventContext.player, Functions.exponentialMore(wardEventContext.level, 0.05f, 0.15f));
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public static void livingAttack(LivingAttackEvent event) {
        if (event.getSource().getImmediateSource() instanceof RangeExplosionEntity && event.getSource().getTrueSource() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
            WardHelper.applyWardEnchantments(player, event.getAmount());
        } else if (protectFromExplosion != null && event.getEntity() == protectFromExplosion && event.getSource().getDamageType().equals("explosion")) {
            protectFromExplosion = null;
            event.setCanceled(true);
        }
    }

    @Override
    public boolean onItemUse(WardEventContext context, IWardEnchantments enchantments, ItemUseContext itemUseContext) {
        if (((IWardEnchantableItem) context.item.getItem()).getToolType() == WAND) {
            if (enchantments.getEnchantments().stream().noneMatch(e -> WardInit.getWards().get(e.getWardId()) instanceof RangeWard)) {
                BlockPos pos = itemUseContext.getPos();
                protectFromExplosion = context.player;
                context.world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 0.5f * context.level, Explosion.Mode.DESTROY);
                protectFromExplosion = null;
                WardHelper.removeMana(context.player, Functions.exponentialMore(context.level, 0.01f, 0.1f));
                return true;
            }
        }

        return false;
    }
}
