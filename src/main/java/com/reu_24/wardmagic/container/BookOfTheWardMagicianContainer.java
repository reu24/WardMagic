package com.reu_24.wardmagic.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.RegistryObject;

public class BookOfTheWardMagicianContainer extends Container {
    public BookOfTheWardMagicianContainer(int id, RegistryObject<ContainerType<BookOfTheWardMagicianContainer>> containerType, PacketBuffer buffer) {
        this(id, containerType);
    }

    public BookOfTheWardMagicianContainer(int id, RegistryObject<ContainerType<BookOfTheWardMagicianContainer>> containerType) {
        super(containerType.get(), id);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}
