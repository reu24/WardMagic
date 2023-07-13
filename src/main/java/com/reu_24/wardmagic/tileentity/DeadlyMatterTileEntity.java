package com.reu_24.wardmagic.tileentity;

import com.reu_24.wardmagic.init.DamageSourceInit;
import com.reu_24.wardmagic.init.ModTileEntityTypes;
import com.reu_24.wardmagic.util.WardHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class DeadlyMatterTileEntity extends TileEntity implements ITickableTileEntity {

    public static final float DAMAGE_AMOUNT = 4;

    protected WardAltarTileEntity wardAltarTileEntity;
    BlockPos wardAltarTileEntityPos;
    protected int spreadTicksRemaining = (int)(Math.random() * 200 + 50);

    public DeadlyMatterTileEntity() {
        super(ModTileEntityTypes.DEADLY_MATTER.get());
    }

    public void setWardAltarTileEntity(WardAltarTileEntity wardAltarTileEntity) {
        this.wardAltarTileEntity = wardAltarTileEntity;
    }

    @Override
    public void tick() {
        if (!world.isRemote()) {
            if (wardAltarTileEntity == null) {
                TileEntity tileEntity = world.getTileEntity(wardAltarTileEntityPos);
                if (tileEntity instanceof WardAltarTileEntity) {
                    wardAltarTileEntity = (WardAltarTileEntity) tileEntity;
                }
            }

            if (wardAltarTileEntity == null || wardAltarTileEntity.hasEnoughMana() || wardAltarTileEntity.isRemoved()) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                return;
            }

            if (spreadTicksRemaining-- <= 0) {
                WardHelper.spreadDeadlyMatter(world, pos, wardAltarTileEntity);
            }
            List<Entity> entities = world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(pos));
            for (Entity entity : entities) {
                wardAltarTileEntity.addMana(WardHelper.removeMana(entity, DAMAGE_AMOUNT));
            }
        }
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);

        wardAltarTileEntityPos = new BlockPos(nbt.getInt("wardAltarX"), nbt.getInt("wardAltarY"), nbt.getInt("wardAltarZ"));
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        if (wardAltarTileEntity != null) {
            nbt.putInt("wardAltarX", wardAltarTileEntity.getPos().getX());
            nbt.putInt("wardAltarY", wardAltarTileEntity.getPos().getY());
            nbt.putInt("wardAltarZ", wardAltarTileEntity.getPos().getZ());
        }

        return super.write(nbt);
    }
}
