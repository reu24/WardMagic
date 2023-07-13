package com.reu_24.wardmagic.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.container.WardCompassContainer;
import com.reu_24.wardmagic.util.WardHelper;
import com.reu_24.wardmagic.util.ModResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.widget.Slider;

public class WardCompassScreen extends ContainerScreen<WardCompassContainer> {

    private static final ResourceLocation TEXTURE = new ModResourceLocation("textures/gui/ward_compass.png");

    protected TranslationTextComponent radius = new TranslationTextComponent("container." + WardMagic.MOD_ID + ".radius");
    Slider slider;

    public WardCompassScreen(WardCompassContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);

        xSize = 176;
        ySize = 82;
    }

    @Override
    protected void init() {
        super.init();

        if (slider == null) {
            slider = new Slider(guiLeft + 22, guiTop + 25, 134, 20, new StringTextComponent(""), new StringTextComponent(""), 1, WardHelper.MAX_CIRCLE_RADIUS, 1, false, true, (a) -> {
            });
            addButton(slider);
            addButton(new Button(guiLeft + 65, guiTop + 53, 42, 19, new TranslationTextComponent("container." + WardMagic.MOD_ID + ".ok"), (button) -> {
                WardHelper.placeWardCompassParticles(Minecraft.getInstance().world, slider.getValueInt(), getContainer().getPlacePos());
                closeScreen();
            }));
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        font.drawString(matrixStack, radius.getString(), 8.0f, 6.0f, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        minecraft.getTextureManager().bindTexture(TEXTURE);
        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize, 256, 256);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        slider.mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
