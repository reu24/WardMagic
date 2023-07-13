package com.reu_24.wardmagic.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.reu_24.wardmagic.entities.SoulEntity;
import com.reu_24.wardmagic.init.ModParticleTypes;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SoulParticle extends SpriteTexturedParticle {

    protected final SoulEntity soulEntity;

    protected SoulParticle(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ, IAnimatedSprite sprite) {
        super(world, x, y, z, motionX, motionY, motionZ);

        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        this.soulEntity = SoulEntity.activeInstance;
        canCollide = false;
        posX = x;
        posY = y;
        posZ = z;
        particleScale = 0.5f;
        maxAge = SoulEntity.LIVING_TICKS;
    }

    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {
        if (soulEntity.isAlive()) {
            super.renderParticle(buffer, renderInfo, partialTicks);
        }
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite sprite) {
            spriteSet = sprite;
        }

        @Override
        public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SoulParticle particle = new SoulParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
            particle.selectSpriteRandomly(spriteSet);
            return particle;
        }
    }
}
