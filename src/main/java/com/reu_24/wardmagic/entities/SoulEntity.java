package com.reu_24.wardmagic.entities;

import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.capability.WardEnchantment;
import com.reu_24.wardmagic.capability.WardEnchantmentsProvider;
import com.reu_24.wardmagic.init.ModEntityTypes;
import com.reu_24.wardmagic.init.ModParticleTypes;
import com.reu_24.wardmagic.init.WardInit;
import com.reu_24.wardmagic.ward.WardEventContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkHooks;

@Mod.EventBusSubscriber(modid = WardMagic.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SoulEntity extends Entity {

    private static final DataParameter<Float> DIED_MAX_HEALTH = EntityDataManager.createKey(SoulEntity.class, DataSerializers.FLOAT);

    public static final float MOVEMENT_SPEED = 0.1f;
    public static final int LIVING_TICKS = 80;

    protected float motionY = MOVEMENT_SPEED;

    public static SoulEntity activeInstance;

    public SoulEntity(EntityType<? extends SoulEntity> type, World worldIn) {
        super(type, worldIn);

        noClip = true;
    }

    public SoulEntity(World worldIn, double x, double y, double z, float maxHealth) {
        this(ModEntityTypes.SOUL.get(), worldIn);
        this.setPosition(x, y, z);
        this.getDataManager().set(DIED_MAX_HEALTH, maxHealth);

        activeInstance = this;
        world.addParticle(ModParticleTypes.SOUL_PARTICLE.get(), x, y + 0.5, z, 0, MOVEMENT_SPEED, 0);
        activeInstance = null;
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.world.isRemote) {
            setPosition(getPosX(), getPosY() + motionY, getPosZ());
            motionY *= 0.98f;

            if (ticksExisted >= LIVING_TICKS) {
                remove();
            }
        }
    }

    @Override
    protected void registerData() {
        this.getDataManager().register(DIED_MAX_HEALTH, 0.0f);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        this.getDataManager().set(DIED_MAX_HEALTH, compound.getFloat("died_max_health"));
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        compound.putFloat("died_max_health", this.getDataManager().get(DIED_MAX_HEALTH));
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getTrueSource() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) source.getTrueSource();
            player.getHeldItemMainhand().getCapability(WardEnchantmentsProvider.WARDS_ENCHANTMENTS).ifPresent(cap -> {
                for (WardEnchantment enchantment : cap.getEnchantments()) {
                    WardInit.getWards().get(enchantment.getWardId()).onSoulHit(new WardEventContext(enchantment.getLevel(), player, player.getHeldItemMainhand()), this.getDataManager().get(DIED_MAX_HEALTH), this);
                }
            });
            remove();
            return true;
        }
        return false;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @SubscribeEvent
    public static void entityDie(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        World world = entity.world;
        if (!world.isRemote() && entity instanceof LivingEntity) {
            SoulEntity soul = new SoulEntity(world, event.getEntity().getPosX(), event.getEntity().getPosY(), event.getEntity().getPosZ(), ((LivingEntity) entity).getMaxHealth());
            world.addEntity(soul);
        }
    }
}
