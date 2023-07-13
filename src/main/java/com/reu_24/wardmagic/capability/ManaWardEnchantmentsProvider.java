package com.reu_24.wardmagic.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;

public class ManaWardEnchantmentsProvider extends ManaProvider {

    @CapabilityInject(IWardEnchantments.class)
    public static final Capability<IWardEnchantments> WARDS_ENCHANTMENTS = null;

    private IWardEnchantments instance = null;

    public ManaWardEnchantmentsProvider(float initialMana, float initialMaxMana) {
        super(initialMana, initialMaxMana);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        LazyOptional<T> superResult = super.getCapability(cap, side);
        if (superResult.isPresent()) {
            return superResult;
        }

        if (instance == null) {
            if (WARDS_ENCHANTMENTS != null) {
                instance = WARDS_ENCHANTMENTS.getDefaultInstance();
            } else {
                return LazyOptional.empty();
            }
        }

        return cap == WARDS_ENCHANTMENTS ? LazyOptional.of(() -> (T) instance) : LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        CompoundNBT result = new CompoundNBT();
        result.put("mana", super.serializeNBT());

        if (instance == null && WARDS_ENCHANTMENTS != null) {
            instance = WARDS_ENCHANTMENTS.getDefaultInstance();
        }

        INBT wardEnchantments = WARDS_ENCHANTMENTS.getStorage().writeNBT(WARDS_ENCHANTMENTS, instance, null);
        result.put("wardEnchantments", wardEnchantments);
        return result;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CompoundNBT cNbt = (CompoundNBT) nbt;
        super.deserializeNBT(cNbt.get("mana"));

        if (instance == null && WARDS_ENCHANTMENTS != null) {
            instance = WARDS_ENCHANTMENTS.getDefaultInstance();
        }

        WARDS_ENCHANTMENTS.getStorage().readNBT(WARDS_ENCHANTMENTS, instance, null, (cNbt.get("wardEnchantments")));
    }
}
