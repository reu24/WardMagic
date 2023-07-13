package com.reu_24.wardmagic.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class WardEnchantmentsProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject(IWardEnchantments.class)
    public static final Capability<IWardEnchantments> WARDS_ENCHANTMENTS = null;

    private IWardEnchantments instance = null;

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
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
        if (instance == null && WARDS_ENCHANTMENTS != null) {
            instance = WARDS_ENCHANTMENTS.getDefaultInstance();
        }

        return WARDS_ENCHANTMENTS.getStorage().writeNBT(WARDS_ENCHANTMENTS, instance, null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        if (instance == null && WARDS_ENCHANTMENTS != null) {
            instance = WARDS_ENCHANTMENTS.getDefaultInstance();
        }

        WARDS_ENCHANTMENTS.getStorage().readNBT(WARDS_ENCHANTMENTS, instance, null, nbt);
    }
}
