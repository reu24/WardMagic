package com.reu_24.wardmagic.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class WardsProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject(IWards.class)
    public static final Capability<IWards> WARDS = null;

    private final IWards instance = WARDS.getDefaultInstance();

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == WARDS ? LazyOptional.of(() -> (T) instance) : null;
    }

    @Override
    public INBT serializeNBT() {
        return WARDS.getStorage().writeNBT(WARDS, instance, null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        WARDS.getStorage().readNBT(WARDS, instance, null, nbt);
    }
}
