package com.reu_24.wardmagic.client.entity;

import com.reu_24.wardmagic.entities.SoulEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class SoulEntityRenderer extends EntityRenderer<SoulEntity> {
    public SoulEntityRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public ResourceLocation getEntityTexture(SoulEntity entity) {
        return null;
    }
}
