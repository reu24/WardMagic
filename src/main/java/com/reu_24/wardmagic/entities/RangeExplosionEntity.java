package com.reu_24.wardmagic.entities;

import com.reu_24.wardmagic.init.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class RangeExplosionEntity extends DamagingProjectileEntity {

    protected final double explosionPower;

    public RangeExplosionEntity(EntityType<? extends RangeExplosionEntity> type, World worldIn) {
        super(type, worldIn);
        this.explosionPower = 1;
    }

    @OnlyIn(Dist.CLIENT)
    public RangeExplosionEntity(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(ModEntityTypes.RANGE_EXPLOSION.get(), x, y, z, accelX, accelY, accelZ, worldIn);
        this.explosionPower = 1;
    }

    public RangeExplosionEntity(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ, double explosionPower) {
        super(ModEntityTypes.RANGE_EXPLOSION.get(), shooter, accelX, accelY, accelZ, worldIn);
        this.explosionPower = explosionPower;
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (result instanceof EntityRayTraceResult && ((EntityRayTraceResult) result).getEntity() instanceof RangeFireEntity) {
            return;
        }
        super.onImpact(result);
        if (!this.world.isRemote) {
            boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this.func_234616_v_());
            this.world.createExplosion(null, this.getPosX(), this.getPosY(), this.getPosZ(), (float)this.explosionPower, false, flag ? Explosion.Mode.DESTROY : Explosion.Mode.NONE);
            this.remove();
        }
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult p_213868_1_) {
        if (!this.world.isRemote) {
            Entity entity = p_213868_1_.getEntity();
            Entity entity1 = this.func_234616_v_();
            entity.attackEntityFrom(DamageSource.causeExplosionDamage((LivingEntity) func_234616_v_()), 6.0F);
            if (entity1 instanceof LivingEntity) {
                this.applyEnchantments((LivingEntity)entity1, entity);
            }
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
