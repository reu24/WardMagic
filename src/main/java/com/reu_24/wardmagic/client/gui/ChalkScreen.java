package com.reu_24.wardmagic.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.container.ChalkContainer;
import com.reu_24.wardmagic.init.WardInit;
import com.reu_24.wardmagic.util.ModPacketHandler;
import com.reu_24.wardmagic.util.ModResourceLocation;
import com.reu_24.wardmagic.util.WardHelper;
import com.reu_24.wardmagic.ward.AbstractWard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChalkScreen extends ContainerScreen<ChalkContainer> {

    private static final ResourceLocation TEXTURE = new ModResourceLocation("textures/gui/chalk.png");
    private static final int OFFSET_Y = -20;
    private static final int WARDS_START_X = 18;
    private static final int WARDS_START_Y = 12;
    private static final int WARD_SIZE = 20;
    private static final int WARD_MARGIN = 2;
    private static final int WARD_PER_ROW = 5;
    private static final int WARD_ACTUAL_SIZE = 16;

    protected static int lastWardId = -1;
    protected ArrayList<Button> wardButtons;

    public ChalkScreen(ChalkContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);

        xSize = 146;
        ySize = 180;
    }

    @Override
    protected void init() {
        super.init();

        int[] knownWards = container.getKnownWards();
        wardButtons = new ArrayList<>(knownWards.length);
        for (int i = 0; i < knownWards.length; i++) {
            int column = i % WARD_PER_ROW;
            int row = i / WARD_PER_ROW;

            wardButtons.add(addButton(new ImageButton(guiLeft + WARDS_START_X + column * (WARD_SIZE + WARD_MARGIN), guiTop + WARDS_START_Y + row * (WARD_SIZE + WARD_MARGIN) + OFFSET_Y, WARD_SIZE, WARD_SIZE, 147, 0, WARD_SIZE, TEXTURE, (button) -> {
                int wardId = knownWards[wardButtons.indexOf(button)];
                lastWardId = wardId;
                ModPacketHandler.sendToServer("place_ward", wardId, getContainer().getPlacePos().up());
                closeScreen();
            })));
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        minecraft.getTextureManager().bindTexture(TEXTURE);
        blit(matrixStack, guiLeft, guiTop + OFFSET_Y, 0, 0, xSize, ySize, 256, 256);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        int[] knownWards = container.getKnownWards();
        for (int i = 0; i < knownWards.length; i++) {
            int column = i % WARD_PER_ROW;
            int row = i / WARD_PER_ROW;

            minecraft.getTextureManager().bindTexture(new ModResourceLocation("textures/block/ward_block_" + knownWards[i] + ".png"));
            blit(matrixStack, WARDS_START_X + column * (WARD_SIZE + WARD_MARGIN) + (WARD_SIZE / 2 - WARD_ACTUAL_SIZE / 2), WARDS_START_Y + row * (WARD_SIZE + WARD_MARGIN) + (WARD_SIZE / 2 - WARD_ACTUAL_SIZE / 2) + OFFSET_Y, 0, 0, 16, 16, 16, 16);
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        for (int i = 0; i < wardButtons.size(); i++) {
            if (wardButtons.get(i).isMouseOver(mouseX, mouseY)) {
                AbstractWard ward = WardInit.getWards().get(container.getKnownWards()[i]);
                List<ITextComponent> texts = WardHelper.isHoldingShift() ? getDetailTooltip(ward) : getDefaultTooltip(ward);
                renderWrappedToolTip(matrixStack, texts, mouseX, mouseY, minecraft.fontRenderer);
            }
        }
    }

    protected List<ITextComponent> getDefaultTooltip(AbstractWard ward) {
        return Arrays.asList(new StringTextComponent(ward.getName(true)),
                new StringTextComponent(ward.getTooltip()),
                new StringTextComponent(TextFormatting.YELLOW.toString() + new TranslationTextComponent("container." + WardMagic.MOD_ID + ".hold_shift").getString()));
    }

    protected List<ITextComponent> getDetailTooltip(AbstractWard ward) {
        return Arrays.asList(new StringTextComponent(ward.getName(true)),
                new StringTextComponent(ward.getTooltip()),
                new TranslationTextComponent("container." + WardMagic.MOD_ID + ".mana_required_per_level", ward.getManaRequiredPerLevel()),
                new TranslationTextComponent("container." + WardMagic.MOD_ID + ".additional_mana_percentage_required", ward.getAdditionalManaPercentageRequired() * 100 + "%"),
                new StringTextComponent(new TranslationTextComponent("container." + WardMagic.MOD_ID + ".mana_usage_level").getString() + ward.getManaUsageLevel().getColor() + ward.getManaUsageLevel().getLevel() + "."));
    }

    public static int getLastWardId() {
        return lastWardId;
    }
}