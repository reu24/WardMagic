package com.reu_24.wardmagic.data.client;

import com.mojang.datafixers.util.Pair;
import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.data.Annotations;
import com.reu_24.wardmagic.init.WardInit;
import com.reu_24.wardmagic.object.block.CrackStoneBlock;
import com.reu_24.wardmagic.object.block.WardBlock;
import com.reu_24.wardmagic.util.ModResourceLocation;
import com.reu_24.wardmagic.util.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Objects;

public class ModBlockStateProvider extends BlockStateProvider {

    protected final Pair<Vector3i, Vector3i> WARD_NORTH = new Pair<>(new Vector3i(0, 0, 15), new Vector3i(16, 16, 15));
    protected final Pair<Vector3i, Vector3i> WARD_EAST = new Pair<>(new Vector3i(1, 0, 0), new Vector3i(1, 16, 16));
    protected final Pair<Vector3i, Vector3i> WARD_SOUTH = new Pair<>(new Vector3i(0, 0, 1), new Vector3i(16, 16, 1));
    protected final Pair<Vector3i, Vector3i> WARD_WEST = new Pair<>(new Vector3i(15, 0, 0), new Vector3i(15, 16, 16));
    protected final Pair<Vector3i, Vector3i> WARD_UP = new Pair<>(new Vector3i(0, 15, 0), new Vector3i(16, 15, 16));
    protected final Pair<Vector3i, Vector3i> WARD_DOWN = new Pair<>(new Vector3i(0, 1, 0), new Vector3i(16, 1, 16));


    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, WardMagic.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ReflectionHelper.forEachBlock((blockData, field) -> {
            registerForEnum(blockData.blockState(), ReflectionHelper.getBlock(field));
            return null;
        });
    }



    protected void registerForEnum(Annotations.BlockStateType type, Block block) {
        switch (type) {
            case SIMPLE:
                simpleBlock(block);
                break;
            case WARD:
                wardBlock(block);
                break;
            case LOG:
                logBlock((RotatedPillarBlock) block);
                break;
            case CROSS:
                crossBlock(block);
                break;
            case CRACK:
                crack(block);
                break;
            case SMALL:
                small(block);
                break;
        }
    }


    protected void wardBlock(Block block) {
        VariantBlockStateBuilder blockState = getVariantBuilder(block);

        String blockPath = Objects.requireNonNull(block.getRegistryName()).getPath();
        for (int i = 0; i < WardInit.getWards().size(); i++) {
            String texturePath = "block/" + blockPath + "_" + i;
            for (int j = 0; j < 6; j++) {
                Direction direction = Direction.byIndex(j);
                Pair<Vector3i, Vector3i> fromTo = getFromTo(direction);
                String modelPath = "block/" + blockPath + "_" + direction.name().toLowerCase() + "_" + i;

                ModelFile model = models()
                        .getBuilder(modelPath)
                        .ao(false)
                        .texture("particle", texturePath)
                        .texture("ward", texturePath)
                        .element()
                        .from(fromTo.getFirst().getX(), fromTo.getFirst().getY(), fromTo.getFirst().getZ())
                        .to(fromTo.getSecond().getX(), fromTo.getSecond().getY(), fromTo.getSecond().getZ())
                        .shade(false)
                        .face(getFace(direction))
                        .uvs(0, 0, 16, 16)
                        .texture("#ward")
                        .tintindex(0)
                        .end()
                        .end();

                blockState = blockState.partialState().with(WardBlock.WARD_ID, i).with(WardBlock.FACING, direction)
                        .modelForState().modelFile(model).addModel();
            }
        }
    }

    protected Pair<Vector3i, Vector3i> getFromTo(Direction direction) {
        switch(direction) {
            case NORTH:
                return WARD_NORTH;
            case EAST:
                return WARD_EAST;
            case SOUTH:
                return WARD_SOUTH;
            case WEST:
                return WARD_WEST;
            case UP:
                return WARD_UP;
            case DOWN:
            default:
                return WARD_DOWN;
        }
    }

    protected Direction getFace(Direction direction) {
        if (direction == Direction.UP || direction == Direction.DOWN) {
            return direction.getOpposite();
        }
        return direction;
    }

    protected void crossBlock(Block block) {
        simpleBlock(block, cross(block));
    }

    public ModelFile cross(Block block) {
        return models().cross(block.getRegistryName().getPath(), blockTexture(block));
    }

    private void crack(Block block) {
        VariantBlockStateBuilder blockState = getVariantBuilder(block);
        String blockPath = Objects.requireNonNull(block.getRegistryName()).getPath();
        for (int i = 1; i <= CrackStoneBlock.CRACK_START; i++) {
            String texturePath = "block/" + blockPath + "_" + i;

            ModelFile model = models()
                    .getBuilder(texturePath)
                    .parent(new ModelFile.UncheckedModelFile("minecraft:block/cube_all"))
                    .texture("all", new ModResourceLocation(texturePath));

            blockState = blockState.partialState().with(CrackStoneBlock.CRACK, i)
                    .modelForState().modelFile(model).addModel();
        }
    }

    private void small(Block block) {
        String blockPath = Objects.requireNonNull(block.getRegistryName()).getPath();
        String texturePath = "block/" + blockPath;

        ModelFile model = models()
                .getBuilder(blockPath)
                .ao(false)
                .texture("particle", texturePath)
                .texture("ward", texturePath)
                .element()
                .from(WARD_DOWN.getFirst().getX(), WARD_DOWN.getFirst().getY(), WARD_DOWN.getFirst().getZ())
                .to(WARD_DOWN.getSecond().getX(), WARD_DOWN.getSecond().getY(), WARD_DOWN.getSecond().getZ())
                .shade(false)
                .face(Direction.UP)
                .uvs(0, 0, 16, 16)
                .texture("#ward")
                .end()
                .end();

        getVariantBuilder(block).partialState().modelForState().modelFile(model).addModel();
    }
}
