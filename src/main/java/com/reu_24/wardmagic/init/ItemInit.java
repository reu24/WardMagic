package com.reu_24.wardmagic.init;

import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.data.Annotations;
import com.reu_24.wardmagic.itemgroup.WardMagicItemGroup;
import com.reu_24.wardmagic.object.item.*;
import com.reu_24.wardmagic.util.MagicalWoodTier;
import com.reu_24.wardmagic.ward.AbstractWard;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WardMagic.MOD_ID);

    @Annotations.ItemData(name = "Magnifying Glass", id = "magnifying_glass", itemParent = Annotations.ItemParent.HAND_HELD)
    public static final RegistryObject<Item> MAGNIFYING_GLASS = ITEMS.register("magnifying_glass",
            () -> new MagnifyingGlassItem(new Item.Properties().group(WardMagicItemGroup.INSTANCE).maxStackSize(1)));

    @Annotations.ItemData(name = "Chalk", id = "chalk", itemParent = Annotations.ItemParent.HAND_HELD)
    public static final RegistryObject<Item> CHALK = ITEMS.register("chalk",
            () -> new ChalkItem(new Item.Properties().group(WardMagicItemGroup.INSTANCE).maxStackSize(1)));

    @Annotations.ItemData(name = "Magical Stick", id = "magical_stick")
    public static final RegistryObject<Item> MAGICAL_STICK = ITEMS.register("magical_stick",
            () -> new FurnaceBurnableItem(new Item.Properties().group(WardMagicItemGroup.INSTANCE), 100));

    @Annotations.ItemData(name = "Ward Compass", id = "ward_compass", itemParent = Annotations.ItemParent.HAND_HELD)
    public static final RegistryObject<Item> WARD_COMPASS = ITEMS.register("ward_compass",
            () -> new WardCompassItem(new Item.Properties().group(WardMagicItemGroup.INSTANCE).maxStackSize(1)));

    @Annotations.ItemData(name = "Ward Wand", id = "ward_wand", itemParent = Annotations.ItemParent.HAND_HELD)
    public static final RegistryObject<Item> WARD_WAND = ITEMS.register("ward_wand",
            () -> new WardEnchantableItem(new Item.Properties().group(WardMagicItemGroup.INSTANCE).maxStackSize(1), AbstractWard.WAND));

    @Annotations.ItemData(name = "Magical Sword", id = "magical_sword", itemParent = Annotations.ItemParent.HAND_HELD)
    public static final RegistryObject<Item> MAGICAL_SWORD = ITEMS.register("magical_sword",
            () -> new SwordWardEnchantableItem(MagicalWoodTier.MAGICAL_WOOD, 5, -2.4F, new Item.Properties().group(WardMagicItemGroup.INSTANCE).maxStackSize(1)));

    public static final RegistryObject<Item> MANA_HOLDER = ITEMS.register("mana_holder",
            () -> new ManaHolderItem(new Item.Properties().group(WardMagicItemGroup.INSTANCE).maxStackSize(1), 0, 15));

    public static final RegistryObject<Item> INFINITY_MANA_HOLDER = ITEMS.register("infinity_mana_holder",
            () -> new ManaHolderItem(new Item.Properties().group(WardMagicItemGroup.INSTANCE).maxStackSize(1), Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY));

    @Annotations.ItemData(name = "Book of the Ward Magician", id = "book_of_the_ward_magician", itemParent = Annotations.ItemParent.HAND_HELD)
    public static final RegistryObject<Item> BOOK_OF_THE_WARD_MAGICIAN = ITEMS.register("book_of_the_ward_magician",
            () -> new BookOfTheWardMagicianItem(new Item.Properties().group(WardMagicItemGroup.INSTANCE).maxStackSize(1)));
}
