package com.reu_24.wardmagic;

import com.reu_24.wardmagic.capability.ManaStorage;
import com.reu_24.wardmagic.capability.WardEnchantmentsStorage;
import com.reu_24.wardmagic.capability.WardsStorage;
import com.reu_24.wardmagic.init.*;
import com.reu_24.wardmagic.util.ModPacketHandler;
import com.reu_24.wardmagic.world.gen.FeatureGen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(WardMagic.MOD_ID)
@Mod.EventBusSubscriber(modid = WardMagic.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WardMagic
{
    public static final String MOD_ID = "wardmagic";
    private static final Logger LOGGER = LogManager.getLogger();

    public WardMagic() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::preInit);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, this::addFeatures);

        WardInit.registerWards();
        ModParticleTypes.PARTICLES.register(modEventBus);
        ItemInit.ITEMS.register(modEventBus);
        BlockInit.BLOCKS.register(modEventBus);
        StructureFeatureInit.STRUCTURE_FEATURES.register(modEventBus);

        ModTileEntityTypes.TILE_ENTITY_TYPE.register(modEventBus);
        ModContainerTypes.CONTAINER_TYPES.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);

        ModPacketHandler.register();
    }

    public void preInit(FMLCommonSetupEvent evt) {
        WardsStorage.register();
        WardEnchantmentsStorage.register();
        ManaStorage.register();
        evt.enqueueWork(StructureFeatureInit::setupStructures);
    }

    public void addFeatures(final BiomeLoadingEvent event) {
        FeatureGen.generateFeatures(event);
        StructureFeatureInit.generateStructures(event);
    }
}
