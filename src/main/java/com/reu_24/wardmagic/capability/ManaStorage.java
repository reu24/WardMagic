package com.reu_24.wardmagic.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

import java.util.HashSet;
import java.util.Set;

public class ManaStorage implements Capability.IStorage<IMana> {

    @Override
    public INBT writeNBT(Capability<IMana> capability, IMana instance, Direction side) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putFloat("maxMana", instance.getMaxMana());
        nbt.putFloat("mana", instance.getMana());
        return nbt;
    }

    @Override
    public void readNBT(Capability<IMana> capability, IMana instance, Direction side, INBT nbt) {
        CompoundNBT cNbt = (CompoundNBT)nbt;
        instance.setMaxMana(cNbt.getFloat("maxMana"));
        instance.setMana(cNbt.getFloat("mana"));
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(IMana.class, new ManaStorage(), Mana::new);
    }
}
