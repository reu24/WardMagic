package com.reu_24.wardmagic.data;

import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.init.BlockInit;
import com.reu_24.wardmagic.init.ItemInit;
import com.reu_24.wardmagic.init.ModBlockTags;
import com.reu_24.wardmagic.init.ModItemTags;
import com.reu_24.wardmagic.util.ReflectionHelper;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, WardMagic.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        registerBlockTags();
        registerItemTags();
    }

    protected void registerBlockTags() {
        for (ModBlockTags.BlockTag tag : ModBlockTags.getTags()) {
            copy(tag.blockTag, tag.itemTag);
        }
    }

    protected void registerItemTags() {
    }
}
