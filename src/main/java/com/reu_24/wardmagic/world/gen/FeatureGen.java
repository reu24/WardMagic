package com.reu_24.wardmagic.world.gen;

import com.reu_24.wardmagic.world.features.MagicalTree;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.event.world.BiomeLoadingEvent;

public class FeatureGen {
    public static void generateFeatures(final BiomeLoadingEvent event) {
        if (event.getCategory() == Biome.Category.FOREST) {
            event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.TREE.withConfiguration(MagicalTree.MAGICAL_TREE_CONFIG));
        }
    }
}
