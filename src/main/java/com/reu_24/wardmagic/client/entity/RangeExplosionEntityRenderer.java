package com.reu_24.wardmagic.client.entity;

import com.reu_24.wardmagic.entities.RangeExplosionEntity;
import com.reu_24.wardmagic.util.ModResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RangeExplosionEntityRenderer extends AbstractRangeMagicEntityRenderer<RangeExplosionEntity> {
    public static final ResourceLocation TEXTURE = new ModResourceLocation("textures/entity/range_explosion.png");

    public RangeExplosionEntityRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public ResourceLocation getEntityTexture(RangeExplosionEntity entity) {
        return TEXTURE;
    }
}
