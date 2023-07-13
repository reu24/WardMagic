package com.reu_24.wardmagic.init;

import com.mojang.datafixers.util.Pair;
import com.reu_24.wardmagic.WardMagic;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

public class ModBlockTags {

    public static final BlockTag TEST_TAG = mod("test_tag");

    // ********************************************************************************************************************

    protected static ArrayList<BlockTag> tags;

    public static ArrayList<BlockTag> getTags() {
        return tags;
    }

    protected static BlockTag forge(String path) {
        String location = new ResourceLocation("forge", path).toString();
        BlockTag tag = new BlockTag(BlockTags.makeWrapperTag(location),
                ItemTags.makeWrapperTag(location),
                path);
        if (tags == null) {
            tags = new ArrayList<>();
        }
        tags.add(tag);
        return tag;
    }

    protected static BlockTag mod(final String path) {
        String location = new ResourceLocation(WardMagic.MOD_ID, path).toString();
        BlockTag tag = new BlockTag(BlockTags.makeWrapperTag(location),
                ItemTags.makeWrapperTag(location),
                path);
        if (tags == null) {
            tags = new ArrayList<>();
        }
        tags.add(tag);
        return tag;
    }

    public static class BlockTag {
        public final ITag.INamedTag<Block> blockTag;
        public final ITag.INamedTag<Item> itemTag;
        public final String path;

        public BlockTag(ITag.INamedTag<Block> blockTag, ITag.INamedTag<Item> itemTag, String path) {
            this.blockTag = blockTag;
            this.itemTag = itemTag;
            this.path = path;
        }
    }
}
