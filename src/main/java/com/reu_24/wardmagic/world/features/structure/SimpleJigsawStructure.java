package com.reu_24.wardmagic.world.features.structure;

import com.mojang.serialization.Codec;
import com.reu_24.wardmagic.util.ModResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class SimpleJigsawStructure extends Structure<NoFeatureConfig> {

    protected final String structureName;
    protected final String startPool;
    protected final int maxRecPieces;
    protected final int groundLevel;

    /**
     * @param groundLevel How much it is over the ground. Use 0 if it has no floor or starts with a stair. Else use 1.
     */
    public SimpleJigsawStructure(Codec<NoFeatureConfig> codec, String structureName, String startPool, int maxRecPieces, int groundLevel) {
        super(codec);
        this.structureName = structureName;
        this.startPool = startPool;
        this.maxRecPieces = maxRecPieces;
        this.groundLevel = groundLevel;
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return Start::new;
    }

    @Override
    public GenerationStage.Decoration getDecorationStage() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    public String getStructureName() {
        return structureName;
    }

    public class Start extends StructureStart<NoFeatureConfig> {

        public Start(Structure p_i225876_1_, int p_i225876_2_, int p_i225876_3_, MutableBoundingBox p_i225876_4_, int p_i225876_5_, long p_i225876_6_) {
            super(p_i225876_1_, p_i225876_2_, p_i225876_3_, p_i225876_4_, p_i225876_5_, p_i225876_6_);
        }

        @Override
        public void func_230364_a_(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome p_230364_6_, NoFeatureConfig p_230364_7_) {
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;

            BlockPos blockpos = new BlockPos(x, 0, z);

            JigsawManager.func_242837_a(
                    dynamicRegistryManager,
                    new VillageConfig(() -> dynamicRegistryManager.getRegistry(Registry.JIGSAW_POOL_KEY)
                            .getOrDefault(new ModResourceLocation(startPool)),
                            maxRecPieces),
                    (templateManager, jigsawPiece, blockPos, groundLevelDelta, rotation, mutableBoundingBox) -> new SimpleJigsawPiece(templateManager, jigsawPiece, blockPos, groundLevel, rotation, mutableBoundingBox),
                    chunkGenerator,
                    templateManagerIn,
                    blockpos,
                    this.components,
                    this.rand,
                    false,
                    true); // starts on ground

            this.recalculateStructureSize();
        }
    }
}
