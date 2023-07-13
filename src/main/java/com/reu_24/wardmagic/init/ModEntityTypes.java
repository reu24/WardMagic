package com.reu_24.wardmagic.init;

import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.entities.RangeExplosionEntity;
import com.reu_24.wardmagic.entities.RangeFireEntity;
import com.reu_24.wardmagic.entities.SoulEntity;
import com.reu_24.wardmagic.util.ModResourceLocation;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, WardMagic.MOD_ID);

    public static final RegistryObject<EntityType<SoulEntity>> SOUL = ENTITY_TYPES.register("soul", () -> EntityType.Builder.<SoulEntity>create(SoulEntity::new, EntityClassification.MISC).size(1.0f, 1.0f).immuneToFire().setCustomClientFactory((spawnEntity, world) -> new SoulEntity(world, spawnEntity.getPosX(), spawnEntity.getPosY(), spawnEntity.getPosZ(), 0)).build(new ModResourceLocation("soul").toString()));
    public static final RegistryObject<EntityType<RangeExplosionEntity>> RANGE_EXPLOSION = ENTITY_TYPES.register("range_explosion", () -> EntityType.Builder.<RangeExplosionEntity>create(RangeExplosionEntity::new, EntityClassification.MISC).size(1.0f, 1.0f).immuneToFire().setCustomClientFactory((spawnEntity, world) -> new RangeExplosionEntity(world, spawnEntity.getPosX(), spawnEntity.getPosY(), spawnEntity.getPosZ(), spawnEntity.getVelX(), spawnEntity.getVelY(), spawnEntity.getVelZ())).build(new ModResourceLocation("range_explosion").toString()));
    public static final RegistryObject<EntityType<RangeFireEntity>> RANGE_FIRE = ENTITY_TYPES.register("range_fire", () -> EntityType.Builder.<RangeFireEntity>create(RangeFireEntity::new, EntityClassification.MISC).size(1.0f, 1.0f).immuneToFire().setCustomClientFactory((spawnEntity, world) -> new RangeFireEntity(world, spawnEntity.getPosX(), spawnEntity.getPosY(), spawnEntity.getPosZ(), spawnEntity.getVelX(), spawnEntity.getVelY(), spawnEntity.getVelZ())).build(new ModResourceLocation("range_fire").toString()));
}
