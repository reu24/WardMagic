package com.reu_24.wardmagic.util;

import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.capability.IWardEnchantments;
import com.reu_24.wardmagic.capability.ManaProvider;
import com.reu_24.wardmagic.capability.WardEnchantment;
import com.reu_24.wardmagic.capability.WardEnchantmentsProvider;
import com.reu_24.wardmagic.init.BlockInit;
import com.reu_24.wardmagic.init.DamageSourceInit;
import com.reu_24.wardmagic.init.ModParticleTypes;
import com.reu_24.wardmagic.init.WardInit;
import com.reu_24.wardmagic.object.block.WardBlock;
import com.reu_24.wardmagic.object.item.IWardEnchantableItem;
import com.reu_24.wardmagic.object.item.ManaHolderItem;
import com.reu_24.wardmagic.tileentity.DeadlyMatterTileEntity;
import com.reu_24.wardmagic.tileentity.WardAltarTileEntity;
import com.reu_24.wardmagic.ward.AbstractWard;
import com.reu_24.wardmagic.ward.LifeStealWard;
import com.reu_24.wardmagic.ward.ManaStealWard;
import com.reu_24.wardmagic.ward.WardEventContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import org.lwjgl.glfw.GLFW;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public final class WardHelper {
    private WardHelper() {}

    public static int MAX_CIRCLE_RADIUS = 10;
    public static int MAX_POSITION_CALCULATED = 100;

    public static final float DAMAGE_PER_LEVEL = 0.1f;

    public static void placeWardCompassParticles(World world, int radius, BlockPos placePos) {
        for (BlockPos pos : getCircle(radius)) {
            Vector3d worldPos = new Vector3d(placePos.getX(), placePos.getY(), placePos.getZ()).add(pos.getX(), pos.getY(), pos.getZ()).add(0.5, 1.5, 0.5);
            world.addParticle(ModParticleTypes.WARD_COMPASS_PARTICLE.get(), worldPos.getX(), worldPos.getY(), worldPos.getZ(), 0, 0, 0);
        }
    }

    public static void startRitual(World world, PlayerEntity player, BlockPos pos, WardAltarTileEntity wardAltarTileEntity) {
        int radius = getCircleRadius(world, pos);
        ItemStack itemToEnchant = wardAltarTileEntity.getItemToEnchant();
        if (radius > MAX_CIRCLE_RADIUS) {
            player.sendMessage(new TranslationTextComponent("ritual." + WardMagic.MOD_ID + ".fail"), null);
            return;
        }

        Map<Integer, Integer> wardMap = new HashMap<>();
        for (BlockPos circlePos : getCircle(radius)) {
            BlockState block = world.getBlockState(pos.add(circlePos));
            int wardId = block.get(WardBlock.WARD_ID);
            wardMap.put(wardId, wardMap.getOrDefault(wardId, 0) + 1);
        }

        float manaRequired = 0;
        for (Integer wardId : wardMap.keySet()) {
            AbstractWard ward = WardInit.getWards().get(wardId);
            int level = wardMap.get(wardId);
            manaRequired += ward.getManaRequired(level);
            if (ward.canApply(((IWardEnchantableItem) itemToEnchant.getItem()).getToolType())) {
                itemToEnchant.getCapability(WardEnchantmentsProvider.WARDS_ENCHANTMENTS)
                        .orElseThrow(NullPointerException::new)
                        .addWardEnchantment(new WardEnchantment(wardId, level));

                ward.onEnchanted(new WardEventContext(level, player, itemToEnchant));
            }
        }

        wardAltarTileEntity.initForRitual(manaRequired, itemToEnchant);
        if (!wardAltarTileEntity.hasEnoughMana()) {
            spreadDeadlyMatter(world, wardAltarTileEntity.getPos(), wardAltarTileEntity);
        }
    }

    public static float getManaRequired(World world, BlockPos pos) {
        int radius = getCircleRadius(world, pos);
        if (radius > MAX_CIRCLE_RADIUS) {
            return 0.0f;
        }

        Map<Integer, Integer> wardMap = new HashMap<>();
        for (BlockPos circlePos : getCircle(radius)) {
            BlockState block = world.getBlockState(pos.add(circlePos));
            int wardId = block.get(WardBlock.WARD_ID);
            wardMap.put(wardId, wardMap.getOrDefault(wardId, 0) + 1);
        }

        float manaRequired = 0;
        for (Integer wardId : wardMap.keySet()) {
            AbstractWard ward = WardInit.getWards().get(wardId);
            int level = wardMap.get(wardId);
            manaRequired += ward.getManaRequired(level);
        }

        return manaRequired;
    }
    
    public static void spreadDeadlyMatter(World world, BlockPos pos, WardAltarTileEntity wardAltarTileEntity) {
        setDeadlyMatter(world, pos.north(), wardAltarTileEntity);
        setDeadlyMatter(world, pos.east(), wardAltarTileEntity);
        setDeadlyMatter(world, pos.south(), wardAltarTileEntity);
        setDeadlyMatter(world, pos.west(), wardAltarTileEntity);
    }

    public static void applyWardEnchantments(PlayerEntity player, float damage) {
        LazyOptional<IWardEnchantments> enchantments = player.getHeldItemMainhand().getCapability(WardEnchantmentsProvider.WARDS_ENCHANTMENTS);
        enchantments.ifPresent(cap -> {
            for (WardEnchantment enchantment : cap.getEnchantments()) {
                AbstractWard ward = WardInit.getWards().get(enchantment.getWardId());
                if (ward instanceof LifeStealWard) {
                    player.heal(LifeStealWard.getHealAmount(damage, enchantment.getLevel()));
                } else if (ward instanceof ManaStealWard) {
                    WardHelper.addMana(player, ManaStealWard.getMana(damage, enchantment.getLevel()));
                }
            }
        });
    }

    private static final long WINDOW = Minecraft.getInstance().getMainWindow().getHandle();

    @OnlyIn(Dist.CLIENT)
    public static boolean isHoldingShift() {
        return InputMappings.isKeyDown(WINDOW, GLFW.GLFW_KEY_LEFT_SHIFT) ||
                InputMappings.isKeyDown(WINDOW, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    protected static void setDeadlyMatter(World world, BlockPos pos, WardAltarTileEntity wardAltarTileEntity) {
        while (isReplaceable(world, pos.down())) {
            pos = pos.down();
        }
        while (!isReplaceable(world, pos)) {
            pos = pos.up();
        }

        if (world.getBlockState(pos).getBlock() != BlockInit.DEADLY_MATTER.get()) {
            world.setBlockState(pos, BlockInit.DEADLY_MATTER.get().getDefaultState());
            ((DeadlyMatterTileEntity)world.getTileEntity(pos)).setWardAltarTileEntity(wardAltarTileEntity);
        }
    }

    protected static boolean isReplaceable(World world, BlockPos pos) {
        return world.getBlockState(pos).getMaterial().isReplaceable();
    }

    protected static int getCircleRadius(World world, BlockPos pos) {
        int radius;
        for (radius = 1; radius <= MAX_POSITION_CALCULATED; radius++) {
            boolean isCircle = true;
            for (BlockPos circlePos : getCircle(radius)) {
                if (world.getBlockState(pos.add(circlePos)).getBlock() != BlockInit.WARD_BLOCK.get()) {
                    isCircle = false;
                    break;
                }
            }
            if (isCircle) {
                break;
            }
        }
        return radius;
    }

    public static float removeMana(Entity entity, float mana) {
        AtomicReference<Float> atomicMana = new AtomicReference<>(mana);

        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            player.inventory.mainInventory.forEach(itemStack -> {
                if (atomicMana.get() <= 0 || !(itemStack.getItem() instanceof ManaHolderItem)) {
                    return;
                }

                itemStack.getCapability(ManaProvider.MANA).ifPresent(cap -> {
                    atomicMana.set(atomicMana.get() - cap.removeMana(atomicMana.get()));
                });
            });
        }

        if (atomicMana.get() > 0.001) {
            if (entity.attackEntityFrom(DamageSourceInit.DEADLY_MATTER, atomicMana.get())) {
                return mana;
            } else {
                return mana - atomicMana.get();
            }
        }
        return mana;
    }

    public static void addMana(PlayerEntity player, float mana) {
        AtomicReference<Float> atomicMana = new AtomicReference<>(mana);
        player.inventory.mainInventory.forEach(itemStack -> {
            if (atomicMana.get() <= 0) {
                return;
            }

            itemStack.getCapability(ManaProvider.MANA).ifPresent(cap -> {
                atomicMana.set(atomicMana.get() - cap.addMana(atomicMana.get()));
            });
        });
    }

    public static float calculateAttackDamage(PlayerEntity playerEntity, Entity targetEntity, int level) {
        float f = (float)playerEntity.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float f2 = playerEntity.getCooledAttackStrength(0.5F);
        f = f * (0.2F + f2 * f2 * 0.8F);
        float f1 = DAMAGE_PER_LEVEL * level;
        f1 = f1 * f2;

        boolean flag = f2 > 0.9F;
        boolean flag2 = flag && playerEntity.fallDistance > 0.0F && !playerEntity.isOnGround() && !playerEntity.isOnLadder() && !playerEntity.isInWater() && !playerEntity.isPotionActive(Effects.BLINDNESS) && !playerEntity.isPassenger();
        flag2 = flag2 && !playerEntity.isSprinting();
        net.minecraftforge.event.entity.player.CriticalHitEvent hitResult = net.minecraftforge.common.ForgeHooks.getCriticalHit(playerEntity, targetEntity, flag2, flag2 ? 1.5F : 1.0F);
        flag2 = hitResult != null;
        if (flag2) {
            f *= hitResult.getDamageModifier();
        }

        f += f1;
        return f;
    }

    public static Set<BlockPos> getCircle(int radius) {
        Set<BlockPos> positions = new HashSet<>(50);
        for (int i = 0; i < MAX_POSITION_CALCULATED; i++) {
            double degrees = (double)i * 360.0 / MAX_POSITION_CALCULATED;
            positions.add(new BlockPos(Math.round(Math.cos(degrees) * radius), 0, Math.round(Math.sin(degrees) * radius)));
        }
        return positions;
    }
    // From: https://stackoverflow.com/questions/12967896/converting-integers-to-roman-numerals-java/12968022

    private final static TreeMap<Integer, String> map = new TreeMap<>();

    static {
        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");

    }

    public static String toRoman(int number) {
        int l =  map.floorKey(number);
        if ( number == l ) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number-l);
    }
}
