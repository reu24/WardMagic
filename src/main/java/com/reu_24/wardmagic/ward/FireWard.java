package com.reu_24.wardmagic.ward;

import com.mojang.datafixers.util.Pair;
import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.capability.IWardEnchantments;
import com.reu_24.wardmagic.entities.RangeFireEntity;
import com.reu_24.wardmagic.init.WardInit;
import com.reu_24.wardmagic.object.item.IWardEnchantableItem;
import com.reu_24.wardmagic.util.Functions;
import com.reu_24.wardmagic.util.WardHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WardMagic.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FireWard extends AbstractWard {

    public static final float SPEED = 0.2f;
    public static final float MANA_PER_FIRE = 0.25f;

    @Override
    public Pair<String, String> getNameTranslation() {
        return new Pair<>("wards." + WardMagic.MOD_ID + ".fire", "Fire");
    }

    @Override
    public Pair<String, String> getTooltipTranslation() {
        return new Pair<>("wards." + WardMagic.MOD_ID + ".fire_tooltip", "Adds the power of fire to your wand or sword.");
    }

    @Override
    public String getTooltipColor() {
        return TextFormatting.RED.toString();
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
    public void onHitEntity(WardEventContext wardEventContext, LivingEntity target, IWardEnchantments enchantments) {
        if (((IWardEnchantableItem) wardEventContext.item.getItem()).getToolType() == SWORD) {
            target.setFire(1 + (int) Math.ceil(wardEventContext.level * 0.125f));
            WardHelper.removeMana(wardEventContext.player, Functions.exponentialMore(wardEventContext.level, 0.03f, 0.08f));
        }
    }

    @Override
    public boolean onRightClick(WardEventContext wardEventContext, IWardEnchantments enchantments) {
        if (((IWardEnchantableItem) wardEventContext.item.getItem()).getToolType() == WAND) {
            if (enchantments.getEnchantments().stream().anyMatch(e -> WardInit.getWards().get(e.getWardId()) instanceof RangeWard)) {
                Vector3d vel = wardEventContext.player.getLookVec().mul(SPEED, SPEED, SPEED);
                PlayerEntity player = wardEventContext.player;
                if (!wardEventContext.world.isRemote()) {
                    RangeFireEntity rangeFire = new RangeFireEntity(wardEventContext.world, wardEventContext.player, vel.x, vel.y, vel.z, 0.2f * wardEventContext.level);
                    rangeFire.forceSetPosition(rangeFire.getPosX(), rangeFire.getPosY() + 1, rangeFire.getPosZ());
                    wardEventContext.world.addEntity(rangeFire);
                }
                wardEventContext.world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1.0f, 1.0f);
                WardHelper.removeMana(wardEventContext.player, Functions.exponentialMore(wardEventContext.level, 0.06f, 0.2f));
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public static void livingAttack(LivingAttackEvent event) {
        if (event.getSource().getDamageType().equals("explosion.player") && event.getSource().getTrueSource() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
            WardHelper.applyWardEnchantments(player, event.getAmount());
        }
    }

    @Override
    public boolean onItemUse(WardEventContext wardEventContext, IWardEnchantments enchantments, ItemUseContext itemUseContext) {
        if (((IWardEnchantableItem) wardEventContext.item.getItem()).getToolType() == WAND) {
            if (enchantments.getEnchantments().stream().noneMatch(e -> WardInit.getWards().get(e.getWardId()) instanceof RangeWard)) {
                if (lightOnFire(itemUseContext)) {
                    WardHelper.removeMana(wardEventContext.player, MANA_PER_FIRE);
                    return true;
                }
                return false;
            }
        }

        return false;
    }

    protected boolean lightOnFire(ItemUseContext context) {
        PlayerEntity playerentity = context.getPlayer();
        World world = context.getWorld();
        BlockPos blockpos = context.getPos();
        BlockState blockstate = world.getBlockState(blockpos);

        if (CampfireBlock.canBeLit(blockstate)) {
            world.playSound(playerentity, blockpos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, (float)Math.random() * 0.4F + 0.8F);
            world.setBlockState(blockpos, blockstate.with(BlockStateProperties.LIT, Boolean.TRUE), 11);
            return true;
        }

        BlockPos blockpos1 = blockpos.offset(context.getFace());
        if (AbstractFireBlock.canLightBlock(world, blockpos1, context.getPlacementHorizontalFacing())) {
            world.playSound(playerentity, blockpos1, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, (float)Math.random() * 0.4F + 0.8F);
            BlockState blockstate1 = AbstractFireBlock.getFireForPlacement(world, blockpos1);
            world.setBlockState(blockpos1, blockstate1, 11);
            ItemStack itemstack = context.getItem();
            if (playerentity instanceof ServerPlayerEntity) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) playerentity, blockpos1, itemstack);
            }
            return true;
        }
        return false;
    }
}
