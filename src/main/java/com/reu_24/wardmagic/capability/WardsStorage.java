package com.reu_24.wardmagic.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

import java.util.Arrays;
import java.util.stream.Collectors;

public class WardsStorage implements Capability.IStorage<IWards> {

    @Override
    public INBT writeNBT(Capability<IWards> capability, IWards instance, Direction side) {
        return new IntArrayNBT(instance.getWards().stream().mapToInt(i -> i).toArray());
    }

    @Override
    public void readNBT(Capability<IWards> capability, IWards instance, Direction side, INBT nbt) {
        instance.setWards(Arrays.stream(((IntArrayNBT)nbt).getIntArray()).boxed().collect(Collectors.toSet()));
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(IWards.class, new WardsStorage(), Wards::new);
    }
}
