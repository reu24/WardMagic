package com.reu_24.wardmagic.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.reu_24.wardmagic.init.BlockInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WardCompassParticle extends SpriteTexturedParticle {

    public static final int SEARCH_RANGE = 3;

    protected boolean removed = false;

    protected WardCompassParticle(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ, IAnimatedSprite sprite) {
        super(world, x, y, z, motionX, motionY, motionZ);

        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        posX = x;
        posY = y;
        posZ = z;
        particleScale = 0.5f;
        maxAge = 30 * 60 * 10;
    }

    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {
        if (!removed) {
            if (hasWardBlock()) {
                removed = true;
            } else {
                super.renderParticle(buffer, renderInfo, partialTicks);
            }
        }
    }

    protected boolean hasWardBlock() {
        World world = Minecraft.getInstance().world;
        int startY = (int) Math.max(posY - SEARCH_RANGE, 0);
        int endY = (int) Math.min(posY + SEARCH_RANGE, 255);
        for (int y = startY; y <= endY; y++) {
            if (world.getBlockState(new BlockPos(posX, y, posZ)).getBlock() == BlockInit.WARD_BLOCK.get()) {
                return true;
            }
        }
        return false;
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
            WardCompassParticle particle = new WardCompassParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
            particle.selectSpriteRandomly(spriteSet);
            return particle;
        }
    }
}
