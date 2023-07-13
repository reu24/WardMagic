package com.reu_24.wardmagic.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.container.WardAltarContainer;
import com.reu_24.wardmagic.util.ModPacketHandler;
import com.reu_24.wardmagic.util.ModResourceLocation;
import com.reu_24.wardmagic.util.WardHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class WardAltarScreen extends ContainerScreen<WardAltarContainer> {

    private static final ResourceLocation TEXTURE = new ModResourceLocation("textures/gui/ward_altar.png");

    protected final TranslationTextComponent startRitual = new TranslationTextComponent("container." + WardMagic.MOD_ID + ".start_ritual");
    protected Button button;

    public WardAltarScreen(WardAltarContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);

        xSize = 176;
        ySize = 166;
    }

    @Override
    protected void init() {
        super.init();

        button = addButton(new Button(guiLeft + 60, guiTop + 65, 65, 13, startRitual, button -> {
            ModPacketHandler.sendToServer("start_ritual", getContainer().getPos());
            closeScreen();
        }));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        super.drawGuiContainerForegroundLayer(matrixStack, x, y);
        float manaRequired = WardHelper.getManaRequired(Minecraft.getInstance().world, getContainer().getPos());

        button.active = getContainer().getInventory().get(40).getCount() == 1 && manaRequired != 0;

        String text;
        if (manaRequired == 0) {
            text = new TranslationTextComponent("container." + WardMagic.MOD_ID + ".ward_altar.no_circle").getString();
        } else {
        text = new TranslationTextComponent("container." + WardMagic.MOD_ID + ".ward_altar.mana_required", manaRequired).getString();
        }
        font.drawString(matrixStack, text, 40.0f, 54.0f, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        minecraft.getTextureManager().bindTexture(TEXTURE);
        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize, 256, 256);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }
}
