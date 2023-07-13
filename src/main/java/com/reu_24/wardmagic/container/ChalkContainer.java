package com.reu_24.wardmagic.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.RegistryObject;

public class ChalkContainer extends Container {

    protected int[] knownWards;
    protected BlockPos placePos;

    public ChalkContainer(int id, RegistryObject<ContainerType<ChalkContainer>> containerType, PacketBuffer buffer) {
        this(id, containerType, buffer.readVarIntArray(), buffer.readBlockPos());
    }

    public ChalkContainer(int id, RegistryObject<ContainerType<ChalkContainer>> containerType, int[] knownWards, BlockPos placePos) {
        super(containerType.get(), id);

        this.knownWards = knownWards;
        this.placePos = placePos;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public int[] getKnownWards() {
        return knownWards;
    }

    public BlockPos getPlacePos() {
        return placePos;
    }
}
