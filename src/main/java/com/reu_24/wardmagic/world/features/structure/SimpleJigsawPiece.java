package com.reu_24.wardmagic.world.features.structure;

import com.reu_24.wardmagic.init.BlockInit;
import com.reu_24.wardmagic.init.WardInit;
import com.reu_24.wardmagic.object.block.WardBlock;
import com.reu_24.wardmagic.util.ModResourceLocation;
import com.reu_24.wardmagic.ward.AbstractWard;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.StructureMode;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

public class SimpleJigsawPiece extends AbstractVillagePiece {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final double WARD_SPAWN_PROBABILITY = 0.5;

    protected TemplateManager templateManager;

    public SimpleJigsawPiece(TemplateManager p_i242036_1_, JigsawPiece p_i242036_2_, BlockPos blockPos, int p_i242036_4_, Rotation p_i242036_5_, MutableBoundingBox p_i242036_6_) {
        super(p_i242036_1_, p_i242036_2_, blockPos, p_i242036_4_, p_i242036_5_, p_i242036_6_);

        this.templateManager = p_i242036_1_;
    }

    public SimpleJigsawPiece(TemplateManager p_i242037_1_, CompoundNBT nbt) {
        super(p_i242037_1_, nbt);

        this.templateManager = p_i242037_1_;
    }


    public boolean func_230383_a_(ISeedReader p_230383_1_, StructureManager p_230383_2_, ChunkGenerator p_230383_3_, Random p_230383_4_, MutableBoundingBox p_230383_5_, ChunkPos p_230383_6_, BlockPos p_230383_7_) {
        for (Template.BlockInfo blockInfo : ((SingleJigsawPiece) jigsawPiece).getDataMarkers(templateManager, pos, rotation, false)) {
            if (blockInfo.nbt != null) {
                StructureMode structuremode = StructureMode.valueOf(blockInfo.nbt.getString("mode"));
                if (structuremode == StructureMode.DATA) {
                    this.handleDataMarker(blockInfo.nbt.getString("metadata"), pos.add(blockInfo.pos.rotate(rotation)), p_230383_1_, p_230383_4_, p_230383_5_);
                }
            }
        }
        return super.func_230383_a_(p_230383_1_, p_230383_2_, p_230383_3_, p_230383_4_, p_230383_5_, p_230383_6_, p_230383_7_);
    }

    protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand, MutableBoundingBox sbb) {
        if (sbb.isVecInside(pos)) {
            worldIn.removeBlock(pos, false);
            if (function.contains("ward")) {
                if (rand.nextDouble() > WARD_SPAWN_PROBABILITY) {
                    int wardId = getRandomWardId(rand);
                    Direction facing = rotation.rotate(charToDirection(function.charAt(4)));
                    worldIn.setBlockState(pos, BlockInit.WARD_BLOCK.get().getDefaultState()
                            .with(WardBlock.WARD_ID, wardId)
                            .with(WardBlock.FACING, facing), 3);
                }
            } else if (function.contains("weak_chest")) {
                BlockState state =  Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, rotation.rotate(charToDirection(function.charAt(10))));
                generateChest(worldIn, sbb, rand, pos, new ModResourceLocation("chests/weak_chest"), state);
            }
        }
    }

    protected int getRandomWardId(Random rand) {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        int count = 0;
        for (int i = 0; i < WardInit.getWards().size(); i++) {
            int weight = WardInit.getWards().get(i).getSpawnWeight();
            treeMap.put(count, i);
            count += weight;
        }

        return treeMap.floorEntry((int) Math.round(rand.nextDouble() * count)).getValue();
    }

    protected Direction charToDirection(char c) {
        switch (c) {
            case 'n':
                return Direction.NORTH;
            case 'e':
                return Direction.EAST;
            case 's':
                return Direction.SOUTH  ;
            case 'w':
                return Direction.WEST;
            case 'u':
                return Direction.UP;
            case 'd':
                return Direction.DOWN;
        }
        throw new IllegalArgumentException("Unknown key " + c);
    }
}
