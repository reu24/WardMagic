package com.reu_24.wardmagic.tileentity;

import com.reu_24.wardmagic.init.ModTileEntityTypes;
import com.reu_24.wardmagic.object.block.CrackStoneBlock;
import com.reu_24.wardmagic.util.ModPacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class CrackStoneTileEntity extends TileEntity implements ITickableTileEntity {
    public static final int TICKS_TO_STATE_UPDATE = 5;
    protected int tickOnBlock = 0;

    public CrackStoneTileEntity() {
        this(ModTileEntityTypes.CRACK_STONE.get());
    }

    public CrackStoneTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick() {
        if (!world.isRemote()) {
            Entity closesEntity = world.getClosestEntity(world.getPlayers(), new EntityPredicate(), null, pos.getX(), pos.getY(), pos.getZ());
            if (closesEntity != null && pos.withinDistance(closesEntity.getPosition().down(), 0.5d)) {
                tickOnBlock++;
            } else {
                tickOnBlock = 0;
            }

            if (tickOnBlock++ >= TICKS_TO_STATE_UPDATE) {
                ModPacketHandler.sendToAllNearClients("crack_stone", pos, 5, world, pos);
                tickOnBlock = 0;
                if (getBlockState().get(CrackStoneBlock.CRACK) <= 1) {
                    world.removeBlock(pos, false);
                } else {
                    world.setBlockState(pos, getBlockState().with(CrackStoneBlock.CRACK, getBlockState().get(CrackStoneBlock.CRACK) - 1));
                }
            }
        }
    }
}
