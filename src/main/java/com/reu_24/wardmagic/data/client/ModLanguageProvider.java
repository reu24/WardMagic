package com.reu_24.wardmagic.data.client;

import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.init.WardInit;
import com.reu_24.wardmagic.itemgroup.WardMagicItemGroup;
import com.reu_24.wardmagic.util.ReflectionHelper;
import com.reu_24.wardmagic.ward.AbstractWard;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(DataGenerator gen) {
        super(gen, WardMagic.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        ReflectionHelper.forEachBlock((blockData, field) -> {
            add(ReflectionHelper.getBlock(field), blockData.name());
            return null;
        });

        ReflectionHelper.forEachItem((itemData, field) -> {
            add(ReflectionHelper.getItem(field), itemData.name());
            return null;
        });

        for (AbstractWard ward : WardInit.getWards()) {
            add(ward.getNameTranslation().getFirst(), ward.getNameTranslation().getSecond());
            add(ward.getTooltipTranslation().getFirst(), ward.getTooltipTranslation().getSecond());
        }

        add("itemGroup.ward_magic", "Ward Magic");
        add("container.ward_magic.chalk", "Chalk");
        add("container." + WardMagic.MOD_ID + ".radius", "Radius");
        add("container." + WardMagic.MOD_ID + ".ok", "Ok");
        add("block." + WardMagic.MOD_ID + ".ward_altar", "Ward Altar");
        add("container." + WardMagic.MOD_ID + ".ward_altar", "Ward Altar");
        add("container." + WardMagic.MOD_ID + ".start_ritual", "Start ritual");
        add("ritual." + WardMagic.MOD_ID + ".fail", "Failed to start ritual. No circular wards were found. Use the ward compass to find out where to place the wards.");
        add("death.attack.deadly_matter.message", "%1$s overestimated his power.");
        add("container." + WardMagic.MOD_ID + ".mana_required_per_level", "[Ritual] Requires per level %1$s Mana.");
        add("container." + WardMagic.MOD_ID + ".additional_mana_percentage_required", "[Ritual] Requires additionally %1$s more Mana per Level.");
        add("container." + WardMagic.MOD_ID + ".mana_usage_level", "[Usage] Mana level: ");
        add("ward." + WardMagic.MOD_ID + ".ward", "Ward");
        add("item." + WardMagic.MOD_ID + ".mana_holder_tooltip", "%1$s Mana / %2$s Mana");
        add("item." + WardMagic.MOD_ID + ".mana_holder", "Mana Holder");
        add("item." + WardMagic.MOD_ID + ".infinity_mana_holder", "Infinity Mana Holder");
        add("entity." + WardMagic.MOD_ID + ".range_explosion", "Range Explosion");
        add("magnifyingGlass." + WardMagic.MOD_ID + ".new", "New Ward discovered: %1$s");
        add("magnifyingGlass." + WardMagic.MOD_ID + ".old", "Ward already discovered: %1$s");
        add("container." + WardMagic.MOD_ID + ".ward_altar.mana_required", "Mana required: %1$s");
        add("container." + WardMagic.MOD_ID + ".hold_shift", "Hold SHIFT for more information.");
        add("container." + WardMagic.MOD_ID + ".chapter_how_find_wards_title", "Finding wards");
        add("container." + WardMagic.MOD_ID + ".chapter_magical_weapons_title", "Magical weapons");
        add("container." + WardMagic.MOD_ID + ".chapter_preparing_a_ritual_title", "Preparing a ritual");
        add("container." + WardMagic.MOD_ID + ".chapter_executing_a_ritual_title", "Execute a ritual");
        add("container." + WardMagic.MOD_ID + ".chapter_getting_mana_title", "Getting mana");
        add("container." + WardMagic.MOD_ID + ".chapter_range_ward_title", "Range ward");
        add("key." + WardMagic.MOD_ID + ".place_ward", "Place last ward");
        add("item." + WardMagic.MOD_ID + ".chalk_tooltip", "Press %1$s to place the last ward.");
        add("container." + WardMagic.MOD_ID + ".ward_altar.no_circle", "No circle found.");

        add("container." + WardMagic.MOD_ID + ".chapter_how_find_wards", "How to find wards\n\nThere are ancient structures scattered around the world. These may contain magical wards. Using a \u00a76Magnifying Glass\u00a70 you can analyze these wards and recreate the using a \u00a76Chalk\u00a70.");
        add("container." + WardMagic.MOD_ID + ".chapter_magical_weapons", "Magical weapons\n\nUsing \u00a76Magical Planks\u00a70 obtained by a magical tree you can craft a \u00a76Magical Sword\u00a70 and a \u00a7Ward Wand\u00a70. These weapons can be enchanted in ward rituals.");
        add("container." + WardMagic.MOD_ID + ".chapter_preparing_a_ritual", "Preparing a ritual\n\nPlace down a \u00a76Ward Altar\u00a70 and surround it with wards of your choice. Using a \u00a76Ward Compass\u00a70 you can figure out where to place the wards. The more wards of the same type you use the more mana it requires.");
        add("container." + WardMagic.MOD_ID + ".chapter_executing_a_ritual", "Executing a ritual.\n\nPlace a magical weapon in the \u00a76Ward Altar\u00a70 and start the ritual. If the \u00a76Ward Altar\u00a70 doesn't have enough mana, deadly matter spreads that will consume the mana of the surrounding. After enough mana is consumed the weapon will drop.");
        add("container." + WardMagic.MOD_ID + ".chapter_getting_mana", "Getting mana.\n\nEvery action that involves wards requires mana. So the more mana you have the stronger you can enchant your weapons. Mana can be stored in a \u00a76Mana Container\u00a70. The main source of mana is the \u00a76Mana Steal Ward\u00a70.");
        add("container." + WardMagic.MOD_ID + ".chapter_range_ward", "Range ward.\n\nWithout other wards the \u00a76Range Ward\u00a70 doesn't do anything. But combining it with the \u00a76Fire Ward\u00a70 or the \u00a76Explosion Ward\u00a70 it will execute a range attack.");
    }
}
