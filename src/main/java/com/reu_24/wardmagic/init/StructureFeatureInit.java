package com.reu_24.wardmagic.init;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.util.ModResourceLocation;
import com.reu_24.wardmagic.world.features.structure.SimpleJigsawStructure;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = WardMagic.MOD_ID)
public class StructureFeatureInit {
    public static final DeferredRegister<Structure<?>> STRUCTURE_FEATURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, WardMagic.MOD_ID);

    public static final RegistryObject<SimpleJigsawStructure> WARD_TEMPLE = register("ward_temple", false,
            new SimpleJigsawStructure(NoFeatureConfig.field_236558_a_, "ward_temple", "ward_temple/front", 1, 0));

    public static final RegistryObject<SimpleJigsawStructure> DESERT_PILLOW = register("desert_pillow", false,
            new SimpleJigsawStructure(NoFeatureConfig.field_236558_a_, "desert_pillow", "desert_pillow", 0, 0));

    public static final RegistryObject<SimpleJigsawStructure> RITUAL = register("ritual", false,
            new SimpleJigsawStructure(NoFeatureConfig.field_236558_a_, "ritual", "ritual", 0, 0));

    public static final RegistryObject<SimpleJigsawStructure> WARD_PYRAMID = register("ward_pyramid", false,
            new SimpleJigsawStructure(NoFeatureConfig.field_236558_a_, "ward_pyramid", "ward_pyramid", 0, 0));


    public static void registerStructures() {
        structureData.add(new StructureData(WARD_TEMPLE, "ward_temple", 5, 7, WARD_TEMPLE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG), StructureData.BiomeCategory.JUNGLE));
        structureData.add(new StructureData(DESERT_PILLOW, "desert_pillow", 15, 17, DESERT_PILLOW.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG), StructureData.BiomeCategory.DESERT));
        structureData.add(new StructureData(RITUAL, "ritual", 12, 14, RITUAL.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG), StructureData.BiomeCategory.LAND));
        structureData.add(new StructureData(WARD_PYRAMID, "ward_pyramid", 11, 15, WARD_PYRAMID.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG), StructureData.BiomeCategory.LAND2));
    }

    // ***************************************************************************************************************

    protected static ArrayList<StructureData> structureData = new ArrayList<>();

    protected static class StructureData {
        public final RegistryObject<? extends Structure<NoFeatureConfig>> structure;
        public final String name;
        public final int minChunk;
        public final int maxChunk;
        public final Object registryValue;
        public final BiomeCategory biomeCategory;

        public StructureData(RegistryObject<? extends Structure<NoFeatureConfig>> structure, String name, int minChunk, int maxChunk, Object registryValue, BiomeCategory biomeCategory) {
            this.structure = structure;
            this.name = name;
            this.minChunk = minChunk;
            this.maxChunk = maxChunk;
            this.registryValue = registryValue;
            this.biomeCategory = biomeCategory;
        }

        public enum BiomeCategory {
            LAND,
            JUNGLE,
            DESERT,
            LAND2
        }
    }

    public static void setupRegistries() {
        for (StructureData data : structureData) {
            Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ModResourceLocation(data.name), (StructureFeature<?, ?>) data.registryValue);
        }
    }

    public static void addStructures() {
        for (StructureData data : structureData) {
            addStructure(data.structure, data.minChunk, data.maxChunk);
        }
    }

    public static void generateStructures(final BiomeLoadingEvent event) {
        for (StructureData data : structureData) {
            if (data.biomeCategory == StructureData.BiomeCategory.LAND &&
                    event.getCategory() != Biome.Category.ICY && event.getCategory() != Biome.Category.THEEND &&
                    event.getCategory() != Biome.Category.OCEAN && event.getCategory() != Biome.Category.RIVER &&
                    event.getCategory() != Biome.Category.NETHER) {
                generate(event, data);
            } else if (data.biomeCategory == StructureData.BiomeCategory.JUNGLE && event.getCategory() == Biome.Category.JUNGLE) {
                generate(event, data);
            } else if (data.biomeCategory == StructureData.BiomeCategory.DESERT && event.getCategory() == Biome.Category.DESERT) {
                generate(event, data);
            } else if (data.biomeCategory == StructureData.BiomeCategory.LAND2 &&
                    event.getCategory() != Biome.Category.ICY && event.getCategory() != Biome.Category.THEEND &&
                    event.getCategory() != Biome.Category.OCEAN && event.getCategory() != Biome.Category.RIVER &&
                    event.getCategory() != Biome.Category.NETHER && event.getCategory() != Biome.Category.BEACH) {
                generate(event, data);
            }
        }
    }

    protected static void generate(BiomeLoadingEvent event, StructureData data) {
        event.getGeneration().withStructure(getFeature(data.name, data.structure));
    }

    public static void setupStructures() {
        registerStructures();
        addStructures();
        setupRegistries();
    }

    protected static void addStructure(RegistryObject<? extends Structure<NoFeatureConfig>> structure, int minChunk, int maxChunk) {
        DimensionStructuresSettings.field_236191_b_ =
                ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                        .putAll(DimensionStructuresSettings.field_236191_b_)
                        .put(structure.get(), new StructureSeparationSettings(maxChunk, minChunk, 484783923))
                        .build();

        DimensionSettings.field_242740_q.getStructures().field_236193_d_.put(structure.get(),
                new StructureSeparationSettings(maxChunk,  minChunk, 484783923));
    }

    protected static StructureFeature<NoFeatureConfig, ?> getFeature(String name, RegistryObject<? extends Structure<NoFeatureConfig>> structure) {
        return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE,
                new ModResourceLocation(name), structure.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
    }

    protected static <T extends Structure<NoFeatureConfig>> RegistryObject<T> register(String name, boolean transformSurroundingLand, T structure) {
        if (!Structure.NAME_STRUCTURE_BIMAP.containsValue(structure)) {
            Structure.NAME_STRUCTURE_BIMAP.putIfAbsent(name, structure);
        }

        Structure.STRUCTURE_DECORATION_STAGE_MAP.putIfAbsent(structure, structure.getDecorationStage());

        if (transformSurroundingLand) {
            Structure.field_236384_t_ = ImmutableList.<Structure<?>>builder().addAll(Structure.field_236384_t_)
                    .add(structure).build();
        }

        return STRUCTURE_FEATURES.register(name, () -> structure);
    }
}
