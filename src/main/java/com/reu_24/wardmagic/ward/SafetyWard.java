package com.reu_24.wardmagic.ward;

import com.mojang.datafixers.util.Pair;
import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.capability.IWardEnchantments;
import com.reu_24.wardmagic.object.item.IWardEnchantableItem;
import com.reu_24.wardmagic.util.Functions;
import com.reu_24.wardmagic.util.WardHelper;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class SafetyWard extends AbstractWard {

    public static final float SWORD_HEALTH_START = 4.0f;
    public static final float SLOWNESS_DURATION_PER_LEVEL = 3.4f;
    public static final float MANA_PER_OBSIDIAN_SAFETY = 5.0f;

    @Override
    public Pair<String, String> getNameTranslation() {
        return new Pair<>("wards." + WardMagic.MOD_ID + ".safety", "Safety");
    }

    @Override
    public Pair<String, String> getTooltipTranslation() {
        return new Pair<>("wards." + WardMagic.MOD_ID + ".safety_tooltip", "Makes you safer when you put it on your wand or sword.");
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
        return 2.0f;
    }

    @Override
    public float getAdditionalManaPercentageRequired() {
        return 0.03f;
    }

    @Override
    public ManaUsageLevel getManaUsageLevel() {
        return ManaUsageLevel.LOW;
    }

    @Override
    public int getSpawnWeight() {
        return 1;
    }

    @Override
    public void onHitEntity(WardEventContext context, LivingEntity target, IWardEnchantments enchantments) {
        if (((IWardEnchantableItem) context.item.getItem()).getToolType() == SWORD) {
            PlayerEntity player = context.player;
            if (player.getHealth() <= SWORD_HEALTH_START) {
                target.addPotionEffect(new EffectInstance(Effects.SLOWNESS, (int) Math.ceil(SLOWNESS_DURATION_PER_LEVEL * context.level), 10));
                WardHelper.removeMana(player, Functions.exponentialMore(context.level, 0.05f, 0.05f));
                context.world.playSound(target.getPosX(), target.getPosY(), target.getPosZ(), SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.PLAYERS, 1.0f, 1.0f, true);
            }
        }
    }

    @Override
    public boolean onRightClick(WardEventContext wardEventContext, IWardEnchantments enchantments) {
        if (((IWardEnchantableItem) wardEventContext.item.getItem()).getToolType() == WAND) {
            BlockPos pos = wardEventContext.player.getPosition();
            World world = wardEventContext.world;

            boolean placed = false;
            for (BlockPos placePos : getPlacePositions(pos)) {
                if (world.getBlockState(placePos).getBlock() != Blocks.OBSIDIAN && world.getBlockState(placePos).getBlock() != Blocks.BEDROCK) {
                    placed = true;
                    world.playSound(placePos.getX(), placePos.getY(), placePos.getZ(), SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 0.5f, 1.0f, true);
                    world.setBlockState(placePos, Blocks.OBSIDIAN.getDefaultState());
                }
            }

            if (placed) {
                WardHelper.removeMana(wardEventContext.player, MANA_PER_OBSIDIAN_SAFETY);
                wardEventContext.player.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                return true;
            }
            return false;
        }
        return false;
    }

    public BlockPos[] getPlacePositions(BlockPos pos) {
        return new BlockPos[] {
                pos.north(),
                pos.north().up(),
                pos.east(),
                pos.east().up(),
                pos.south(),
                pos.south().up(),
                pos.west(),
                pos.west().up(),
                pos.down(),
                pos.up().up()
        };
    }
}
