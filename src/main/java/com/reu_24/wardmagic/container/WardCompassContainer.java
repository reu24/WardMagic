package com.reu_24.wardmagic.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.RegistryObject;

public class WardCompassContainer extends Container {
    protected BlockPos placePos;

    public WardCompassContainer(int id, RegistryObject<ContainerType<WardCompassContainer>> containerType, PacketBuffer buffer) {
        this(id, containerType, buffer.readBlockPos());
    }

    public WardCompassContainer(int id, RegistryObject<ContainerType<WardCompassContainer>> containerType, BlockPos placePos) {
        super(containerType.get(), id);

        this.placePos = placePos;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public BlockPos getPlacePos() {
        return placePos;
    }
}
