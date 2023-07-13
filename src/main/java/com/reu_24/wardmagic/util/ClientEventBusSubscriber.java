package com.reu_24.wardmagic.util;

import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.capability.IMana;
import com.reu_24.wardmagic.capability.Mana;
import com.reu_24.wardmagic.capability.ManaProvider;
import com.reu_24.wardmagic.client.entity.RangeExplosionEntityRenderer;
import com.reu_24.wardmagic.client.entity.RangeFireEntityRenderer;
import com.reu_24.wardmagic.client.entity.SoulEntityRenderer;
import com.reu_24.wardmagic.client.gui.BookOfTheWardMagicianScreen;
import com.reu_24.wardmagic.client.gui.ChalkScreen;
import com.reu_24.wardmagic.client.gui.WardAltarScreen;
import com.reu_24.wardmagic.client.gui.WardCompassScreen;
import com.reu_24.wardmagic.init.BlockInit;
import com.reu_24.wardmagic.init.ItemInit;
import com.reu_24.wardmagic.init.ModContainerTypes;
import com.reu_24.wardmagic.init.ModEntityTypes;
import javafx.scene.input.KeyCode;
import net.java.games.input.Keyboard;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemModelsProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = WardMagic.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    private static final int MANA_HOLDER_STATES = 5;

    private static KeyBinding placeWardBinding;

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ModContainerTypes.CHALK.get(), ChalkScreen::new);
        ScreenManager.registerFactory(ModContainerTypes.WARD_COMPASS.get(), WardCompassScreen::new);
        ScreenManager.registerFactory(ModContainerTypes.WARD_ALTAR.get(), WardAltarScreen::new);
        ScreenManager.registerFactory(ModContainerTypes.BOOK_OF_THE_WARD_MAGICIAN.get(), BookOfTheWardMagicianScreen::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SOUL.get(), SoulEntityRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.RANGE_EXPLOSION.get(), RangeExplosionEntityRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.RANGE_FIRE.get(), RangeFireEntityRenderer::new);

        placeWardBinding = new KeyBinding("key." + WardMagic.MOD_ID + ".place_ward", 0x47, "key.categories.misc");
        ClientRegistry.registerKeyBinding(placeWardBinding);

        event.enqueueWork(() -> {
            ItemModelsProperties.registerProperty(ItemInit.MANA_HOLDER.get(), new ModResourceLocation("mana"), (stack, world, entity) -> {
                IMana mana = stack.getCapability(ManaProvider.MANA).orElse(new Mana());
                return mana.getMana() / mana.getMaxMana() * MANA_HOLDER_STATES;
            });
        });

        for (Field field : BlockInit.class.getFields()) {
            if (field.getAnnotation(Cutout.class) != null) {
                RenderTypeLookup.setRenderLayer(Objects.requireNonNull(ReflectionHelper.getBlock(field)), RenderType.getCutout());
            }
        }
    }

    public static KeyBinding getPlaceWardKeyBinding() {
        return placeWardBinding;
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Cutout {
    }
}
