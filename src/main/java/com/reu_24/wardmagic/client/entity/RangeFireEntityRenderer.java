package com.reu_24.wardmagic.client.entity;

import com.reu_24.wardmagic.entities.RangeFireEntity;
import com.reu_24.wardmagic.util.ModResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RangeFireEntityRenderer extends AbstractRangeMagicEntityRenderer<RangeFireEntity> {
    public static final ResourceLocation TEXTURE = new ModResourceLocation("textures/entity/range_fire.png");

    public RangeFireEntityRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public ResourceLocation getEntityTexture(RangeFireEntity entity) {
        return TEXTURE;
    }
}
