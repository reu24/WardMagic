package com.reu_24.wardmagic.data.client;

import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.util.ReflectionHelper;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, WardMagic.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        registerBlockItems();
        registerItems();
    }

    protected void registerBlockItems() {
        ReflectionHelper.forEachBlock((blockData, field) -> {
            if (blockData.generateItem()) {
                getBuilder(blockData.id()).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + blockData.id())));
            }
            return null;
        });

        getBuilder("magical_sapling").parent(getExistingFile(mcLoc("item/generated")))
                .texture("layer0", "block/magical_sapling");
        getBuilder("crack_stone").parent(new ModelFile.UncheckedModelFile(modLoc("block/crack_stone_3")));
    }

    protected void registerItems() {
        ReflectionHelper.forEachItem((itemData, field) -> {
            ItemModelBuilder builder = getBuilder(itemData.id()).parent(getExistingFile(mcLoc(itemData.itemParent().getPath())));
            if (itemData.addTexture()) {
                builder.texture("layer0", "item/" + itemData.id());
            }
            return null;
        });
    }
}
