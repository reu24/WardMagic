package com.reu_24.wardmagic.init;

import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.data.Annotations;
import com.reu_24.wardmagic.particle.SoulParticle;
import com.reu_24.wardmagic.particle.WardCompassParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = WardMagic.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, WardMagic.MOD_ID);

    @Annotations.ParticleData(id = "ward_compass_particle", textures = {WardMagic.MOD_ID + ":ward_compass_particle"})
    public static final RegistryObject<BasicParticleType> WARD_COMPASS_PARTICLE = PARTICLES.register("ward_compass_particle",
            () -> new BasicParticleType(true));

    @Annotations.ParticleData(id = "soul_particle", textures = {WardMagic.MOD_ID + ":soul_particle0", WardMagic.MOD_ID + ":soul_particle1"})
    public static final RegistryObject<BasicParticleType> SOUL_PARTICLE = PARTICLES.register("soul_particle",
            () -> new BasicParticleType(true));

    @SubscribeEvent
    public static void registerParticleFactory(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particles.registerFactory(ModParticleTypes.WARD_COMPASS_PARTICLE.get(),
                WardCompassParticle.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ModParticleTypes.SOUL_PARTICLE.get(),
                SoulParticle.Factory::new);
    }
}
