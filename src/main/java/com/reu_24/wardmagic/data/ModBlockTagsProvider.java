package com.reu_24.wardmagic.data;

import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.init.BlockInit;
import com.reu_24.wardmagic.init.ModBlockTags;
import com.reu_24.wardmagic.util.ReflectionHelper;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, WardMagic.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        getOrCreateBuilder(BlockTags.LEAVES).add(BlockInit.MAGICAL_LEAVES.get());
        getOrCreateBuilder(BlockTags.LOGS_THAT_BURN).add(BlockInit.MAGICAL_LOG.get());
        getOrCreateBuilder(BlockTags.PLANKS).add(BlockInit.MAGICAL_PLANKS.get());
    }
}
