package com.reu_24.wardmagic.init;

import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.container.BookOfTheWardMagicianContainer;
import com.reu_24.wardmagic.container.ChalkContainer;
import com.reu_24.wardmagic.container.WardAltarContainer;
import com.reu_24.wardmagic.container.WardCompassContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainerTypes {

    public static DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, WardMagic.MOD_ID);

    public static final RegistryObject<ContainerType<ChalkContainer>> CHALK = CONTAINER_TYPES.register("chalk",
            () -> IForgeContainerType.create((windowId, inventory, packetBuffer) -> new ChalkContainer(windowId, ModContainerTypes.CHALK, packetBuffer)));

    public static final RegistryObject<ContainerType<WardCompassContainer>> WARD_COMPASS = CONTAINER_TYPES.register("ward_compass",
            () -> IForgeContainerType.create((windowId, inventory, packetBuffer) -> new WardCompassContainer(windowId, ModContainerTypes.WARD_COMPASS, packetBuffer)));

    public static final RegistryObject<ContainerType<WardAltarContainer>> WARD_ALTAR = CONTAINER_TYPES.register("ward_altar",
            () -> IForgeContainerType.create((windowId, inventory, packetBuffer) -> new WardAltarContainer(windowId, inventory, packetBuffer, ModContainerTypes.WARD_ALTAR)));

    public static final RegistryObject<ContainerType<BookOfTheWardMagicianContainer>> BOOK_OF_THE_WARD_MAGICIAN = CONTAINER_TYPES.register("book_of_the_ward_magician",
            () -> IForgeContainerType.create((windowId, inventory, packetBuffer) -> new BookOfTheWardMagicianContainer(windowId, ModContainerTypes.BOOK_OF_THE_WARD_MAGICIAN, packetBuffer)));
}
