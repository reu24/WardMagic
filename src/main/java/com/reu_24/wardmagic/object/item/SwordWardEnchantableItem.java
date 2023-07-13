package com.reu_24.wardmagic.object.item;

import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.capability.*;
import com.reu_24.wardmagic.init.WardInit;
import com.reu_24.wardmagic.util.WardHelper;
import com.reu_24.wardmagic.ward.AbstractWard;
import com.reu_24.wardmagic.ward.ManaStealWard;
import com.reu_24.wardmagic.ward.StrengthWard;
import com.reu_24.wardmagic.ward.WardEventContext;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Mod.EventBusSubscriber(modid = WardMagic.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SwordWardEnchantableItem extends SwordItem implements IWardEnchantableItem {

    public SwordWardEnchantableItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn) {
        super(tier, attackDamageIn, attackSpeedIn, builderIn);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
        return new WardEnchantmentsProvider();
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        addTooltip(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public byte getToolType() {
        return AbstractWard.SWORD;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ActionResult<ItemStack> actionResult = itemRightClick(worldIn, playerIn, handIn);
        if (actionResult.getType() != ActionResultType.PASS) {
            return actionResult;
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        return itemUse(context);
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        PlayerEntity player = event.getPlayer();
        if (event.getTarget() instanceof LivingEntity) {
            ItemStack item = player.getHeldItemMainhand();
            item.getCapability(WardEnchantmentsProvider.WARDS_ENCHANTMENTS).ifPresent(w -> {
                w.getEnchantments().forEach(e -> {
                    AbstractWard ward = WardInit.getWards().get(e.getWardId());
                    ward.onHitEntity(new WardEventContext(e.getLevel(), player, item), (LivingEntity) event.getTarget(), w);
                });
            });
        }
    }

    @Override
    public int getBurnTime(ItemStack itemStack) {
        return 200;
    }
}
