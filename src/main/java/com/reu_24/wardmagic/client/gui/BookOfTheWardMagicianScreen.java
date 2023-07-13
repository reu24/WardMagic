package com.reu_24.wardmagic.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.container.BookOfTheWardMagicianContainer;
import com.reu_24.wardmagic.util.ModResourceLocation;
import com.reu_24.wardmagic.util.PageSoundImageButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ChangePageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class BookOfTheWardMagicianScreen extends ContainerScreen<BookOfTheWardMagicianContainer> {
    private static final ResourceLocation TEXTURE = new ModResourceLocation("textures/gui/book_of_the_ward_magician.png");

    private static final int OFFSET_Y = -20;
    private static final int CHAPTER_X = 18;
    private static final int CHAPTER_LABEL_X = 45;
    private static final int CHAPTER_START_Y = 12;
    private static final int CHAPTER_SIZE = 20;
    private static final int CHAPTER_MARGIN = 2;
    private static final int CHAPTER_ACTUAL_SIZE = 16;
    private static final int TEXT_OFFSET_Y = 7;

    private static final ChapterData[] chapters = {
            new ChapterData(new TranslationTextComponent("container." + WardMagic.MOD_ID + ".chapter_how_find_wards_title"),
                    new TranslationTextComponent("container." + WardMagic.MOD_ID + ".chapter_how_find_wards"),
                    new ModResourceLocation("textures/item/magnifying_glass.png")),

            new ChapterData(new TranslationTextComponent("container." + WardMagic.MOD_ID + ".chapter_magical_weapons_title"),
                    new TranslationTextComponent("container." + WardMagic.MOD_ID + ".chapter_magical_weapons"),
                    new ModResourceLocation("textures/item/magical_sword.png")),

            new ChapterData(new TranslationTextComponent("container." + WardMagic.MOD_ID + ".chapter_preparing_a_ritual_title"),
                    new TranslationTextComponent("container." + WardMagic.MOD_ID + ".chapter_preparing_a_ritual"),
                    new ModResourceLocation("textures/item/ward_compass.png")),

            new ChapterData(new TranslationTextComponent("container." + WardMagic.MOD_ID + ".chapter_executing_a_ritual_title"),
                    new TranslationTextComponent("container." + WardMagic.MOD_ID + ".chapter_executing_a_ritual"),
                    new ModResourceLocation("textures/item/chalk.png")),

            new ChapterData(new TranslationTextComponent("container." + WardMagic.MOD_ID + ".chapter_getting_mana_title"),
                    new TranslationTextComponent("container." + WardMagic.MOD_ID + ".chapter_getting_mana"),
                    new ModResourceLocation("textures/item/mana_holder4.png")),

            new ChapterData(new TranslationTextComponent("container." + WardMagic.MOD_ID + ".chapter_range_ward_title"),
                    new TranslationTextComponent("container." + WardMagic.MOD_ID + ".chapter_range_ward"),
                    new ModResourceLocation("textures/block/ward_block_7.png"))
    };

    protected List<Button> chapterButtons = new ArrayList<>();
    protected Button backButton;
    protected int currentChapter = -1;

    public BookOfTheWardMagicianScreen(BookOfTheWardMagicianContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);

        xSize = 146;
        ySize = 180;
    }

    @Override
    protected void init() {
        super.init();

        chapterButtons.clear();
        for (int i = 0; i < chapters.length; i++) {
            chapterButtons.add(addButton(new PageSoundImageButton(guiLeft + CHAPTER_X, guiTop + CHAPTER_START_Y + i * (CHAPTER_SIZE + CHAPTER_MARGIN) + OFFSET_Y, CHAPTER_SIZE, CHAPTER_SIZE, 147, 0, CHAPTER_SIZE, TEXTURE, (button) -> {
                backButton.visible = true;
                for (Button chapterButton : chapterButtons) {
                    chapterButton.visible = false;
                }
                currentChapter = chapterButtons.indexOf(button);
            })));
        }

        backButton = addButton(new ChangePageButton(((width - 192) / 2) + 43, 159, false, (button -> {
            backButton.visible = false;
            for (Button chapterButton : chapterButtons) {
                chapterButton.visible = true;
            }
            currentChapter = -1;
        }), true));
        backButton.visible = false;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        minecraft.getTextureManager().bindTexture(TEXTURE);
        blit(matrixStack, guiLeft, guiTop + OFFSET_Y, 0, 0, xSize, ySize, 256, 256);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        if (currentChapter == -1) {
            renderChapters(matrixStack);
        } else {
            renderChapterText(matrixStack);
        }
    }

    protected void renderChapters(MatrixStack matrixStack) {
        for (int i = 0; i < chapters.length; i++) {
            String text = chapters[i].title.getString();
            Minecraft.getInstance().fontRenderer.drawString(matrixStack, text, CHAPTER_LABEL_X, CHAPTER_START_Y + i * (CHAPTER_SIZE + CHAPTER_MARGIN) + OFFSET_Y + TEXT_OFFSET_Y, 0x404040);

            minecraft.getTextureManager().bindTexture(chapters[i].titleImageLocation);
            blit(matrixStack, CHAPTER_X + CHAPTER_MARGIN, CHAPTER_START_Y + i * (CHAPTER_SIZE + CHAPTER_MARGIN) + (CHAPTER_SIZE / 2 - CHAPTER_ACTUAL_SIZE / 2) + OFFSET_Y, 0, 0, 16, 16, 16, 16);
        }
    }

    protected void renderChapterText(MatrixStack matrixStack) {
        TranslationTextComponent text = chapters[currentChapter].text;
        List<IReorderingProcessor> lines = this.font.trimStringToWidth(text, 114);
        int k = Math.min(128 / 9, lines.size());
        for(int l = 0; l < k; ++l) {
            IReorderingProcessor ireorderingprocessor = lines.get(l);
            this.font.func_238422_b_(matrixStack, ireorderingprocessor, CHAPTER_X, (float)(CHAPTER_START_Y + l * 9) + OFFSET_Y, 0);
        }
    }

    private static class ChapterData {
        private final TranslationTextComponent title;
        private final TranslationTextComponent text;
        private final ResourceLocation titleImageLocation;

        private ChapterData(TranslationTextComponent title, TranslationTextComponent text, ResourceLocation titleImageLocation) {
            this.title = title;
            this.text = text;
            this.titleImageLocation = titleImageLocation;
        }
    }
}
