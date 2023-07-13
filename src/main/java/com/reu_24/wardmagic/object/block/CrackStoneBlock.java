package com.reu_24.wardmagic.object.block;

import com.reu_24.wardmagic.init.ModTileEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class CrackStoneBlock extends Block {
    public static final int CRACK_START = 3;
    public static final IntegerProperty CRACK = IntegerProperty.create("crack_state", 1, CRACK_START);

    public CrackStoneBlock(Properties properties) {
        super(properties);
        setDefaultState(getStateContainer().getBaseState().with(CRACK, CRACK_START));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(CRACK);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntityTypes.CRACK_STONE.get().create();
    }
}
