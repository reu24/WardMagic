package com.reu_24.wardmagic.init;

import com.mojang.datafixers.util.Pair;
import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.util.ModResourceLocation;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

public class ModItemTags {

    protected static ArrayList<ITag.INamedTag<Item>> tags;

    public static ArrayList<ITag.INamedTag<Item>> getTags() {
        return tags;
    }

    public static ITag.INamedTag<Item> fromPath(String path) {
        return tags.stream()
                .filter(t -> t.getName().getPath().equals(path))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    protected static ITag.INamedTag<Item> forge(String path) {
        ITag.INamedTag<Item> tag = ItemTags.makeWrapperTag(new ResourceLocation("forge", path).toString());
        if (tags == null) {
            tags = new ArrayList<>();
        }
        tags.add(tag);
        return tag;
    }

    protected static ITag.INamedTag<Item> mod(String path) {
        ITag.INamedTag<Item> tag = ItemTags.makeWrapperTag(new ModResourceLocation(path).toString());
        if (tags == null) {
            tags = new ArrayList<>();
        }
        tags.add(tag);
        return tag;
    }
}
