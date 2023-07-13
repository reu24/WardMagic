package com.reu_24.wardmagic.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

import java.util.HashSet;
import java.util.Set;

public class WardEnchantmentsStorage implements Capability.IStorage<IWardEnchantments> {

    @Override
    public INBT writeNBT(Capability<IWardEnchantments> capability, IWardEnchantments instance, Direction side) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putIntArray("wardId", instance.getEnchantments().stream().mapToInt(e -> e.wardId).toArray());
        nbt.putIntArray("level", instance.getEnchantments().stream().mapToInt(e -> e.level).toArray());
        return nbt;
    }

    @Override
    public void readNBT(Capability<IWardEnchantments> capability, IWardEnchantments instance, Direction side, INBT nbt) {
        int[] wardIds = ((CompoundNBT) nbt).getIntArray("wardId");
        int[] levels = ((CompoundNBT) nbt).getIntArray("level");
        Set<WardEnchantment> enchantments = new HashSet<>(wardIds.length);
        for (int i = 0; i < wardIds.length; i++) {
            enchantments.add(new WardEnchantment(wardIds[i], levels[i]));
        }
        instance.setEnchantments(enchantments);
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(IWardEnchantments.class, new WardEnchantmentsStorage(), WardEnchantments::new);
    }
}
