package com.reu_24.wardmagic.client.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;

public abstract class AbstractRangeMagicEntityRenderer <T extends Entity> extends EntityRenderer<T> {

    public AbstractRangeMagicEntityRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        IVertexBuilder b = bufferIn.getBuffer(RenderType.getBeaconBeam(getEntityTexture(entityIn), false));

        matrixStackIn.push();
        matrixStackIn.translate(0.5f, 0.5f, 0.5f);
        matrixStackIn.rotate(new Quaternion(45.0f, 0.0f, 0.0f, true));
        matrixStackIn.translate(-0.5f, -0.5f, -0.5f);
        //matrixStackIn.translate(-0.5, -0.3, -0.4);
        drawCube(matrixStackIn, b);
        //matrixStackIn.translate(0.5, 0.3, 0.4);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(0.5f, 0.5f, 0.5f);
        matrixStackIn.rotate(new Quaternion(0.0f, 0.0f, 45.0f, true));
        matrixStackIn.translate(-0.5f, -0.5f, -0.5f);
        //matrixStackIn.translate(-0.2, 0.5, -0.5);
        drawCube(matrixStackIn, b);
        //matrixStackIn.translate(0.2, -0.5, 0.5);
        matrixStackIn.pop();

        matrixStackIn.push();
        //matrixStackIn.translate(-0.5, 0.0, -0.5);
        drawCube(matrixStackIn, b);
        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private static void drawCube(MatrixStack matrixStackIn, IVertexBuilder b) {
        MatrixStack.Entry matrixstack$entry = matrixStackIn.getLast();
        Matrix4f matrix4f = matrixstack$entry.getMatrix();
        Matrix3f matrix3f = matrixstack$entry.getNormal();
        addVerticalQuad(matrix4f, matrix3f, b, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1);
        addVerticalQuad(matrix4f, matrix3f, b, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1);
        addVerticalQuad(matrix4f, matrix3f, b, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1);
        addVerticalQuad(matrix4f, matrix3f, b, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 1, 0, 1);
        addHorizontalQuad(matrix4f, matrix3f, b, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 0, 1);
        addHorizontalQuad(matrix4f, matrix3f, b, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 1, 0, 1);
    }

    private static void addHorizontalQuad(Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder bufferIn, float red, float green, float blue, float alpha, int y, float x1, float z1, float x2, float z2, float u1, float u2, float v1, float v2) {
        addVertex(matrixPos, matrixNormal, bufferIn, red, green, blue, alpha, y, x1, z1, u2, v1);
        addVertex(matrixPos, matrixNormal, bufferIn, red, green, blue, alpha, y, x2, z1, u2, v2);
        addVertex(matrixPos, matrixNormal, bufferIn, red, green, blue, alpha, y, x2, z2, u1, v2);
        addVertex(matrixPos, matrixNormal, bufferIn, red, green, blue, alpha, y, x1, z2, u1, v1);
    }

    private static void addVerticalQuad(Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder bufferIn, float red, float green, float blue, float alpha, int yMin, int yMax, float x1, float z1, float x2, float z2, float u1, float u2, float v1, float v2) {
        addVertex(matrixPos, matrixNormal, bufferIn, red, green, blue, alpha, yMax, x1, z1, u2, v1);
        addVertex(matrixPos, matrixNormal, bufferIn, red, green, blue, alpha, yMin, x1, z1, u2, v2);
        addVertex(matrixPos, matrixNormal, bufferIn, red, green, blue, alpha, yMin, x2, z2, u1, v2);
        addVertex(matrixPos, matrixNormal, bufferIn, red, green, blue, alpha, yMax, x2, z2, u1, v1);
    }

    private static void addVertex(Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder bufferIn, float red, float green, float blue, float alpha, int y, float x, float z, float texU, float texV) {
        bufferIn.pos(matrixPos, x, (float)y, z).color(red, green, blue, alpha).tex(texU, texV).overlay(OverlayTexture.NO_OVERLAY).lightmap(15728880).normal(matrixNormal, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
