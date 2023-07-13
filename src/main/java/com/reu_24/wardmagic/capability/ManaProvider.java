package com.reu_24.wardmagic.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ManaProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject(IMana.class)
    public static final Capability<IMana> MANA = null;

    private IMana instance = null;
    private final float initialMana;
    private final float initialMaxMana;

    public ManaProvider(float initialMana, float initialMaxMana) {
        this.initialMana = initialMana;
        this.initialMaxMana = initialMaxMana;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (instance == null) {
            if (MANA != null) {
                instance = MANA.getDefaultInstance();
                instance.setMana(initialMana);
                instance.setMaxMana(initialMaxMana);
            } else {
                return LazyOptional.empty();
            }
        }

        return cap == MANA ? LazyOptional.of(() -> (T) instance) : LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        if (instance == null && MANA != null) {
            instance = MANA.getDefaultInstance();
            instance.setMana(initialMana);
            instance.setMaxMana(initialMaxMana);
        }

        return MANA.getStorage().writeNBT(MANA, instance, null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        if (instance == null && MANA != null) {
            instance = MANA.getDefaultInstance();
            instance.setMana(initialMana);
            instance.setMaxMana(initialMaxMana);
        }

        MANA.getStorage().readNBT(MANA, instance, null, nbt);
    }
}
